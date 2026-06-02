package ru.stepanov.oracle.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match_attempt", schema = "oracle")
public class MatchAttemptEntity {

    @Id
    @Column(name = "match_attempt_id")
    private UUID matchAttemptId;

    @Column(name = "incoming_transaction_id", nullable = false)
    private UUID incomingTransactionId;

    @Column(name = "watch_account_id", nullable = false)
    private UUID watchAccountId;

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "attempted_at", nullable = false)
    private Instant attemptedAt;

    @OneToMany(mappedBy = "matchAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConditionSnapshotEntity> snapshots = new ArrayList<>();

    public UUID getMatchAttemptId() { return matchAttemptId; }
    public void setMatchAttemptId(UUID matchAttemptId) { this.matchAttemptId = matchAttemptId; }
    public UUID getIncomingTransactionId() { return incomingTransactionId; }
    public void setIncomingTransactionId(UUID incomingTransactionId) { this.incomingTransactionId = incomingTransactionId; }
    public UUID getWatchAccountId() { return watchAccountId; }
    public void setWatchAccountId(UUID watchAccountId) { this.watchAccountId = watchAccountId; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public Instant getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(Instant attemptedAt) { this.attemptedAt = attemptedAt; }
    public List<ConditionSnapshotEntity> getSnapshots() { return snapshots; }
    public void setSnapshots(List<ConditionSnapshotEntity> snapshots) { this.snapshots = snapshots; }
}
