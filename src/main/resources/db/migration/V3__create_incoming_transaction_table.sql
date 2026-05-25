CREATE TABLE oracle.incoming_transaction (
  incoming_transaction_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  external_transaction_id   VARCHAR(255) NOT NULL UNIQUE,
  watch_profile_id          UUID REFERENCES oracle.watch_profile(watch_profile_id),
  status                    VARCHAR(20) NOT NULL,
  received_at               TIMESTAMP NOT NULL,
  processed_at              TIMESTAMP,
  credit_debit_indicator    VARCHAR(10),
  mcc                       VARCHAR(10),
  merchant_name             VARCHAR(140),
  merchant_id               VARCHAR(60),
  amount                    VARCHAR(30),
  currency                  CHAR(3),
  booking_date_time         TIMESTAMP,
  value_date_time           TIMESTAMP
);
