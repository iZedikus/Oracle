package ru.stepanov.oracle.domain.model.incomingtransaction;

import java.time.Instant;
import java.util.UUID;

public class IncomingTransaction {
    private final UUID incomingTransactionID;
    private final String externalTransactionID;
    private final UUID watchProfileID;
    private IncomingTransactionStatus status;
    private final TransactionData data;
    private final Instant receivedAt;
    private Instant processedAt;
    private String errorMessage;

    private IncomingTransaction(UUID incomingTransactionID,
                                String externalTransactionID,
                                UUID watchProfileID,
                                IncomingTransactionStatus status,
                                TransactionData data,
                                Instant receivedAt) {
        this.incomingTransactionID = incomingTransactionID;
        this.externalTransactionID = externalTransactionID;
        this.watchProfileID = watchProfileID;
        this.status = status;
        this.data = data;
        this.receivedAt = receivedAt;
    }

    public static IncomingTransaction receive(String externalTransactionID, UUID watchProfileID, TransactionData data) {
        return new IncomingTransaction(UUID.randomUUID(), externalTransactionID, watchProfileID,
                IncomingTransactionStatus.Received, data, Instant.now());
    }

    public static IncomingTransaction restore(UUID incomingTransactionID,
                                              String externalTransactionID,
                                              UUID watchProfileID,
                                              IncomingTransactionStatus status,
                                              TransactionData data,
                                              Instant receivedAt,
                                              Instant processedAt,
                                              String errorMessage) {
        IncomingTransaction transaction = new IncomingTransaction(
                incomingTransactionID, externalTransactionID, watchProfileID, status, data, receivedAt);
        transaction.processedAt = processedAt;
        transaction.errorMessage = errorMessage;
        return transaction;
    }

    public void markProcessing() { this.status = IncomingTransactionStatus.Processing; this.processedAt = Instant.now(); }
    public void markMatched() { this.status = IncomingTransactionStatus.Matched; this.processedAt = Instant.now(); }
    public void markUnmatched() { this.status = IncomingTransactionStatus.Unmatched; this.processedAt = Instant.now(); }
    public void markError(String message) { this.status = IncomingTransactionStatus.Error; this.errorMessage = message; this.processedAt = Instant.now(); }

    public UUID getIncomingTransactionID() { return incomingTransactionID; }
    public String getExternalTransactionID() { return externalTransactionID; }
    public UUID getWatchProfileID() { return watchProfileID; }
    public IncomingTransactionStatus getStatus() { return status; }
    public TransactionData getData() { return data; }
    public Instant getReceivedAt() { return receivedAt; }
    public Instant getProcessedAt() { return processedAt; }
    public String getErrorMessage() { return errorMessage; }
}
