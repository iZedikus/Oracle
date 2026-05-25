CREATE INDEX idx_incoming_tx_watch_profile
  ON oracle.incoming_transaction(watch_profile_id);

CREATE INDEX idx_incoming_tx_status
  ON oracle.incoming_transaction(status);

CREATE INDEX idx_match_attempt_tx_id
  ON oracle.match_attempt(incoming_transaction_id);

CREATE INDEX idx_trigger_event_status_scheduled
  ON oracle.trigger_event(delivery_status, scheduled_at);

CREATE INDEX idx_active_rule_scenario_id
  ON oracle.active_rule(external_user_scenario_id);

CREATE INDEX idx_watch_profile_external_user
  ON oracle.watch_profile(external_user_id);
