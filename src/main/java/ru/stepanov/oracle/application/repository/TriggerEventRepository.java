package ru.stepanov.oracle.application.repository;

import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;

import java.util.List;

public interface TriggerEventRepository {
    TriggerEvent save(TriggerEvent triggerEvent);
    List<TriggerEvent> findPendingForRetry();
}
