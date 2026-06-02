package ru.stepanov.oracle.application.repository;

import ru.stepanov.oracle.domain.model.processingerror.ProcessingError;

public interface ProcessingErrorRepository {
    ProcessingError save(ProcessingError error);
}
