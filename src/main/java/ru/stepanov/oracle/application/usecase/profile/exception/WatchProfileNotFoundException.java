package ru.stepanov.oracle.application.usecase.profile.exception;

import java.util.UUID;

public class WatchProfileNotFoundException extends RuntimeException {
    public WatchProfileNotFoundException(UUID externalUserScenarioID) {
        super("WatchProfile not found for externalUserScenarioID=" + externalUserScenarioID);
    }
}
