package ru.stepanov.oracle.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public TopicExchange simulacrumEventsExchange() { return new TopicExchange("simulacrum.events", true, false); }
    @Bean
    public TopicExchange isEventsExchange() { return new TopicExchange("is.events", true, false); }
    @Bean
    public TopicExchange oracleEventsExchange() { return new TopicExchange("oracle.events", true, false); }
    @Bean
    public Queue oracleInboxQueue() { return new Queue("oracle.inbox", true); }
    @Bean
    public Queue oracleProfileQueue() { return new Queue("oracle.profile", true); }
    @Bean
    public Binding oracleInboxBinding() { return BindingBuilder.bind(oracleInboxQueue()).to(simulacrumEventsExchange()).with("transaction.created"); }
    @Bean
    public Binding oracleProfileBinding() { return BindingBuilder.bind(oracleProfileQueue()).to(isEventsExchange()).with("scenario.profile.sync"); }
}
