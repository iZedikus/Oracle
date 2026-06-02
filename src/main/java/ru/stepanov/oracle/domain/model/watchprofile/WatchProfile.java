package ru.stepanov.oracle.domain.model.watchprofile;

import ru.stepanov.oracle.domain.event.DomainEvent;
import ru.stepanov.oracle.domain.event.WatchProfileRegisteredEvent;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WatchProfile {
    private final UUID watchProfileID;
    private final UUID externalUserID;
    private final String paymentToken;
    private final String accountId;
    private DebitConfig debitConfig;
    private WatchProfileStatus watchProfileStatus;
    private final List<ActiveRule> rules;
    private final Instant registeredAt;
    private Instant updatedAt;
    private Instant lastEventAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private WatchProfile(UUID watchProfileID, UUID externalUserID, String paymentToken, String accountId,
                         DebitConfig debitConfig, WatchProfileStatus watchProfileStatus,
                         List<ActiveRule> rules, Instant registeredAt, Instant updatedAt, Instant lastEventAt) {
        this.watchProfileID = watchProfileID;
        this.externalUserID = externalUserID;
        this.paymentToken = paymentToken;
        this.accountId = accountId;
        this.debitConfig = debitConfig;
        this.watchProfileStatus = watchProfileStatus;
        this.rules = new ArrayList<>(rules);
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
        this.lastEventAt = lastEventAt;
    }

    public static WatchProfile create(UUID externalUserID, UUID externalUserScenarioID,
                                      String scenarioTypeCode, String paymentToken, String accountId,
                                      List<RuleCondition> rules, int ruleVersion,
                                      DebitConfig debitConfig) {
        ActiveRule activeRule = new ActiveRule(UUID.randomUUID(), externalUserScenarioID, scenarioTypeCode,
                ruleVersion, true, rules, Instant.now(), Instant.now());
        WatchProfile profile = new WatchProfile(UUID.randomUUID(), externalUserID, paymentToken, accountId, debitConfig,
                WatchProfileStatus.Active, List.of(activeRule), Instant.now(), Instant.now(), null);
        profile.domainEvents.add(new WatchProfileRegisteredEvent(profile.watchProfileID, externalUserID, Instant.now()));
        return profile;
    }

    public void register(UUID userID) { recordEvent(Instant.now()); }
    public void pause() { this.watchProfileStatus = WatchProfileStatus.Paused; this.updatedAt = Instant.now(); }
    public void resume() { this.watchProfileStatus = WatchProfileStatus.Active; this.updatedAt = Instant.now(); }
    public void terminate() { this.watchProfileStatus = WatchProfileStatus.Terminated; this.updatedAt = Instant.now(); }
    public void addRule(ActiveRule rule) { this.rules.add(rule); this.updatedAt = Instant.now(); }
    public void updateRule(ActiveRule rule) {
        removeRule(rule.getActiveRuleID());
        addRule(rule);
    }

    public void updateDebitConfig(DebitConfig debitConfig) {
        this.debitConfig = debitConfig;
        this.updatedAt = Instant.now();
    }
    public void removeRule(UUID ruleID) { this.rules.removeIf(r -> r.getActiveRuleID().equals(ruleID)); this.updatedAt = Instant.now(); }
    public List<ActiveRule> findActiveRules() { return rules.stream().filter(ActiveRule::isApplicable).toList(); }
    public void recordEvent(Instant at) { this.lastEventAt = at; this.updatedAt = Instant.now(); }

    public UUID getWatchProfileID() { return watchProfileID; }
    public UUID getExternalUserID() { return externalUserID; }
    public String getPaymentToken() { return paymentToken; }
    public String getAccountId() { return accountId; }
    public DebitConfig getDebitConfig() { return debitConfig; }
    public WatchProfileStatus getWatchProfileStatus() { return watchProfileStatus; }
    public List<ActiveRule> getRules() { return List.copyOf(rules); }
    public Instant getRegisteredAt() { return registeredAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getLastEventAt() { return lastEventAt; }
    public List<DomainEvent> pullDomainEvents() { var copy = List.copyOf(domainEvents); domainEvents.clear(); return copy; }
}
