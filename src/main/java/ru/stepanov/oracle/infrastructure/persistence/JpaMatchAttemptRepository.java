package ru.stepanov.oracle.infrastructure.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.stepanov.oracle.application.repository.MatchAttemptRepository;
import ru.stepanov.oracle.domain.model.matchattempt.MatchAttempt;
import ru.stepanov.oracle.infrastructure.persistence.jpa.MatchAttemptJpaRepository;
import ru.stepanov.oracle.infrastructure.persistence.mapper.PersistenceMapper;

@Repository
@Profile("!test")
@Transactional
public class JpaMatchAttemptRepository implements MatchAttemptRepository {

    private final MatchAttemptJpaRepository jpaRepository;
    private final PersistenceMapper mapper;

    public JpaMatchAttemptRepository(MatchAttemptJpaRepository jpaRepository, PersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public MatchAttempt save(MatchAttempt matchAttempt) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(matchAttempt)));
    }
}
