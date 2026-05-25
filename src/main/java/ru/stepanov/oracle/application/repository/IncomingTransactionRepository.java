package ru.stepanov.oracle.application.repository;

import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;

public interface IncomingTransactionRepository {
    IncomingTransaction save(IncomingTransaction transaction);
}
