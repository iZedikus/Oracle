package ru.stepanov.oracle.domain.model.watchprofile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActiveRule {
    private final UUID activeRuleID;
    private final UUID externalUserScenarioID;
    private final String scenarioTypeCode;
    private int version;
    private boolean isEnabled;
    private List<RuleCondition> conditions;
    private final Instant registeredAt;
    private Instant updatedAt;

    public ActiveRule(UUID activeRuleID, UUID externalUserScenarioID, String scenarioTypeCode, int version,
                      boolean isEnabled, List<RuleCondition> conditions, Instant registeredAt, Instant updatedAt) {
        this.activeRuleID = activeRuleID;
        this.externalUserScenarioID = externalUserScenarioID;
        this.scenarioTypeCode = scenarioTypeCode;
        this.version = version;
        this.isEnabled = isEnabled;
        this.conditions = new ArrayList<>(conditions);
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
    }

    public void enable() { this.isEnabled = true; this.updatedAt = Instant.now(); }
    public void disable() { this.isEnabled = false; this.updatedAt = Instant.now(); }
    public void updateConditions(List<RuleCondition> conditions, int version) {
        this.conditions = new ArrayList<>(conditions);
        this.version = version;
        this.updatedAt = Instant.now();
    }
    public boolean isApplicable() { return isEnabled && !conditions.isEmpty(); }

    public UUID getActiveRuleID() { return activeRuleID; }
    public UUID getExternalUserScenarioID() { return externalUserScenarioID; }
    public String getScenarioTypeCode() { return scenarioTypeCode; }
    public int getVersion() { return version; }
    public boolean isEnabled() { return isEnabled; }
    public List<RuleCondition> getConditions() { return List.copyOf(conditions); }
    public Instant getRegisteredAt() { return registeredAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
