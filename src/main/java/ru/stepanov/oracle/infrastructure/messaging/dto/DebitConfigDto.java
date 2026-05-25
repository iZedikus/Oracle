package ru.stepanov.oracle.infrastructure.messaging.dto;

import java.util.UUID;

public class DebitConfigDto {
    public String debitAmount;
    public String currency;
    public String recipientPaymentToken;
    public UUID consentId;
    public UUID sourceAccountId;
}
