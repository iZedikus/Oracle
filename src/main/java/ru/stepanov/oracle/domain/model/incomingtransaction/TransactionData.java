package ru.stepanov.oracle.domain.model.incomingtransaction;

import java.time.Instant;

public record TransactionData(
        CreditDebitCode creditDebitIndicator,
        String mcc,
        String merchantName,
        String merchantID,
        String amount,
        String currency,
        Instant bookingDateTime,
        Instant valueDateTime
) {
}
