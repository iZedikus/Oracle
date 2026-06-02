package ru.stepanov.oracle.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "rule_condition", schema = "oracle")
public class RuleConditionEntity {

    @Id
    @Column(name = "condition_id")
    private UUID conditionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "active_rule_id", nullable = false)
    private ActiveRuleEntity activeRule;

    @Column(name = "field", nullable = false)
    private String field;

    @Column(name = "operator", nullable = false)
    private String operator;

    @Column(name = "value", nullable = false)
    private String value;

    public UUID getConditionId() { return conditionId; }
    public void setConditionId(UUID conditionId) { this.conditionId = conditionId; }
    public ActiveRuleEntity getActiveRule() { return activeRule; }
    public void setActiveRule(ActiveRuleEntity activeRule) { this.activeRule = activeRule; }
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
