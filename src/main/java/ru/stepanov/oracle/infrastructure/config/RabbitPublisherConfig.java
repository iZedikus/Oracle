package ru.stepanov.oracle.infrastructure.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitPublisherConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returned -> {
            throw new org.springframework.amqp.AmqpException(
                    "Unroutable trigger message: exchange=" + returned.getExchange()
                            + ", routingKey=" + returned.getRoutingKey()
                            + ", replyCode=" + returned.getReplyCode()
                            + ", replyText=" + returned.getReplyText());
        });
        return rabbitTemplate;
    }
}
