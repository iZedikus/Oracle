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
@Table(name = "watch_profile", schema = "oracle")
public class WatchProfileEntity {

    @Id
    @Column(name = "watch_profile_id")
    private UUID watchProfileId;

    @Column(name = "external_user_id", nullable = false)
    private UUID externalUserId;

    @Column(name = "payment_token")
    private String paymentToken;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "watch_profile_status", nullable = false)
    private String watchProfileStatus;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "last_event_at")
    private Instant lastEventAt;

    @Column(name = "debit_amount")
    private String debitAmount;

    @Column(name = "debit_currency", length = 3)
    private String debitCurrency;

    @Column(name = "recipient_payment_token")
    private String recipientPaymentToken;

    @Column(name = "consent_id")
    private UUID consentId;

    @Column(name = "source_account_id")
    private UUID sourceAccountId;

    @OneToMany(mappedBy = "watchProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActiveRuleEntity> rules = new ArrayList<>();

    public UUID getWatchProfileId() { return watchProfileId; }
    public void setWatchProfileId(UUID watchProfileId) { this.watchProfileId = watchProfileId; }
    public UUID getExternalUserId() { return externalUserId; }
    public void setExternalUserId(UUID externalUserId) { this.externalUserId = externalUserId; }
    public String getPaymentToken() { return paymentToken; }
    public void setPaymentToken(String paymentToken) { this.paymentToken = paymentToken; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getWatchProfileStatus() { return watchProfileStatus; }
    public void setWatchProfileStatus(String watchProfileStatus) { this.watchProfileStatus = watchProfileStatus; }
    public Instant getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Instant registeredAt) { this.registeredAt = registeredAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getLastEventAt() { return lastEventAt; }
    public void setLastEventAt(Instant lastEventAt) { this.lastEventAt = lastEventAt; }
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
    public List<ActiveRuleEntity> getRules() { return rules; }
    public void setRules(List<ActiveRuleEntity> rules) { this.rules = rules; }
}
