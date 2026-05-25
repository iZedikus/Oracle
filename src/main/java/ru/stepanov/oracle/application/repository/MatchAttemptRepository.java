package ru.stepanov.oracle.application.repository;

import ru.stepanov.oracle.domain.model.matchattempt.MatchAttempt;

public interface MatchAttemptRepository {
    MatchAttempt save(MatchAttempt attempt);
}
