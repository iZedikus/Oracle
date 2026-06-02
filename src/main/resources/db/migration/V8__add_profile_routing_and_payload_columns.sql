ALTER TABLE oracle.watch_profile
  ADD COLUMN payment_token VARCHAR(255),
  ADD COLUMN account_id VARCHAR(255),
  ADD COLUMN debit_amount VARCHAR(30),
  ADD COLUMN debit_currency CHAR(3),
  ADD COLUMN recipient_payment_token VARCHAR(255),
  ADD COLUMN consent_id UUID,
  ADD COLUMN source_account_id UUID;

ALTER TABLE oracle.incoming_transaction
  ADD COLUMN error_message TEXT;

ALTER TABLE oracle.trigger_event
  ADD COLUMN debit_amount VARCHAR(30),
  ADD COLUMN debit_currency CHAR(3),
  ADD COLUMN recipient_payment_token VARCHAR(255),
  ADD COLUMN consent_id UUID,
  ADD COLUMN source_account_id UUID;

CREATE INDEX idx_watch_profile_payment_token ON oracle.watch_profile(payment_token);
CREATE INDEX idx_watch_profile_account_id ON oracle.watch_profile(account_id);
