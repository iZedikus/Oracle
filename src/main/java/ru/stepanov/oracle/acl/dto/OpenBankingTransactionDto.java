package ru.stepanov.oracle.acl.dto;

import java.util.UUID;

public class OpenBankingTransactionDto {
    public UUID messageId;
    public String occurredAt;
    public String transactionId;
    public String accountId;
    public String paymentToken;
    public String amount;
    public String currency;
    public String creditDebitIndicator;
    public String bookingDateTime;
    public String valueDateTime;
    public String merchantName;
    public String merchantId;
    public int mccCode;
    public String debtorName;
    public String creditorName;
}
