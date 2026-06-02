package ru.stepanov.oracle.infrastructure.persistence;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.stepanov.oracle.application.repository.ProcessingErrorRepository;
import ru.stepanov.oracle.domain.model.processingerror.ProcessingError;
import ru.stepanov.oracle.infrastructure.persistence.entity.ProcessingErrorEntity;
import ru.stepanov.oracle.infrastructure.persistence.jpa.ProcessingErrorJpaRepository;

@Repository
@Profile("!test")
@Transactional
public class JpaProcessingErrorRepository implements ProcessingErrorRepository {

    private final ProcessingErrorJpaRepository jpaRepository;

    public JpaProcessingErrorRepository(ProcessingErrorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ProcessingError save(ProcessingError error) {
        ProcessingErrorEntity entity = new ProcessingErrorEntity();
        entity.setId(error.getId());
        entity.setSource(error.getSource().name());
        entity.setExternalTransactionId(error.getExternalTransactionID());
        entity.setTriggerEventId(error.getTriggerEventID());
        entity.setMessage(error.getMessage());
        entity.setOccurredAt(error.getOccurredAt());
        jpaRepository.save(entity);
        return error;
    }
}
