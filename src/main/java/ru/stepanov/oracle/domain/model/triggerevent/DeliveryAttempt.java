package ru.stepanov.oracle.domain.model.triggerevent;

import java.time.Instant;

public record DeliveryAttempt(int attemptNumber, Instant attemptedAt, boolean success, int httpStatus, String errorMessage) {
}
