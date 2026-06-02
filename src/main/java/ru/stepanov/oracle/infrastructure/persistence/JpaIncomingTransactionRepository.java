package ru.stepanov.oracle.infrastructure.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.stepanov.oracle.application.repository.IncomingTransactionRepository;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;
import ru.stepanov.oracle.infrastructure.persistence.jpa.IncomingTransactionJpaRepository;
import ru.stepanov.oracle.infrastructure.persistence.mapper.PersistenceMapper;

@Repository
@Profile("!test")
@Transactional
public class JpaIncomingTransactionRepository implements IncomingTransactionRepository {

    private final IncomingTransactionJpaRepository jpaRepository;
    private final PersistenceMapper mapper;

    public JpaIncomingTransactionRepository(IncomingTransactionJpaRepository jpaRepository, PersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public IncomingTransaction save(IncomingTransaction transaction) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(transaction)));
    }
}
