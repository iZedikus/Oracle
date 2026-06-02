package ru.stepanov.oracle.infrastructure.messaging.publisher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.stepanov.oracle.domain.model.incomingtransaction.CreditDebitCode;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;
import ru.stepanov.oracle.domain.model.incomingtransaction.TransactionData;
import ru.stepanov.oracle.domain.model.matchattempt.MatchAttempt;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.domain.model.watchprofile.ActiveRule;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;
import ru.stepanov.oracle.domain.model.watchprofile.RuleField;
import ru.stepanov.oracle.domain.model.watchprofile.RuleOperator;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;
import ru.stepanov.oracle.infrastructure.messaging.dto.TriggerEventMessage;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitTriggerEventSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitTriggerEventSender sender;

    @Test
    void send_mapsTriggerEventToContractMessage() {
        UUID externalUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID externalUserScenarioId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        UUID consentId = UUID.fromString("00000000-0000-0000-0000-000000000003");
        UUID sourceAccountId = UUID.fromString("00000000-0000-0000-0000-000000000004");
        DebitConfig debitConfig = new DebitConfig(
                "200.00",
                "RUB",
                "charity-token-xyz",
                consentId,
                sourceAccountId
        );

        WatchProfile profile = WatchProfile.create(
                externalUserId,
                externalUserScenarioId,
                "UNDESIRABLE_PURCHASE",
                "token-abc-xyz",
                null,
                List.of(new RuleCondition(UUID.randomUUID(), RuleField.MCC, RuleOperator.In, "5912")),
                1,
                debitConfig
        );
        ActiveRule matchedRule = profile.getRules().getFirst();

        IncomingTransaction transaction = IncomingTransaction.receive(
                "TX-2026-001",
                profile.getWatchProfileID(),
                new TransactionData(
                        CreditDebitCode.Debit,
                        "5912",
                        "Табакoff ООО",
                        "merchant-1",
                        "350.00",
                        "RUB",
                        Instant.parse("2026-05-25T10:00:00Z"),
                        Instant.parse("2026-05-25T10:00:00Z")
                )
        );

        MatchAttempt matchAttempt = MatchAttempt.attempt(transaction, matchedRule);
        TriggerEvent triggerEvent = TriggerEvent.create(matchAttempt, profile, matchedRule, transaction);

        doAnswer(invocation -> {
            CorrelationData correlationData = invocation.getArgument(3, CorrelationData.class);
            @SuppressWarnings("deprecation")
            CorrelationData.Confirm ack = org.mockito.Mockito.mock(CorrelationData.Confirm.class);
            org.mockito.Mockito.when(ack.isAck()).thenReturn(true);
            correlationData.getFuture().complete(ack);
            return null;
        }).when(rabbitTemplate).convertAndSend(
                eq("oracle.events"),
                eq("trigger.matched"),
                any(TriggerEventMessage.class),
                any(CorrelationData.class));

        sender.send(triggerEvent);

        ArgumentCaptor<TriggerEventMessage> messageCaptor = ArgumentCaptor.forClass(TriggerEventMessage.class);
        verify(rabbitTemplate).convertAndSend(
                eq("oracle.events"),
                eq("trigger.matched"),
                messageCaptor.capture(),
                any(CorrelationData.class)
        );

        TriggerEventMessage message = messageCaptor.getValue();
        assertNotNull(message.messageId());
        assertEquals(triggerEvent.getOccurredAt(), message.occurredAt());
        assertEquals(triggerEvent.getTriggerEventID(), message.triggerEventId());
        assertEquals(externalUserScenarioId, message.externalUserScenarioId());
        assertEquals(externalUserId, message.externalUserId());
        assertEquals("TX-2026-001", message.triggerTransactionId());
        assertEquals("5912", message.matchedMcc());
        assertEquals("Табакoff ООО", message.matchedMerchantName());
        assertEquals("350.00", message.matchedAmount());
        assertEquals("RUB", message.matchedCurrency());
        assertEquals("UNDESIRABLE_PURCHASE", message.scenarioTypeCode());
        assertNotNull(message.debitConfig());
        assertEquals("200.00", message.debitConfig().debitAmount);
        assertEquals("RUB", message.debitConfig().currency);
        assertEquals("charity-token-xyz", message.debitConfig().recipientPaymentToken);
        assertEquals(consentId, message.debitConfig().consentId);
        assertEquals(sourceAccountId, message.debitConfig().sourceAccountId);
    }
}
