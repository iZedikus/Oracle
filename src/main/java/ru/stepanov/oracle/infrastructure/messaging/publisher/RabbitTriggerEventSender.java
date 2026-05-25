package ru.stepanov.oracle.infrastructure.messaging.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.stepanov.oracle.application.port.TriggerEventSenderPort;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.infrastructure.messaging.dto.TriggerEventMessage;

import java.time.Instant;
import java.util.UUID;

@Component
public class RabbitTriggerEventSender implements TriggerEventSenderPort {
    private final RabbitTemplate rabbitTemplate;

    public RabbitTriggerEventSender(RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }

    @Override
    public void send(TriggerEvent event) {
        TriggerEventMessage message = new TriggerEventMessage(UUID.randomUUID(), Instant.now(), UUID.randomUUID(), null, null, "", "", "", "", "", "");
        rabbitTemplate.convertAndSend("oracle.events", "trigger.matched", message);
    }
}
