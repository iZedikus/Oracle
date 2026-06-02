package ru.stepanov.oracle.application.service;

import org.springframework.stereotype.Service;
import ru.stepanov.oracle.application.port.TriggerEventSenderPort;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;

@Service
public class TriggerEventPublicationService {

    private final TriggerEventSenderPort triggerEventSenderPort;

    public TriggerEventPublicationService(TriggerEventSenderPort triggerEventSenderPort) {
        this.triggerEventSenderPort = triggerEventSenderPort;
    }

    /**
     * Публикует событие в RabbitMQ и обновляет статус/attempts в доменной модели.
     *
     * @return true, если брокер подтвердил публикацию; false при ошибке (событие переведено в Retrying/Failed)
     */
    public boolean publish(TriggerEvent event) {
        try {
            triggerEventSenderPort.send(event);
            event.markPublished();
            return true;
        } catch (RuntimeException ex) {
            String message = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            event.registerPublishFailure(message);
            return false;
        }
    }
}
