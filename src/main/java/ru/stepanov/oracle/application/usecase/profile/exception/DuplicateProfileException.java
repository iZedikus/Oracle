package ru.stepanov.oracle.application.usecase.profile.exception;

import java.util.UUID;

public class DuplicateProfileException extends RuntimeException {
    public DuplicateProfileException(UUID externalUserScenarioID) {
        super("WatchProfile already exists for externalUserScenarioID=" + externalUserScenarioID);
    }
}
