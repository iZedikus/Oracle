package ru.stepanov.oracle.domain.event;

import java.time.Instant;
import java.util.UUID;

public final class TriggerEventExhaustedEvent extends DomainEvent {
    public final UUID triggerEventID;
    public final UUID externalUserScenarioID;
    public final Instant occuredAt;

    public TriggerEventExhaustedEvent(UUID triggerEventID, UUID externalUserScenarioID, Instant occuredAt) {
        super(UUID.randomUUID(), occuredAt);
        this.triggerEventID = triggerEventID;
        this.externalUserScenarioID = externalUserScenarioID;
        this.occuredAt = occuredAt;
    }
}
