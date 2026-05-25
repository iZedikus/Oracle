package ru.stepanov.oracle.application.usecase.triggerevent;

import org.springframework.stereotype.Service;
import ru.stepanov.oracle.application.port.TriggerEventSenderPort;
import ru.stepanov.oracle.application.repository.TriggerEventRepository;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;

@Service
public class RetryTriggerEventUseCase {
    private final TriggerEventRepository triggerEventRepository;
    private final TriggerEventSenderPort triggerEventSenderPort;

    public RetryTriggerEventUseCase(TriggerEventRepository triggerEventRepository,
                                    TriggerEventSenderPort triggerEventSenderPort) {
        this.triggerEventRepository = triggerEventRepository;
        this.triggerEventSenderPort = triggerEventSenderPort;
    }

    public void execute() {
        for (TriggerEvent event : triggerEventRepository.findPendingForRetry()) {
            if (event.isExhausted()) {
                event.markFailed("Retry limit exhausted");
            } else {
                try {
                    triggerEventSenderPort.send(event);
                    event.markDelivered();
                } catch (RuntimeException ex) {
                    event.scheduleRetry();
                }
            }
            triggerEventRepository.save(event);
        }
    }
}
