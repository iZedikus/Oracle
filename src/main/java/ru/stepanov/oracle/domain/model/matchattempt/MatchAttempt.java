package ru.stepanov.oracle.domain.model.matchattempt;

import ru.stepanov.oracle.domain.event.DomainEvent;
import ru.stepanov.oracle.domain.event.TransactionMatchedEvent;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;
import ru.stepanov.oracle.domain.model.watchprofile.ActiveRule;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MatchAttempt {
    private final UUID matchAttemptID;
    private final UUID incomingTransactionID;
    private final UUID watchAccountID;
    private final MatchingResult result;
    private final List<ConditionSnapshot> snapshots;
    private final Instant attemptedAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private MatchAttempt(UUID matchAttemptID, UUID incomingTransactionID, UUID watchAccountID, MatchingResult result,
                         List<ConditionSnapshot> snapshots, Instant attemptedAt) {
        this.matchAttemptID = matchAttemptID;
        this.incomingTransactionID = incomingTransactionID;
        this.watchAccountID = watchAccountID;
        this.result = result;
        this.snapshots = snapshots;
        this.attemptedAt = attemptedAt;
    }

    public static MatchAttempt attempt(IncomingTransaction transaction, ActiveRule rule) {
        List<ConditionSnapshot> snapshots = new ArrayList<>();
        boolean allPassed = true;
        for (RuleCondition condition : rule.getConditions()) {
            boolean passed = condition.evaluate(transaction);
            snapshots.add(new ConditionSnapshot(condition.value(), condition.extractFieldValue(transaction), passed));
            allPassed = allPassed && passed;
        }
        MatchingResult result = allPassed ? MatchingResult.Matched : MatchingResult.Unmatched;
        MatchAttempt attempt = new MatchAttempt(UUID.randomUUID(), transaction.getIncomingTransactionID(),
                transaction.getWatchProfileID(), result, snapshots, Instant.now());
        if (result == MatchingResult.Matched) {
            attempt.domainEvents.add(new TransactionMatchedEvent(attempt.matchAttemptID, transaction.getWatchProfileID(),
                    transaction.getIncomingTransactionID(), rule.getActiveRuleID(), Instant.now()));
        }
        return attempt;
    }

    public UUID getMatchAttemptID() { return matchAttemptID; }
    public UUID getIncomingTransactionID() { return incomingTransactionID; }
    public UUID getWatchAccountID() { return watchAccountID; }
    public MatchingResult getResult() { return result; }
    public List<ConditionSnapshot> getSnapshots() { return List.copyOf(snapshots); }
    public Instant getAttemptedAt() { return attemptedAt; }
    public List<DomainEvent> pullDomainEvents() { var copy = List.copyOf(domainEvents); domainEvents.clear(); return copy; }
}
