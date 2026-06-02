package ru.stepanov.oracle.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.stepanov.oracle.infrastructure.persistence.entity.WatchProfileEntity;

import java.util.Optional;
import java.util.UUID;

public interface WatchProfileJpaRepository extends JpaRepository<WatchProfileEntity, UUID> {

    @Query("""
            SELECT wp FROM WatchProfileEntity wp
            JOIN FETCH wp.rules r
            LEFT JOIN FETCH r.conditions
            WHERE r.externalUserScenarioId = :scenarioId
            """)
    Optional<WatchProfileEntity> findByExternalUserScenarioId(@Param("scenarioId") UUID scenarioId);

    @Query("""
            SELECT DISTINCT wp FROM WatchProfileEntity wp
            LEFT JOIN FETCH wp.rules r
            LEFT JOIN FETCH r.conditions
            WHERE (:paymentToken IS NOT NULL AND wp.paymentToken = :paymentToken)
               OR (:accountId IS NOT NULL AND wp.accountId = :accountId)
            """)
    Optional<WatchProfileEntity> findByPaymentTokenOrAccountId(@Param("paymentToken") String paymentToken,
                                                               @Param("accountId") String accountId);
}
