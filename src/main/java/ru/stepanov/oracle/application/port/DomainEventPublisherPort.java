package ru.stepanov.oracle.application.port;

import ru.stepanov.oracle.domain.event.DomainEvent;

public interface DomainEventPublisherPort {
    void publish(DomainEvent event);
}
