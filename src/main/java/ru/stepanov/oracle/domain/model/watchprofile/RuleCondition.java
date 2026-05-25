package ru.stepanov.oracle.domain.model.watchprofile;

import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;

import java.util.Arrays;
import java.util.UUID;

public record RuleCondition(UUID conditionID, RuleField field, RuleOperator operator, String value) {

    public boolean evaluate(IncomingTransaction transaction) {
        String actual = extractFieldValue(transaction);
        if (actual == null) {
            return false;
        }
        return switch (operator) {
            case Equals -> actual.equals(value);
            case NonEquals -> !actual.equals(value);
            case Contains -> actual.contains(value);
            case In -> Arrays.stream(value.split(","))
                    .map(String::trim)
                    .anyMatch(actual::equals);
        };
    }

    public String extractFieldValue(IncomingTransaction transaction) {
        return switch (field) {
            case MCC -> transaction.getData().mcc();
            case MerchantName -> transaction.getData().merchantName();
            case MerchantID -> transaction.getData().merchantID();
            case CreditDebitIndicator -> transaction.getData().creditDebitIndicator().name();
        };
    }
}
