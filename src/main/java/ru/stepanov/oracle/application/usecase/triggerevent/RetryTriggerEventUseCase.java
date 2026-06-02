package ru.stepanov.oracle.application.usecase.triggerevent;

import org.springframework.stereotype.Service;
import ru.stepanov.oracle.application.repository.TriggerEventRepository;
import ru.stepanov.oracle.application.service.TriggerEventPublicationService;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;

@Service
public class RetryTriggerEventUseCase {
    private final TriggerEventRepository triggerEventRepository;
    private final TriggerEventPublicationService triggerEventPublicationService;

    public RetryTriggerEventUseCase(TriggerEventRepository triggerEventRepository,
                                    TriggerEventPublicationService triggerEventPublicationService) {
        this.triggerEventRepository = triggerEventRepository;
        this.triggerEventPublicationService = triggerEventPublicationService;
    }

    public void execute() {
        for (TriggerEvent event : triggerEventRepository.findPendingForRetry()) {
            if (event.isExhausted()) {
                event.markFailed("Retry limit exhausted");
            } else {
                triggerEventPublicationService.publish(event);
            }
            triggerEventRepository.save(event);
        }
    }
}
