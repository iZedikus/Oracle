package ru.stepanov.oracle.application.repository;

import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;

import java.util.Optional;
import java.util.UUID;

public interface WatchProfileRepository {
    WatchProfile save(WatchProfile profile);
    Optional<WatchProfile> findByExternalUserScenarioID(UUID externalUserScenarioID);
    Optional<WatchProfile> findByPaymentTokenOrAccountID(String paymentToken, String accountID);
}
