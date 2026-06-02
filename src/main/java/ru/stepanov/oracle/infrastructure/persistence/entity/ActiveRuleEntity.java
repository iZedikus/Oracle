package ru.stepanov.oracle.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "active_rule", schema = "oracle")
public class ActiveRuleEntity {

    @Id
    @Column(name = "active_rule_id")
    private UUID activeRuleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "watch_profile_id", nullable = false)
    private WatchProfileEntity watchProfile;

    @Column(name = "external_user_scenario_id", nullable = false, unique = true)
    private UUID externalUserScenarioId;

    @Column(name = "scenario_type_code", nullable = false)
    private String scenarioTypeCode;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "activeRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RuleConditionEntity> conditions = new ArrayList<>();

    public UUID getActiveRuleId() { return activeRuleId; }
    public void setActiveRuleId(UUID activeRuleId) { this.activeRuleId = activeRuleId; }
    public WatchProfileEntity getWatchProfile() { return watchProfile; }
    public void setWatchProfile(WatchProfileEntity watchProfile) { this.watchProfile = watchProfile; }
    public UUID getExternalUserScenarioId() { return externalUserScenarioId; }
    public void setExternalUserScenarioId(UUID externalUserScenarioId) { this.externalUserScenarioId = externalUserScenarioId; }
    public String getScenarioTypeCode() { return scenarioTypeCode; }
    public void setScenarioTypeCode(String scenarioTypeCode) { this.scenarioTypeCode = scenarioTypeCode; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Instant getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(Instant registeredAt) { this.registeredAt = registeredAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<RuleConditionEntity> getConditions() { return conditions; }
    public void setConditions(List<RuleConditionEntity> conditions) { this.conditions = conditions; }
}
