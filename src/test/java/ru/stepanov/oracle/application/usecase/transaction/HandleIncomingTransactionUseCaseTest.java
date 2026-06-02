package ru.stepanov.oracle.application.usecase.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.stepanov.oracle.application.port.TriggerEventSenderPort;
import ru.stepanov.oracle.application.repository.IncomingTransactionRepository;
import ru.stepanov.oracle.application.repository.MatchAttemptRepository;
import ru.stepanov.oracle.application.repository.ProcessingErrorRepository;
import ru.stepanov.oracle.application.repository.TriggerEventRepository;
import ru.stepanov.oracle.application.repository.WatchProfileRepository;
import ru.stepanov.oracle.domain.model.incomingtransaction.CreditDebitCode;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransactionStatus;
import ru.stepanov.oracle.domain.model.processingerror.ProcessingError;
import ru.stepanov.oracle.domain.model.processingerror.ProcessingErrorSource;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEventDeliveryStatus;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;
import ru.stepanov.oracle.domain.model.watchprofile.RuleField;
import ru.stepanov.oracle.domain.model.watchprofile.RuleOperator;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;
import ru.stepanov.oracle.infrastructure.repository.InMemoryIncomingTransactionRepository;
import ru.stepanov.oracle.infrastructure.repository.InMemoryMatchAttemptRepository;
import ru.stepanov.oracle.infrastructure.repository.InMemoryTriggerEventRepository;
import ru.stepanov.oracle.infrastructure.repository.InMemoryWatchProfileRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandleIncomingTransactionUseCaseTest {

    @Mock
    private WatchProfileRepository watchProfileRepository;

    @Mock
    private TriggerEventSenderPort triggerEventSenderPort;

    @Mock
    private ProcessingErrorRepository processingErrorRepository;

    private HandleIncomingTransactionUseCase useCase;
    private InMemoryWatchProfileRepository profileStore;

    @BeforeEach
    void setUp() {
        IncomingTransactionRepository incomingTransactionRepository = new InMemoryIncomingTransactionRepository();
        MatchAttemptRepository matchAttemptRepository = new InMemoryMatchAttemptRepository();
        TriggerEventRepository triggerEventRepository = new InMemoryTriggerEventRepository();
        profileStore = new InMemoryWatchProfileRepository();
        useCase = new HandleIncomingTransactionUseCase(
                incomingTransactionRepository,
                watchProfileRepository,
                matchAttemptRepository,
                triggerEventRepository,
                triggerEventSenderPort,
                processingErrorRepository
        );
    }

    @Test
    void execute_onTriggerSendFailure_logsErrorAndSchedulesRetry() {
        WatchProfile profile = registerProfile();
        when(watchProfileRepository.findByPaymentTokenOrAccountID("token-abc", null))
                .thenReturn(Optional.of(profile));
        doThrow(new RuntimeException("RabbitMQ unavailable")).when(triggerEventSenderPort).send(any(TriggerEvent.class));

        IncomingTransaction result = useCase.execute(command("TX-SEND-FAIL"));

        assertEquals(IncomingTransactionStatus.Matched, result.getStatus());

        ArgumentCaptor<ProcessingError> errorCaptor = ArgumentCaptor.forClass(ProcessingError.class);
        verify(processingErrorRepository).save(errorCaptor.capture());
        ProcessingError error = errorCaptor.getValue();
        assertEquals(ProcessingErrorSource.TRIGGER_EVENT_SEND, error.getSource());
        assertEquals("TX-SEND-FAIL", error.getExternalTransactionID());
        assertNotNull(error.getTriggerEventID());
        assertEquals("RabbitMQ unavailable", error.getMessage());
    }

    @Test
    void execute_onProcessingFailure_marksTransactionErrorAndLogs() {
        MatchAttemptRepository failingMatchAttemptRepository = org.mockito.Mockito.mock(MatchAttemptRepository.class);
        org.mockito.Mockito.when(failingMatchAttemptRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new RuntimeException("match failed"));
        useCase = new HandleIncomingTransactionUseCase(
                new InMemoryIncomingTransactionRepository(),
                watchProfileRepository,
                failingMatchAttemptRepository,
                new InMemoryTriggerEventRepository(),
                triggerEventSenderPort,
                processingErrorRepository
        );

        WatchProfile profile = registerProfile();
        when(watchProfileRepository.findByPaymentTokenOrAccountID("token-abc", null))
                .thenReturn(Optional.of(profile));

        IncomingTransaction result = useCase.execute(command("TX-PROC-FAIL"));

        assertEquals(IncomingTransactionStatus.Error, result.getStatus());
        assertEquals("match failed", result.getErrorMessage());

        ArgumentCaptor<ProcessingError> errorCaptor = ArgumentCaptor.forClass(ProcessingError.class);
        verify(processingErrorRepository).save(errorCaptor.capture());
        assertEquals(ProcessingErrorSource.TRANSACTION_PROCESSING, errorCaptor.getValue().getSource());
        assertEquals("TX-PROC-FAIL", errorCaptor.getValue().getExternalTransactionID());
    }

    private WatchProfile registerProfile() {
        WatchProfile profile = WatchProfile.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "UNDESIRABLE_PURCHASE",
                "token-abc",
                null,
                List.of(new RuleCondition(UUID.randomUUID(), RuleField.MCC, RuleOperator.In, "5912")),
                1,
                new DebitConfig("200.00", "RUB", "charity-token", UUID.randomUUID(), UUID.randomUUID())
        );
        return profileStore.save(profile);
    }

    private static HandleIncomingTransactionUseCase.IncomingTransactionCommand command(String transactionId) {
        return new HandleIncomingTransactionUseCase.IncomingTransactionCommand(
                transactionId,
                null,
                "token-abc",
                new BigDecimal("350.00"),
                "RUB",
                CreditDebitCode.Debit,
                "Shop",
                "m-1",
                "5912",
                Instant.parse("2026-05-25T10:00:00Z"),
                Instant.parse("2026-05-25T10:00:00Z"),
                null,
                null
        );
    }
}
