package ru.stepanov.oracle.infrastructure.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.stepanov.oracle.application.repository.MatchAttemptRepository;
import ru.stepanov.oracle.domain.model.matchattempt.MatchAttempt;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("test")
public class InMemoryMatchAttemptRepository implements MatchAttemptRepository {
    private final Map<UUID, MatchAttempt> attempts = new ConcurrentHashMap<>();

    @Override
    public MatchAttempt save(MatchAttempt attempt) {
        attempts.put(attempt.getMatchAttemptID(), attempt);
        return attempt;
    }
}
