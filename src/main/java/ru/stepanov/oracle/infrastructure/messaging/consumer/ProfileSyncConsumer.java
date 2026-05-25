package ru.stepanov.oracle.infrastructure.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.stepanov.oracle.application.usecase.profile.PauseWatchProfileUseCase;
import ru.stepanov.oracle.application.usecase.profile.RegisterWatchProfileUseCase;
import ru.stepanov.oracle.application.usecase.profile.TerminateWatchProfileUseCase;
import ru.stepanov.oracle.application.usecase.profile.UpdateWatchProfileRulesUseCase;
import ru.stepanov.oracle.domain.model.watchprofile.RuleField;
import ru.stepanov.oracle.domain.model.watchprofile.RuleOperator;
import ru.stepanov.oracle.infrastructure.messaging.dto.ProfileSyncMessage;

@Component
public class ProfileSyncConsumer {
    private final RegisterWatchProfileUseCase register;
    private final UpdateWatchProfileRulesUseCase update;
    private final PauseWatchProfileUseCase pause;
    private final TerminateWatchProfileUseCase terminate;

    public ProfileSyncConsumer(RegisterWatchProfileUseCase register, UpdateWatchProfileRulesUseCase update, PauseWatchProfileUseCase pause, TerminateWatchProfileUseCase terminate) {
        this.register = register;
        this.update = update;
        this.pause = pause;
        this.terminate = terminate;
    }

    @RabbitListener(queues = "oracle.profile")
    public void consume(ProfileSyncMessage message) {
        switch (message.action) {
            case REGISTER -> register.execute(new RegisterWatchProfileUseCase.RegisterWatchProfileCommand(
                    message.externalUserId, message.externalUserScenarioId, message.scenarioTypeCode, message.paymentToken,
                    message.bankBic, message.ruleVersion,
                    message.rules.stream().map(r -> new RegisterWatchProfileUseCase.RuleConditionDto(
                            RuleField.valueOf(r.field), RuleOperator.valueOf(r.operator), r.value)).toList(),
                    new RegisterWatchProfileUseCase.DebitConfigDto("from_is")
            ));
            case UPDATE_RULES -> update.execute(new UpdateWatchProfileRulesUseCase.UpdateRulesCommand(
                    message.externalUserScenarioId,
                    message.ruleVersion,
                    message.rules.stream().map(r -> new UpdateWatchProfileRulesUseCase.RuleConditionDto(
                            RuleField.valueOf(r.field), RuleOperator.valueOf(r.operator), r.value)).toList()
            ));
            case PAUSE -> pause.execute(message.externalUserScenarioId);
            case TERMINATE -> terminate.execute(message.externalUserScenarioId);
        }
    }
}
