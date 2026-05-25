package ru.stepanov.oracle.domain.model.incomingtransaction;

public enum IncomingTransactionStatus {
    Received,
    Processing,
    Matched,
    Unmatched,
    Error
}
