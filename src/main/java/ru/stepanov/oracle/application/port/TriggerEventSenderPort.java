package ru.stepanov.oracle.application.port;

import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;

public interface TriggerEventSenderPort {
    void send(TriggerEvent event);
}
