package ru.stepanov.oracle.infrastructure.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEventDeliveryStatus;
import ru.stepanov.oracle.application.repository.TriggerEventRepository;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Profile("test")
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
        return events.stream()
                .filter(event -> event.getDeliveryStatus() == TriggerEventDeliveryStatus.Pending
                        || event.getDeliveryStatus() == TriggerEventDeliveryStatus.Retrying)
                .toList();
    }
}
