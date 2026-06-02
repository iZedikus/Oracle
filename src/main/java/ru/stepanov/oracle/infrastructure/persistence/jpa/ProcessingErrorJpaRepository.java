package ru.stepanov.oracle.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepanov.oracle.infrastructure.persistence.entity.ProcessingErrorEntity;

import java.util.UUID;

public interface ProcessingErrorJpaRepository extends JpaRepository<ProcessingErrorEntity, UUID> {
}
