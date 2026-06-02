package ru.stepanov.oracle.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stepanov.oracle.infrastructure.persistence.entity.MatchAttemptEntity;

import java.util.UUID;

public interface MatchAttemptJpaRepository extends JpaRepository<MatchAttemptEntity, UUID> {
}
