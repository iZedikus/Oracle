package ru.stepanov.oracle.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trigger_event", schema = "oracle")
public class TriggerEventEntity {

    @Id
    @Column(name = "trigger_event_id")
    private UUID triggerEventId;

    @Column(name = "match_attempt_id", nullable = false)
    private UUID matchAttemptId;

    @Column(name = "external_user_scenario_id", nullable = false)
    private UUID externalUserScenarioId;

    @Column(name = "external_user_id", nullable = false)
    private UUID externalUserId;

    @Column(name = "delivery_status", nullable = false)
    private String deliveryStatus;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "last_retry_at")
    private Instant lastRetryAt;

    @Column(name = "trigger_transaction_id")
    private String triggerTransactionId;

    @Column(name = "matched_mcc")
    private String matchedMcc;

    @Column(name = "matched_merchant_name")
    private String matchedMerchantName;

    @Column(name = "matched_amount")
    private String matchedAmount;

    @Column(name = "matched_currency")
    private String matchedCurrency;

    @Column(name = "scenario_type_code")
    private String scenarioTypeCode;

    @Column(name = "occured_at")
    private Instant occuredAt;

    @Column(name = "debit_amount")
    private String debitAmount;

    @Column(name = "debit_currency")
    private String debitCurrency;

    @Column(name = "recipient_payment_token")
    private String recipientPaymentToken;

    @Column(name = "consent_id")
    private UUID consentId;

    @Column(name = "source_account_id")
    private UUID sourceAccountId;

    @OneToMany(mappedBy = "triggerEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryAttemptEntity> deliveryAttempts = new ArrayList<>();

    public UUID getTriggerEventId() { return triggerEventId; }
    public void setTriggerEventId(UUID triggerEventId) { this.triggerEventId = triggerEventId; }
    public UUID getMatchAttemptId() { return matchAttemptId; }
    public void setMatchAttemptId(UUID matchAttemptId) { this.matchAttemptId = matchAttemptId; }
    public UUID getExternalUserScenarioId() { return externalUserScenarioId; }
    public void setExternalUserScenarioId(UUID externalUserScenarioId) { this.externalUserScenarioId = externalUserScenarioId; }
    public UUID getExternalUserId() { return externalUserId; }
    public void setExternalUserId(UUID externalUserId) { this.externalUserId = externalUserId; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public Instant getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Instant scheduledAt) { this.scheduledAt = scheduledAt; }
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }
    public Instant getLastRetryAt() { return lastRetryAt; }
    public void setLastRetryAt(Instant lastRetryAt) { this.lastRetryAt = lastRetryAt; }
    public String getTriggerTransactionId() { return triggerTransactionId; }
    public void setTriggerTransactionId(String triggerTransactionId) { this.triggerTransactionId = triggerTransactionId; }
    public String getMatchedMcc() { return matchedMcc; }
    public void setMatchedMcc(String matchedMcc) { this.matchedMcc = matchedMcc; }
    public String getMatchedMerchantName() { return matchedMerchantName; }
    public void setMatchedMerchantName(String matchedMerchantName) { this.matchedMerchantName = matchedMerchantName; }
    public String getMatchedAmount() { return matchedAmount; }
    public void setMatchedAmount(String matchedAmount) { this.matchedAmount = matchedAmount; }
    public String getMatchedCurrency() { return matchedCurrency; }
    public void setMatchedCurrency(String matchedCurrency) { this.matchedCurrency = matchedCurrency; }
    public String getScenarioTypeCode() { return scenarioTypeCode; }
    public void setScenarioTypeCode(String scenarioTypeCode) { this.scenarioTypeCode = scenarioTypeCode; }
    public Instant getOccuredAt() { return occuredAt; }
    public void setOccuredAt(Instant occuredAt) { this.occuredAt = occuredAt; }
    public String getDebitAmount() { return debitAmount; }
    public void setDebitAmount(String debitAmount) { this.debitAmount = debitAmount; }
    public String getDebitCurrency() { return debitCurrency; }
    public void setDebitCurrency(String debitCurrency) { this.debitCurrency = debitCurrency; }
    public String getRecipientPaymentToken() { return recipientPaymentToken; }
    public void setRecipientPaymentToken(String recipientPaymentToken) { this.recipientPaymentToken = recipientPaymentToken; }
    public UUID getConsentId() { return consentId; }
    public void setConsentId(UUID consentId) { this.consentId = consentId; }
    public UUID getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(UUID sourceAccountId) { this.sourceAccountId = sourceAccountId; }
    public List<DeliveryAttemptEntity> getDeliveryAttempts() { return deliveryAttempts; }
    public void setDeliveryAttempts(List<DeliveryAttemptEntity> deliveryAttempts) { this.deliveryAttempts = deliveryAttempts; }
}
