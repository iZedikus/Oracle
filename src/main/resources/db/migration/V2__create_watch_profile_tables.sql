CREATE TABLE oracle.watch_profile (
  watch_profile_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  external_user_id      UUID NOT NULL,
  watch_profile_status  VARCHAR(20) NOT NULL,
  registered_at         TIMESTAMP NOT NULL,
  updated_at            TIMESTAMP NOT NULL,
  last_event_at         TIMESTAMP
);

CREATE TABLE oracle.active_rule (
  active_rule_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  watch_profile_id            UUID NOT NULL REFERENCES oracle.watch_profile(watch_profile_id),
  external_user_scenario_id   UUID NOT NULL UNIQUE,
  scenario_type_code          VARCHAR(60) NOT NULL,
  version                     INT NOT NULL,
  is_enabled                  BOOLEAN NOT NULL DEFAULT TRUE,
  registered_at               TIMESTAMP NOT NULL,
  updated_at                  TIMESTAMP NOT NULL
);

CREATE TABLE oracle.rule_condition (
  condition_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  active_rule_id    UUID NOT NULL REFERENCES oracle.active_rule(active_rule_id) ON DELETE CASCADE,
  field             VARCHAR(40) NOT NULL,
  operator          VARCHAR(20) NOT NULL,
  value             TEXT NOT NULL
);
