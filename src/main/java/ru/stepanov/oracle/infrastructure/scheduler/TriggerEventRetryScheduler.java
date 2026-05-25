package ru.stepanov.oracle.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.stepanov.oracle.application.usecase.triggerevent.RetryTriggerEventUseCase;

@Component
public class TriggerEventRetryScheduler {
    private final RetryTriggerEventUseCase retryTriggerEventUseCase;

    public TriggerEventRetryScheduler(RetryTriggerEventUseCase retryTriggerEventUseCase) {
        this.retryTriggerEventUseCase = retryTriggerEventUseCase;
    }

    @Scheduled(fixedDelay = 60000)
    public void retry() { retryTriggerEventUseCase.execute(); }
}
