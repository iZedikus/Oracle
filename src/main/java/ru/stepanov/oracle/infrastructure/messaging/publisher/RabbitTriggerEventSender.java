package ru.stepanov.oracle.infrastructure.messaging.publisher;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.stepanov.oracle.application.port.TriggerEventSenderPort;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.infrastructure.messaging.dto.DebitConfigDto;
import ru.stepanov.oracle.infrastructure.messaging.dto.TriggerEventMessage;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitTriggerEventSender implements TriggerEventSenderPort {
    private static final String EXCHANGE = "oracle.events";
    private static final String ROUTING_KEY = "trigger.matched";
    private static final long CONFIRM_TIMEOUT_SECONDS = 10;

    private final RabbitTemplate rabbitTemplate;

    public RabbitTriggerEventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void send(TriggerEvent event) {
        TriggerEventMessage message = toMessage(event);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message, correlationData);
            var confirm = correlationData.getFuture().get(CONFIRM_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!confirm.isAck()) {
                throw new AmqpException("Broker nack for trigger event " + event.getTriggerEventID()
                        + ": " + confirm.getReason());
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new AmqpException("Publish interrupted for trigger event " + event.getTriggerEventID(), ex);
        } catch (ExecutionException | TimeoutException ex) {
            throw new AmqpException("Publish confirm failed for trigger event " + event.getTriggerEventID(), ex);
        }
    }

    static TriggerEventMessage toMessage(TriggerEvent event) {
        return new TriggerEventMessage(
                UUID.randomUUID(),
                event.getOccurredAt(),
                event.getTriggerEventID(),
                event.getExternalUserScenarioID(),
                event.getExternalUserID(),
                event.getTriggerTransactionId(),
                event.getMatchedMcc(),
                event.getMatchedMerchantName(),
                event.getMatchedAmount(),
                event.getMatchedCurrency(),
                event.getScenarioTypeCode(),
                toDebitConfigDto(event.getDebitConfig())
        );
    }

    private static DebitConfigDto toDebitConfigDto(DebitConfig debitConfig) {
        if (debitConfig == null) {
            return null;
        }
        DebitConfigDto dto = new DebitConfigDto();
        dto.debitAmount = debitConfig.debitAmount();
        dto.currency = debitConfig.currency();
        dto.recipientPaymentToken = debitConfig.recipientPaymentToken();
        dto.consentId = debitConfig.consentId();
        dto.sourceAccountId = debitConfig.sourceAccountId();
        return dto;
    }
}
