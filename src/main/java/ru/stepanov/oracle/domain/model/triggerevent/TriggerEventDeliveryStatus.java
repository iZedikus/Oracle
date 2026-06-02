package ru.stepanov.oracle.domain.model.triggerevent;

public enum TriggerEventDeliveryStatus {
    /** Ожидает публикации в RabbitMQ. */
    Pending,
    /** Успешно опубликовано в exchange (подтверждение брокера), IS ещё не подтверждал обработку. */
    Published,
    /** Публикация не удалась, запланирован повтор. */
    Retrying,
    /** Исчерпаны попытки публикации. */
    Failed
}
