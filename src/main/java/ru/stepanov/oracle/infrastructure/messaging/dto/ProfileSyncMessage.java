package ru.stepanov.oracle.infrastructure.messaging.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ProfileSyncMessage {
    public UUID messageId;
    public Instant occurredAt;
    public ScenarioSyncAction action;
    public UUID externalUserId;
    public UUID externalUserScenarioId;
    public String scenarioTypeCode;
    public String paymentToken;
    public String bankBic;
    public int ruleVersion;
    public List<RuleDto> rules;
    public DebitConfigDto debitConfig;
}
