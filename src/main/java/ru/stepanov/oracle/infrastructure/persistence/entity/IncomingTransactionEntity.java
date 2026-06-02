package ru.stepanov.oracle.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "incoming_transaction", schema = "oracle")
public class IncomingTransactionEntity {

    @Id
    @Column(name = "incoming_transaction_id")
    private UUID incomingTransactionId;

    @Column(name = "external_transaction_id", nullable = false, unique = true)
    private String externalTransactionId;

    @Column(name = "watch_profile_id")
    private UUID watchProfileId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "credit_debit_indicator")
    private String creditDebitIndicator;

    @Column(name = "mcc")
    private String mcc;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "amount")
    private String amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "booking_date_time")
    private Instant bookingDateTime;

    @Column(name = "value_date_time")
    private Instant valueDateTime;

    public UUID getIncomingTransactionId() { return incomingTransactionId; }
    public void setIncomingTransactionId(UUID incomingTransactionId) { this.incomingTransactionId = incomingTransactionId; }
    public String getExternalTransactionId() { return externalTransactionId; }
    public void setExternalTransactionId(String externalTransactionId) { this.externalTransactionId = externalTransactionId; }
    public UUID getWatchProfileId() { return watchProfileId; }
    public void setWatchProfileId(UUID watchProfileId) { this.watchProfileId = watchProfileId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Instant receivedAt) { this.receivedAt = receivedAt; }
    public Instant getProcessedAt() { return processedAt; }
    public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getCreditDebitIndicator() { return creditDebitIndicator; }
    public void setCreditDebitIndicator(String creditDebitIndicator) { this.creditDebitIndicator = creditDebitIndicator; }
    public String getMcc() { return mcc; }
    public void setMcc(String mcc) { this.mcc = mcc; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Instant getBookingDateTime() { return bookingDateTime; }
    public void setBookingDateTime(Instant bookingDateTime) { this.bookingDateTime = bookingDateTime; }
    public Instant getValueDateTime() { return valueDateTime; }
    public void setValueDateTime(Instant valueDateTime) { this.valueDateTime = valueDateTime; }
}
