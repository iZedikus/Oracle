package ru.stepanov.oracle.infrastructure.repository;

import org.springframework.stereotype.Repository;
import ru.stepanov.oracle.application.repository.WatchProfileRepository;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryWatchProfileRepository implements WatchProfileRepository {
    private final Map<UUID, WatchProfile> profilesByScenarioID = new ConcurrentHashMap<>();

    @Override
    public WatchProfile save(WatchProfile profile) {
        UUID scenarioID = profile.getRules().stream()
                .findFirst()
                .map(rule -> rule.getExternalUserScenarioID())
                .orElseThrow(() -> new IllegalStateException("Watch profile has no rules and cannot be indexed by scenario ID"));

        profilesByScenarioID.put(scenarioID, profile);
        return profile;
    }

    @Override
    public Optional<WatchProfile> findByExternalUserScenarioID(UUID externalUserScenarioID) {
        return Optional.ofNullable(profilesByScenarioID.get(externalUserScenarioID));
    }

    @Override
    public Optional<WatchProfile> findByPaymentTokenOrAccountID(String paymentToken, String accountID) {
        return profilesByScenarioID.values().stream()
                .filter(profile -> profile.getRules().stream().anyMatch(rule ->
                        rule.getConditions().stream().anyMatch(condition ->
                                paymentToken != null && paymentToken.equals(condition.value())
                                        || accountID != null && accountID.equals(condition.value())
                        )
                ))
                .findFirst();
    }
}
