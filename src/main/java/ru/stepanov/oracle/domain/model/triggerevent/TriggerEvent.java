package ru.stepanov.oracle.domain.model.triggerevent;

import ru.stepanov.oracle.domain.event.DomainEvent;
import ru.stepanov.oracle.domain.event.TriggerEventDeliveredEvent;
import ru.stepanov.oracle.domain.event.TriggerEventExhaustedEvent;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;
import ru.stepanov.oracle.domain.model.incomingtransaction.TransactionData;
import ru.stepanov.oracle.domain.model.matchattempt.MatchAttempt;
import ru.stepanov.oracle.domain.model.watchprofile.ActiveRule;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TriggerEvent {
    private static final int MAX_RETRIES = 3;
    private final UUID triggerEventID;
    private final UUID matchAttemptID;
    private final UUID externalUserScenarioID;
    private final UUID externalUserID;
    private TriggerEventDeliveryStatus deliveryStatus;
    private final TriggerPayload payload;
    private final List<DeliveryAttempt> deliveryAttempts;
    private int retryCount;
    private Instant scheduledAt;
    private Instant deliveredAt;
    private Instant lastRetryAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private TriggerEvent(UUID triggerEventID, UUID matchAttemptID, UUID externalUserScenarioID, UUID externalUserID,
                         TriggerEventDeliveryStatus deliveryStatus, TriggerPayload payload, List<DeliveryAttempt> deliveryAttempts,
                         int retryCount, Instant scheduledAt, Instant deliveredAt, Instant lastRetryAt) {
        this.triggerEventID = triggerEventID;
        this.matchAttemptID = matchAttemptID;
        this.externalUserScenarioID = externalUserScenarioID;
        this.externalUserID = externalUserID;
        this.deliveryStatus = deliveryStatus;
        this.payload = payload;
        this.deliveryAttempts = deliveryAttempts;
        this.retryCount = retryCount;
        this.scheduledAt = scheduledAt;
        this.deliveredAt = deliveredAt;
        this.lastRetryAt = lastRetryAt;
    }

    public static TriggerEvent restore(UUID triggerEventID,
                                       UUID matchAttemptID,
                                       UUID externalUserScenarioID,
                                       UUID externalUserID,
                                       TriggerEventDeliveryStatus deliveryStatus,
                                       TriggerPayload payload,
                                       List<DeliveryAttempt> deliveryAttempts,
                                       int retryCount,
                                       Instant scheduledAt,
                                       Instant deliveredAt,
                                       Instant lastRetryAt) {
        return new TriggerEvent(
                triggerEventID, matchAttemptID, externalUserScenarioID, externalUserID,
                deliveryStatus, payload, new ArrayList<>(deliveryAttempts),
                retryCount, scheduledAt, deliveredAt, lastRetryAt);
    }

    public static TriggerEvent create(MatchAttempt matchAttempt,
                                      WatchProfile watchProfile,
                                      ActiveRule matchedRule,
                                      IncomingTransaction transaction) {
        TransactionData data = transaction.getData();
        Instant occurredAt = Instant.now();
        TriggerPayload payload = new TriggerPayload(
                transaction.getExternalTransactionID(),
                data.mcc(),
                data.merchantName(),
                data.amount(),
                data.currency(),
                matchedRule.getScenarioTypeCode(),
                watchProfile.getDebitConfig(),
                occurredAt);
        return new TriggerEvent(
                UUID.randomUUID(),
                matchAttempt.getMatchAttemptID(),
                matchedRule.getExternalUserScenarioID(),
                watchProfile.getExternalUserID(),
                TriggerEventDeliveryStatus.Pending,
                payload,
                new ArrayList<>(),
                0,
                occurredAt,
                null,
                null);
    }

    public UUID getTriggerEventID() { return triggerEventID; }
    public UUID getMatchAttemptID() { return matchAttemptID; }
    public UUID getExternalUserScenarioID() { return externalUserScenarioID; }
    public UUID getExternalUserID() { return externalUserID; }
    public TriggerEventDeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public TriggerPayload getPayload() { return payload; }
    public String getTriggerTransactionId() { return payload.triggerTransactionID(); }
    public String getMatchedMcc() { return payload.matchedMCC(); }
    public String getMatchedMerchantName() { return payload.matchedMerchantName(); }
    public String getMatchedAmount() { return payload.matchedAmount(); }
    public String getMatchedCurrency() { return payload.matchedCurrency(); }
    public String getScenarioTypeCode() { return payload.scenarioTypeCode(); }
    public DebitConfig getDebitConfig() { return payload.debitConfig(); }
    public Instant getOccurredAt() { return payload.occuredAt(); }
    public int getRetryCount() { return retryCount; }
    public Instant getScheduledAt() { return scheduledAt; }
    public Instant getDeliveredAt() { return deliveredAt; }
    public Instant getLastRetryAt() { return lastRetryAt; }
    public List<DeliveryAttempt> getDeliveryAttempts() { return List.copyOf(deliveryAttempts); }

    public void scheduleDelivery() { this.deliveryStatus = TriggerEventDeliveryStatus.Pending; this.scheduledAt = Instant.now(); }
    public void markDelivered() { this.deliveryStatus = TriggerEventDeliveryStatus.Delivered; this.deliveredAt = Instant.now(); domainEvents.add(new TriggerEventDeliveredEvent(triggerEventID, externalUserScenarioID, deliveredAt)); }
    public void markFailed(String message) { this.deliveryStatus = TriggerEventDeliveryStatus.Failed; deliveryAttempts.add(new DeliveryAttempt(retryCount + 1, Instant.now(), false, 500, message)); }
    public void scheduleRetry() { this.retryCount++; this.lastRetryAt = Instant.now(); this.deliveryStatus = TriggerEventDeliveryStatus.Retrying; }
    public boolean isExhausted() {
        boolean exhausted = retryCount >= MAX_RETRIES;
        if (exhausted) {
            domainEvents.add(new TriggerEventExhaustedEvent(triggerEventID, externalUserScenarioID, Instant.now()));
        }
        return exhausted;
    }

    public List<DomainEvent> pullDomainEvents() { var copy = List.copyOf(domainEvents); domainEvents.clear(); return copy; }
}
