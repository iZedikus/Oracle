CREATE TABLE oracle.trigger_event (
  trigger_event_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  match_attempt_id            UUID NOT NULL REFERENCES oracle.match_attempt(match_attempt_id),
  external_user_scenario_id   UUID NOT NULL,
  external_user_id            UUID NOT NULL,
  delivery_status             VARCHAR(20) NOT NULL,
  retry_count                 INT NOT NULL DEFAULT 0,
  scheduled_at                TIMESTAMP NOT NULL,
  delivered_at                TIMESTAMP,
  last_retry_at               TIMESTAMP,
  trigger_transaction_id      VARCHAR(255),
  matched_mcc                 VARCHAR(10),
  matched_merchant_name       VARCHAR(140),
  matched_amount              VARCHAR(30),
  matched_currency            CHAR(3),
  scenario_type_code          VARCHAR(60),
  occured_at                  TIMESTAMP
);

CREATE TABLE oracle.delivery_attempt (
  id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  trigger_event_id   UUID NOT NULL REFERENCES oracle.trigger_event(trigger_event_id) ON DELETE CASCADE,
  attempt_number     INT NOT NULL,
  attempted_at       TIMESTAMP NOT NULL,
  success            BOOLEAN NOT NULL,
  http_status        INT,
  error_message      TEXT
);
