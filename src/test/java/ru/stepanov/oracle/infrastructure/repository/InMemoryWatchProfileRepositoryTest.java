package ru.stepanov.oracle.infrastructure.repository;

import org.junit.jupiter.api.Test;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;
import ru.stepanov.oracle.domain.model.watchprofile.RuleField;
import ru.stepanov.oracle.domain.model.watchprofile.RuleOperator;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryWatchProfileRepositoryTest {

    @Test
    void findByPaymentTokenOrAccountID_matchesProfilePaymentToken_notRuleCondition() {
        InMemoryWatchProfileRepository repository = new InMemoryWatchProfileRepository();
        UUID scenarioId = UUID.randomUUID();

        WatchProfile profile = WatchProfile.create(
                UUID.randomUUID(),
                scenarioId,
                "UNDESIRABLE_PURCHASE",
                "token-abc-xyz",
                "ACC-TEST-001",
                List.of(new RuleCondition(UUID.randomUUID(), RuleField.MCC, RuleOperator.In, "5912")),
                1,
                new DebitConfig("200.00", "RUB", "charity-token", UUID.randomUUID(), UUID.randomUUID())
        );
        repository.save(profile);

        assertTrue(repository.findByPaymentTokenOrAccountID("token-abc-xyz", null).isPresent());
        assertTrue(repository.findByPaymentTokenOrAccountID(null, "ACC-TEST-001").isPresent());
        assertTrue(repository.findByPaymentTokenOrAccountID("wrong-token", "ACC-TEST-001").isPresent());
    }
}
