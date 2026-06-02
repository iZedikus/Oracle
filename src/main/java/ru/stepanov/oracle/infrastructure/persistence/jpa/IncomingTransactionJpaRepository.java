package ru.stepanov.oracle.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepanov.oracle.infrastructure.persistence.entity.IncomingTransactionEntity;

import java.util.UUID;

public interface IncomingTransactionJpaRepository extends JpaRepository<IncomingTransactionEntity, UUID> {
}
