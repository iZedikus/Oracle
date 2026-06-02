package ru.stepanov.oracle.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stepanov.oracle.infrastructure.persistence.entity.TriggerEventEntity;

import java.util.List;
import java.util.UUID;

public interface TriggerEventJpaRepository extends JpaRepository<TriggerEventEntity, UUID> {

    @Query("""
            SELECT te FROM TriggerEventEntity te
            LEFT JOIN FETCH te.deliveryAttempts
            WHERE te.deliveryStatus IN ('Pending', 'Retrying')
            """)
    List<TriggerEventEntity> findPendingForRetry();
}
