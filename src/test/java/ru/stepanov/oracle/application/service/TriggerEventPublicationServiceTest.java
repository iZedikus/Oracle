package ru.stepanov.oracle.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.stepanov.oracle.application.port.TriggerEventSenderPort;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEventDeliveryStatus;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerPayload;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TriggerEventPublicationServiceTest {

    @Mock
    private TriggerEventSenderPort triggerEventSenderPort;

    @InjectMocks
    private TriggerEventPublicationService publicationService;

    @Test
    void publish_onSuccess_marksPublishedAndRecordsAttempt() {
        TriggerEvent event = pendingEvent();

        assertTrue(publicationService.publish(event));
        assertEquals(TriggerEventDeliveryStatus.Published, event.getDeliveryStatus());
        assertEquals(1, event.getDeliveryAttempts().size());
        assertTrue(event.getDeliveryAttempts().getFirst().success());
        verify(triggerEventSenderPort).send(event);
    }

    @Test
    void publish_onFailure_schedulesRetryAndRecordsAttempt() {
        TriggerEvent event = pendingEvent();
        doThrow(new RuntimeException("broker down")).when(triggerEventSenderPort).send(any());

        assertFalse(publicationService.publish(event));
        assertEquals(TriggerEventDeliveryStatus.Retrying, event.getDeliveryStatus());
        assertEquals(1, event.getDeliveryAttempts().size());
        assertFalse(event.getDeliveryAttempts().getFirst().success());
    }

    private static TriggerEvent pendingEvent() {
        return TriggerEvent.restore(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                TriggerEventDeliveryStatus.Pending,
                new TriggerPayload("TX-1", "5912", "Shop", "100.00", "RUB", "UNDESIRABLE_PURCHASE", null, Instant.now()),
                List.of(),
                0,
                Instant.now(),
                null,
                null
        );
    }
}
