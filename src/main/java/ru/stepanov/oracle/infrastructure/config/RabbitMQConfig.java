package ru.stepanov.oracle.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String SIMULACRUM_EVENTS = "simulacrum.events";
    public static final String SIMULACRUM_EVENTS_DLX = "simulacrum.events.dlx";
    public static final String IS_EVENTS = "is.events";
    public static final String IS_EVENTS_DLX = "is.events.dlx";
    public static final String ORACLE_EVENTS = "oracle.events";
    public static final String ORACLE_EVENTS_DLX = "oracle.events.dlx";

    public static final String ROUTING_TRANSACTION_CREATED = "transaction.created";
    public static final String ROUTING_TRANSACTION_CREATED_DEAD = "transaction.created.dead";
    public static final String ROUTING_SCENARIO_PROFILE_SYNC = "scenario.profile.sync";
    public static final String ROUTING_SCENARIO_PROFILE_SYNC_DEAD = "scenario.profile.sync.dead";

    @Bean
    public TopicExchange simulacrumEventsExchange() {
        return new TopicExchange(SIMULACRUM_EVENTS, true, false);
    }

    @Bean
    public TopicExchange simulacrumEventsDlxExchange() {
        return new TopicExchange(SIMULACRUM_EVENTS_DLX, true, false);
    }

    @Bean
    public TopicExchange isEventsExchange() {
        return new TopicExchange(IS_EVENTS, true, false);
    }

    @Bean
    public TopicExchange isEventsDlxExchange() {
        return new TopicExchange(IS_EVENTS_DLX, true, false);
    }

    @Bean
    public TopicExchange oracleEventsExchange() {
        return new TopicExchange(ORACLE_EVENTS, true, false);
    }

    @Bean
    public TopicExchange oracleEventsDlxExchange() {
        return new TopicExchange(ORACLE_EVENTS_DLX, true, false);
    }

    @Bean
    public Queue oracleInboxQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", SIMULACRUM_EVENTS_DLX);
        arguments.put("x-dead-letter-routing-key", ROUTING_TRANSACTION_CREATED_DEAD);
        arguments.put("x-message-ttl", 86_400_000);
        return new Queue("oracle.inbox", true, false, false, arguments);
    }

    @Bean
    public Queue oracleInboxDlqQueue() {
        return new Queue("oracle.inbox.dlq", true);
    }

    @Bean
    public Queue oracleProfileQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", IS_EVENTS_DLX);
        arguments.put("x-dead-letter-routing-key", ROUTING_SCENARIO_PROFILE_SYNC_DEAD);
        return new Queue("oracle.profile", true, false, false, arguments);
    }

    @Bean
    public Queue oracleProfileDlqQueue() {
        return new Queue("oracle.profile.dlq", true);
    }

    @Bean
    public Binding oracleInboxBinding() {
        return BindingBuilder.bind(oracleInboxQueue())
                .to(simulacrumEventsExchange())
                .with(ROUTING_TRANSACTION_CREATED);
    }

    @Bean
    public Binding oracleInboxDlqBinding() {
        return BindingBuilder.bind(oracleInboxDlqQueue())
                .to(simulacrumEventsDlxExchange())
                .with(ROUTING_TRANSACTION_CREATED_DEAD);
    }

    @Bean
    public Binding oracleProfileBinding() {
        return BindingBuilder.bind(oracleProfileQueue())
                .to(isEventsExchange())
                .with(ROUTING_SCENARIO_PROFILE_SYNC);
    }

    @Bean
    public Binding oracleProfileDlqBinding() {
        return BindingBuilder.bind(oracleProfileDlqQueue())
                .to(isEventsDlxExchange())
                .with(ROUTING_SCENARIO_PROFILE_SYNC_DEAD);
    }
}
