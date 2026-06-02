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
                .filter(profile -> matchesPaymentTokenOrAccountId(profile, paymentToken, accountID))
                .findFirst();
    }

    private static boolean matchesPaymentTokenOrAccountId(WatchProfile profile, String paymentToken, String accountID) {
        return paymentToken != null && paymentToken.equals(profile.getPaymentToken())
                || accountID != null && accountID.equals(profile.getAccountId());
    }
}
