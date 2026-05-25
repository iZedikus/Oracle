package ru.stepanov.oracle.infrastructure.repository;

import org.springframework.stereotype.Repository;
import ru.stepanov.oracle.application.repository.IncomingTransactionRepository;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryIncomingTransactionRepository implements IncomingTransactionRepository {
    private final Map<UUID, IncomingTransaction> transactions = new ConcurrentHashMap<>();

    @Override
    public IncomingTransaction save(IncomingTransaction transaction) {
        transactions.put(transaction.getIncomingTransactionID(), transaction);
        return transaction;
    }
}
