package ru.stepanov.oracle.domain.model.processingerror;

import java.time.Instant;
import java.util.UUID;

public class ProcessingError {
    private final UUID id;
    private final ProcessingErrorSource source;
    private final String externalTransactionID;
    private final UUID triggerEventID;
    private final String message;
    private final Instant occurredAt;

    private ProcessingError(UUID id,
                            ProcessingErrorSource source,
                            String externalTransactionID,
                            UUID triggerEventID,
                            String message,
                            Instant occurredAt) {
        this.id = id;
        this.source = source;
        this.externalTransactionID = externalTransactionID;
        this.triggerEventID = triggerEventID;
        this.message = message;
        this.occurredAt = occurredAt;
    }

    public static ProcessingError record(ProcessingErrorSource source,
                                         String externalTransactionID,
                                         UUID triggerEventID,
                                         String message) {
        return new ProcessingError(
                UUID.randomUUID(),
                source,
                externalTransactionID,
                triggerEventID,
                message,
                Instant.now()
        );
    }

    public UUID getId() { return id; }
    public ProcessingErrorSource getSource() { return source; }
    public String getExternalTransactionID() { return externalTransactionID; }
    public UUID getTriggerEventID() { return triggerEventID; }
    public String getMessage() { return message; }
    public Instant getOccurredAt() { return occurredAt; }
}
