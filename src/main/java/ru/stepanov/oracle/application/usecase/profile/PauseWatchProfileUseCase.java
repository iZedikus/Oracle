package ru.stepanov.oracle.application.usecase.profile;

import org.springframework.stereotype.Service;
import ru.stepanov.oracle.application.repository.WatchProfileRepository;
import ru.stepanov.oracle.application.usecase.profile.exception.WatchProfileNotFoundException;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;

import java.util.UUID;

@Service
public class PauseWatchProfileUseCase {
    private final WatchProfileRepository watchProfileRepository;

    public PauseWatchProfileUseCase(WatchProfileRepository watchProfileRepository) {
        this.watchProfileRepository = watchProfileRepository;
    }

    public WatchProfile execute(UUID externalUserScenarioID) {
        WatchProfile profile = watchProfileRepository.findByExternalUserScenarioID(externalUserScenarioID)
                .orElseThrow(() -> new WatchProfileNotFoundException(externalUserScenarioID));
        profile.pause();
        return watchProfileRepository.save(profile);
    }
}
