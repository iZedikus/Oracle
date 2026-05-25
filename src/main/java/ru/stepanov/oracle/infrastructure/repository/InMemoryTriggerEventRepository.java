package ru.stepanov.oracle.infrastructure.repository;

import org.springframework.stereotype.Repository;
import ru.stepanov.oracle.application.repository.TriggerEventRepository;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryTriggerEventRepository implements TriggerEventRepository {
    private final List<TriggerEvent> events = new CopyOnWriteArrayList<>();

    @Override
    public TriggerEvent save(TriggerEvent triggerEvent) {
        events.remove(triggerEvent);
        events.add(triggerEvent);
        return triggerEvent;
    }

    @Override
    public List<TriggerEvent> findPendingForRetry() {
        return List.copyOf(events);
    }
}
