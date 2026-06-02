package ru.stepanov.oracle.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processing_error", schema = "oracle")
public class ProcessingErrorEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "external_transaction_id")
    private String externalTransactionId;

    @Column(name = "trigger_event_id")
    private UUID triggerEventId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getExternalTransactionId() { return externalTransactionId; }
    public void setExternalTransactionId(String externalTransactionId) { this.externalTransactionId = externalTransactionId; }
    public UUID getTriggerEventId() { return triggerEventId; }
    public void setTriggerEventId(UUID triggerEventId) { this.triggerEventId = triggerEventId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
}
