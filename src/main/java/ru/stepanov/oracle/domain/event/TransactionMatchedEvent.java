package ru.stepanov.oracle.domain.event;

import java.time.Instant;
import java.util.UUID;

public final class TransactionMatchedEvent extends DomainEvent {
    private final UUID matchAttemptID;
    private final UUID watchProfileID;
    private final UUID incomingTransactionID;
    private final UUID matchedRuleID;
    private final Instant occuredAt;

    public TransactionMatchedEvent(UUID matchAttemptID, UUID watchProfileID, UUID incomingTransactionID, UUID matchedRuleID, Instant occuredAt) {
        super(UUID.randomUUID(), occuredAt);
        this.matchAttemptID = matchAttemptID;
        this.watchProfileID = watchProfileID;
        this.incomingTransactionID = incomingTransactionID;
        this.matchedRuleID = matchedRuleID;
        this.occuredAt = occuredAt;
    }
    public UUID getMatchAttemptID() { return matchAttemptID; }
    public UUID getWatchProfileID() { return watchProfileID; }
    public UUID getIncomingTransactionID() { return incomingTransactionID; }
    public UUID getMatchedRuleID() { return matchedRuleID; }
    public Instant getOccuredAt() { return occuredAt; }
}
