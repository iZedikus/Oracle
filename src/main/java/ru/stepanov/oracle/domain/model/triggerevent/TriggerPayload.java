package ru.stepanov.oracle.domain.model.triggerevent;

import java.time.Instant;

public record TriggerPayload(String triggerTransactionID,
                             String matchedMCC,
                             String matchedMerchantName,
                             String matchedAmount,
                             String matchedCurrency,
                             String scenarioTypeCode,
                             Instant occuredAt) {
}
