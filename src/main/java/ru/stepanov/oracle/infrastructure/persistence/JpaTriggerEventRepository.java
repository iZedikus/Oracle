package ru.stepanov.oracle.infrastructure.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.stepanov.oracle.application.repository.TriggerEventRepository;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.infrastructure.persistence.jpa.TriggerEventJpaRepository;
import ru.stepanov.oracle.infrastructure.persistence.mapper.PersistenceMapper;

import java.util.List;

@Repository
@Profile("!test")
@Transactional
public class JpaTriggerEventRepository implements TriggerEventRepository {

    private final TriggerEventJpaRepository jpaRepository;
    private final PersistenceMapper mapper;

    public JpaTriggerEventRepository(TriggerEventJpaRepository jpaRepository, PersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TriggerEvent save(TriggerEvent triggerEvent) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(triggerEvent)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TriggerEvent> findPendingForRetry() {
        return jpaRepository.findPendingForRetry().stream().map(mapper::toDomain).toList();
    }
}
