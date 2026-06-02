package ru.stepanov.oracle.domain.model.triggerevent;

import java.util.UUID;

public record DebitConfig(
        String debitAmount,
        String currency,
        String recipientPaymentToken,
        UUID consentId,
        UUID sourceAccountId
) {
}
