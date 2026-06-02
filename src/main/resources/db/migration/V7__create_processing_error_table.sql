CREATE TABLE oracle.processing_error (
  id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  source                  VARCHAR(50) NOT NULL,
  external_transaction_id VARCHAR(255),
  trigger_event_id        UUID REFERENCES oracle.trigger_event(trigger_event_id),
  message                 TEXT NOT NULL,
  occurred_at             TIMESTAMP NOT NULL
);
