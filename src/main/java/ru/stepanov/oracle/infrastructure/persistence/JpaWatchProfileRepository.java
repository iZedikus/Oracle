package ru.stepanov.oracle.infrastructure.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.stepanov.oracle.application.repository.WatchProfileRepository;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;
import ru.stepanov.oracle.infrastructure.persistence.jpa.WatchProfileJpaRepository;
import ru.stepanov.oracle.infrastructure.persistence.mapper.PersistenceMapper;

import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("!test")
@Transactional
public class JpaWatchProfileRepository implements WatchProfileRepository {

    private final WatchProfileJpaRepository jpaRepository;
    private final PersistenceMapper mapper;

    public JpaWatchProfileRepository(WatchProfileJpaRepository jpaRepository, PersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public WatchProfile save(WatchProfile profile) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(profile)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WatchProfile> findByExternalUserScenarioID(UUID externalUserScenarioID) {
        return jpaRepository.findByExternalUserScenarioId(externalUserScenarioID).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WatchProfile> findByPaymentTokenOrAccountID(String paymentToken, String accountID) {
        return jpaRepository.findByPaymentTokenOrAccountId(paymentToken, accountID).map(mapper::toDomain);
    }
}
