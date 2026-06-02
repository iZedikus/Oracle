package ru.stepanov.oracle.infrastructure.repository;

import org.springframework.stereotype.Repository;
import ru.stepanov.oracle.application.repository.ProcessingErrorRepository;
import ru.stepanov.oracle.domain.model.processingerror.ProcessingError;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryProcessingErrorRepository implements ProcessingErrorRepository {
    private final List<ProcessingError> errors = new CopyOnWriteArrayList<>();

    @Override
    public ProcessingError save(ProcessingError error) {
        errors.add(error);
        return error;
    }
}
