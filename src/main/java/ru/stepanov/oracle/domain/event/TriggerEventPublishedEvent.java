package ru.stepanov.oracle.domain.event;

import java.time.Instant;
import java.util.UUID;

public final class TriggerEventPublishedEvent extends DomainEvent {
    public final UUID triggerEventID;
    public final UUID externalUserScenarioID;
    public final Instant occuredAt;

    public TriggerEventPublishedEvent(UUID triggerEventID, UUID externalUserScenarioID, Instant occuredAt) {
        super(UUID.randomUUID(), occuredAt);
        this.triggerEventID = triggerEventID;
        this.externalUserScenarioID = externalUserScenarioID;
        this.occuredAt = occuredAt;
    }
}
