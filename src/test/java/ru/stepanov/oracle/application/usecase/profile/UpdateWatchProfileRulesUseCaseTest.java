package ru.stepanov.oracle.application.usecase.profile;

import org.junit.jupiter.api.Test;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;
import ru.stepanov.oracle.domain.model.watchprofile.RuleField;
import ru.stepanov.oracle.domain.model.watchprofile.RuleOperator;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;
import ru.stepanov.oracle.infrastructure.repository.InMemoryWatchProfileRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateWatchProfileRulesUseCaseTest {

    @Test
    void execute_updatesDebitConfigFromCommand() {
        UUID externalUserId = UUID.randomUUID();
        UUID externalUserScenarioId = UUID.randomUUID();
        DebitConfig initialDebitConfig = new DebitConfig("100.00", "RUB", "token-old", UUID.randomUUID(), UUID.randomUUID());
        DebitConfig updatedDebitConfig = new DebitConfig("250.00", "USD", "token-new", UUID.randomUUID(), UUID.randomUUID());

        WatchProfile profile = WatchProfile.create(
                externalUserId,
                externalUserScenarioId,
                "UNDESIRABLE_PURCHASE",
                "token-test",
                null,
                List.of(new RuleCondition(UUID.randomUUID(), RuleField.MCC, RuleOperator.In, "5912")),
                1,
                initialDebitConfig
        );

        InMemoryWatchProfileRepository repository = new InMemoryWatchProfileRepository();
        repository.save(profile);

        UpdateWatchProfileRulesUseCase useCase = new UpdateWatchProfileRulesUseCase(repository);
        useCase.execute(new UpdateWatchProfileRulesUseCase.UpdateRulesCommand(
                externalUserScenarioId,
                2,
                List.of(new UpdateWatchProfileRulesUseCase.RuleConditionDto(
                        RuleField.MCC, RuleOperator.In, "5813")),
                updatedDebitConfig
        ));

        WatchProfile saved = repository.findByExternalUserScenarioID(externalUserScenarioId).orElseThrow();
        assertEquals(updatedDebitConfig, saved.getDebitConfig());
        assertEquals("250.00", saved.getDebitConfig().debitAmount());
        assertEquals("USD", saved.getDebitConfig().currency());
        assertEquals("token-new", saved.getDebitConfig().recipientPaymentToken());
    }
}
