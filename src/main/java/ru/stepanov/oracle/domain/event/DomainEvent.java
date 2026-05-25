package ru.stepanov.oracle.domain.event;

import java.time.Instant;
import java.util.UUID;

public sealed abstract class DomainEvent permits TransactionMatchedEvent, TriggerEventDeliveredEvent, TriggerEventExhaustedEvent, WatchProfileRegisteredEvent {
    private final UUID eventID;
    private final Instant occurredAt;

    protected DomainEvent(UUID eventID, Instant occurredAt) {
        this.eventID = eventID;
        this.occurredAt = occurredAt;
    }

    public UUID getEventID() { return eventID; }
    public Instant getOccurredAt() { return occurredAt; }
}
