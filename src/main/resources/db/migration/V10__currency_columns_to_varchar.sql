-- Hibernate maps String + length as VARCHAR; Flyway originally used CHAR(3) (PostgreSQL bpchar).
ALTER TABLE oracle.incoming_transaction
  ALTER COLUMN currency TYPE VARCHAR(3) USING trim(currency);

ALTER TABLE oracle.watch_profile
  ALTER COLUMN debit_currency TYPE VARCHAR(3) USING trim(debit_currency);

ALTER TABLE oracle.trigger_event
  ALTER COLUMN matched_currency TYPE VARCHAR(3) USING trim(matched_currency),
  ALTER COLUMN debit_currency TYPE VARCHAR(3) USING trim(debit_currency);
