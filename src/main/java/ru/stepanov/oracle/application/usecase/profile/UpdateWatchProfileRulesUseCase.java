package ru.stepanov.oracle.application.usecase.profile;

import org.springframework.stereotype.Service;
import ru.stepanov.oracle.application.repository.WatchProfileRepository;
import ru.stepanov.oracle.application.usecase.profile.exception.WatchProfileNotFoundException;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.watchprofile.ActiveRule;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;

import java.util.List;
import java.util.UUID;

@Service
public class UpdateWatchProfileRulesUseCase {
    private final WatchProfileRepository watchProfileRepository;

    public UpdateWatchProfileRulesUseCase(WatchProfileRepository watchProfileRepository) {
        this.watchProfileRepository = watchProfileRepository;
    }

    public WatchProfile execute(UpdateRulesCommand command) {
        WatchProfile profile = watchProfileRepository.findByExternalUserScenarioID(command.externalUserScenarioID())
                .orElseThrow(() -> new WatchProfileNotFoundException(command.externalUserScenarioID()));

        ActiveRule activeRule = profile.findActiveRules().stream()
                .filter(rule -> rule.getExternalUserScenarioID().equals(command.externalUserScenarioID()))
                .findFirst()
                .orElseThrow(() -> new WatchProfileNotFoundException(command.externalUserScenarioID()));

        List<RuleCondition> conditions = command.rules().stream()
                .map(dto -> new RuleCondition(UUID.randomUUID(), dto.field(), dto.operator(), dto.value()))
                .toList();
        activeRule.updateConditions(conditions, command.ruleVersion());
        profile.updateRule(activeRule);
        profile.updateDebitConfig(command.debitConfig());

        return watchProfileRepository.save(profile);
    }

    public record UpdateRulesCommand(
            UUID externalUserScenarioID,
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
