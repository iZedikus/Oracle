CREATE TABLE oracle.match_attempt (
  match_attempt_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  incoming_transaction_id   UUID NOT NULL REFERENCES oracle.incoming_transaction(incoming_transaction_id),
  watch_account_id          UUID NOT NULL,
  result                    VARCHAR(20) NOT NULL,
  attempted_at              TIMESTAMP NOT NULL
);

CREATE TABLE oracle.condition_snapshot (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  match_attempt_id  UUID NOT NULL REFERENCES oracle.match_attempt(match_attempt_id) ON DELETE CASCADE,
  expected_value    TEXT,
  actual_value      TEXT,
  passed            BOOLEAN NOT NULL
);
