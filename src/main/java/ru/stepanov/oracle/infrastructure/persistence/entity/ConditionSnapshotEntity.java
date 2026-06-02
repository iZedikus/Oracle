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
@Table(name = "condition_snapshot", schema = "oracle")
public class ConditionSnapshotEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_attempt_id", nullable = false)
    private MatchAttemptEntity matchAttempt;

    @Column(name = "expected_value")
    private String expectedValue;

    @Column(name = "actual_value")
    private String actualValue;

    @Column(name = "passed", nullable = false)
    private boolean passed;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public MatchAttemptEntity getMatchAttempt() { return matchAttempt; }
    public void setMatchAttempt(MatchAttemptEntity matchAttempt) { this.matchAttempt = matchAttempt; }
    public String getExpectedValue() { return expectedValue; }
    public void setExpectedValue(String expectedValue) { this.expectedValue = expectedValue; }
    public String getActualValue() { return actualValue; }
    public void setActualValue(String actualValue) { this.actualValue = actualValue; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}
