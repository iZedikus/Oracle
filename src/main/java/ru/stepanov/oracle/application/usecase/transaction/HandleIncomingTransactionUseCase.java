package ru.stepanov.oracle.application.usecase.transaction;

import org.springframework.stereotype.Service;
import ru.stepanov.oracle.application.port.TriggerEventSenderPort;
import ru.stepanov.oracle.application.repository.IncomingTransactionRepository;
import ru.stepanov.oracle.application.repository.MatchAttemptRepository;
import ru.stepanov.oracle.application.repository.TriggerEventRepository;
import ru.stepanov.oracle.application.repository.WatchProfileRepository;
import ru.stepanov.oracle.domain.model.incomingtransaction.CreditDebitCode;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;
import ru.stepanov.oracle.domain.model.incomingtransaction.TransactionData;
import ru.stepanov.oracle.domain.model.matchattempt.MatchAttempt;
import ru.stepanov.oracle.domain.model.matchattempt.MatchingResult;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.domain.model.watchprofile.ActiveRule;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfileStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class HandleIncomingTransactionUseCase {
    private final IncomingTransactionRepository incomingTransactionRepository;
    private final WatchProfileRepository watchProfileRepository;
    private final MatchAttemptRepository matchAttemptRepository;
    private final TriggerEventRepository triggerEventRepository;
    private final TriggerEventSenderPort triggerEventSenderPort;

    public HandleIncomingTransactionUseCase(IncomingTransactionRepository incomingTransactionRepository,
                                            WatchProfileRepository watchProfileRepository,
                                            MatchAttemptRepository matchAttemptRepository,
                                            TriggerEventRepository triggerEventRepository,
                                            TriggerEventSenderPort triggerEventSenderPort) {
        this.incomingTransactionRepository = incomingTransactionRepository;
        this.watchProfileRepository = watchProfileRepository;
        this.matchAttemptRepository = matchAttemptRepository;
        this.triggerEventRepository = triggerEventRepository;
        this.triggerEventSenderPort = triggerEventSenderPort;
    }

    public IncomingTransaction execute(IncomingTransactionCommand command) {
        WatchProfile profile = watchProfileRepository
                .findByPaymentTokenOrAccountID(command.paymentToken(), command.accountId())
                .orElse(null);

        IncomingTransaction tx = IncomingTransaction.receive(
                command.externalTransactionID(),
                profile != null ? profile.getWatchProfileID() : UUID.randomUUID(),
                new TransactionData(command.creditDebitIndicator(), command.mccCode(), command.merchantName(),
                        command.merchantId(), command.amount().toPlainString(), command.currency(),
                        command.bookingDateTime(), command.valueDateTime())
        );
        incomingTransactionRepository.save(tx);

        if (profile == null || profile.getWatchProfileStatus() != WatchProfileStatus.Active) {
            tx.markUnmatched();
            return incomingTransactionRepository.save(tx);
        }

        tx.markProcessing();
        incomingTransactionRepository.save(tx);

        MatchAttempt matchedAttempt = null;
        for (ActiveRule rule : profile.findActiveRules()) {
            MatchAttempt attempt = MatchAttempt.attempt(tx, rule);
            matchAttemptRepository.save(attempt);
            if (attempt.getResult() == MatchingResult.Matched) {
                matchedAttempt = attempt;
                break;
            }
        }

        if (matchedAttempt != null) {
            tx.markMatched();
            TriggerEvent triggerEvent = TriggerEvent.create(matchedAttempt, profile);
            triggerEvent.scheduleDelivery();
            triggerEventRepository.save(triggerEvent);
            triggerEventSenderPort.send(triggerEvent);
        } else {
            tx.markUnmatched();
        }

        return incomingTransactionRepository.save(tx);
    }

    public record IncomingTransactionCommand(
            String externalTransactionID,
            String accountId,
            String paymentToken,
            BigDecimal amount,
            String currency,
            CreditDebitCode creditDebitIndicator,
            String merchantName,
            String merchantId,
            String mccCode,
            Instant bookingDateTime,
            Instant valueDateTime,
            String debtorName,
            String creditorName
    ) {}
}
