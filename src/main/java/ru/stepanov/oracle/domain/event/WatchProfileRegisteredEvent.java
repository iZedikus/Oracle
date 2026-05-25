package ru.stepanov.oracle.domain.event;

import java.time.Instant;
import java.util.UUID;

public final class WatchProfileRegisteredEvent extends DomainEvent {
    private final UUID watchProfileID;
    private final UUID externalUserID;

    public WatchProfileRegisteredEvent(UUID watchProfileID, UUID externalUserID, Instant occurredAt) {
        super(UUID.randomUUID(), occurredAt);
        this.watchProfileID = watchProfileID;
        this.externalUserID = externalUserID;
    }

    public UUID getWatchProfileID() { return watchProfileID; }
    public UUID getExternalUserID() { return externalUserID; }
}
