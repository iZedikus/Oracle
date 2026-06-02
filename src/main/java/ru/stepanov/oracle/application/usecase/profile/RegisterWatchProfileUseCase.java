package ru.stepanov.oracle.application.usecase.profile;

import org.springframework.stereotype.Service;
import ru.stepanov.oracle.application.repository.WatchProfileRepository;
import ru.stepanov.oracle.application.usecase.profile.exception.DuplicateProfileException;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;

import java.util.List;
import java.util.UUID;

@Service
public class RegisterWatchProfileUseCase {
    private final WatchProfileRepository watchProfileRepository;

    public RegisterWatchProfileUseCase(WatchProfileRepository watchProfileRepository) {
        this.watchProfileRepository = watchProfileRepository;
    }

    public WatchProfile execute(RegisterWatchProfileCommand command) {
        watchProfileRepository.findByExternalUserScenarioID(command.externalUserScenarioID())
                .ifPresent(profile -> {
                    throw new DuplicateProfileException(command.externalUserScenarioID());
                });

        List<RuleCondition> conditions = command.rules().stream()
                .map(dto -> new RuleCondition(UUID.randomUUID(), dto.field(), dto.operator(), dto.value()))
                .toList();

        WatchProfile profile = WatchProfile.create(
                command.externalUserID(),
                command.externalUserScenarioID(),
                command.scenarioTypeCode(),
                command.paymentToken(),
                command.accountId(),
                conditions,
                command.ruleVersion(),
                command.debitConfig()
        );

        return watchProfileRepository.save(profile);
    }

    public record RegisterWatchProfileCommand(
            UUID externalUserID,
            UUID externalUserScenarioID,
            String scenarioTypeCode,
            String paymentToken,
            String accountId,
            String bankBic,
            int ruleVersion,
            List<RuleConditionDto> rules,
            DebitConfig debitConfig
    ) {}

    public record RuleConditionDto(
            ru.stepanov.oracle.domain.model.watchprofile.RuleField field,
            ru.stepanov.oracle.domain.model.watchprofile.RuleOperator operator,
            String value
    ) {}
}
