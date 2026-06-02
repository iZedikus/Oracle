package ru.stepanov.oracle.infrastructure.persistence.mapper;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.stepanov.oracle.domain.model.incomingtransaction.CreditDebitCode;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransaction;
import ru.stepanov.oracle.domain.model.incomingtransaction.IncomingTransactionStatus;
import ru.stepanov.oracle.domain.model.incomingtransaction.TransactionData;
import ru.stepanov.oracle.domain.model.matchattempt.ConditionSnapshot;
import ru.stepanov.oracle.domain.model.matchattempt.MatchAttempt;
import ru.stepanov.oracle.domain.model.matchattempt.MatchingResult;
import ru.stepanov.oracle.domain.model.triggerevent.DebitConfig;
import ru.stepanov.oracle.domain.model.triggerevent.DeliveryAttempt;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEvent;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerEventDeliveryStatus;
import ru.stepanov.oracle.domain.model.triggerevent.TriggerPayload;
import ru.stepanov.oracle.domain.model.watchprofile.ActiveRule;
import ru.stepanov.oracle.domain.model.watchprofile.RuleCondition;
import ru.stepanov.oracle.domain.model.watchprofile.RuleField;
import ru.stepanov.oracle.domain.model.watchprofile.RuleOperator;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfile;
import ru.stepanov.oracle.domain.model.watchprofile.WatchProfileStatus;
import ru.stepanov.oracle.infrastructure.persistence.entity.ActiveRuleEntity;
import ru.stepanov.oracle.infrastructure.persistence.entity.ConditionSnapshotEntity;
import ru.stepanov.oracle.infrastructure.persistence.entity.DeliveryAttemptEntity;
import ru.stepanov.oracle.infrastructure.persistence.entity.IncomingTransactionEntity;
import ru.stepanov.oracle.infrastructure.persistence.entity.MatchAttemptEntity;
import ru.stepanov.oracle.infrastructure.persistence.entity.RuleConditionEntity;
import ru.stepanov.oracle.infrastructure.persistence.entity.TriggerEventEntity;
import ru.stepanov.oracle.infrastructure.persistence.entity.WatchProfileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Profile("!test")
public class PersistenceMapper {

    public WatchProfileEntity toEntity(WatchProfile profile) {
        WatchProfileEntity entity = new WatchProfileEntity();
        entity.setWatchProfileId(profile.getWatchProfileID());
        entity.setExternalUserId(profile.getExternalUserID());
        entity.setPaymentToken(profile.getPaymentToken());
        entity.setAccountId(profile.getAccountId());
        entity.setWatchProfileStatus(profile.getWatchProfileStatus().name());
        entity.setRegisteredAt(profile.getRegisteredAt());
        entity.setUpdatedAt(profile.getUpdatedAt());
        entity.setLastEventAt(profile.getLastEventAt());
        applyDebitConfig(entity, profile.getDebitConfig());

        List<ActiveRuleEntity> ruleEntities = new ArrayList<>();
        for (ActiveRule rule : profile.getRules()) {
            ActiveRuleEntity ruleEntity = toRuleEntity(rule);
            ruleEntity.setWatchProfile(entity);
            ruleEntities.add(ruleEntity);
        }
        entity.setRules(ruleEntities);
        return entity;
    }

    public WatchProfile toDomain(WatchProfileEntity entity) {
        List<ActiveRule> rules = entity.getRules().stream().map(this::toRuleDomain).toList();
        return WatchProfile.restore(
                entity.getWatchProfileId(),
                entity.getExternalUserId(),
                entity.getPaymentToken(),
                entity.getAccountId(),
                readDebitConfig(entity),
                WatchProfileStatus.valueOf(entity.getWatchProfileStatus()),
                rules,
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getLastEventAt()
        );
    }

    public IncomingTransactionEntity toEntity(IncomingTransaction transaction) {
        IncomingTransactionEntity entity = new IncomingTransactionEntity();
        entity.setIncomingTransactionId(transaction.getIncomingTransactionID());
        entity.setExternalTransactionId(transaction.getExternalTransactionID());
        entity.setWatchProfileId(transaction.getWatchProfileID());
        entity.setStatus(transaction.getStatus().name());
        entity.setReceivedAt(transaction.getReceivedAt());
        entity.setProcessedAt(transaction.getProcessedAt());
        entity.setErrorMessage(transaction.getErrorMessage());

        TransactionData data = transaction.getData();
        entity.setCreditDebitIndicator(data.creditDebitIndicator().name());
        entity.setMcc(data.mcc());
        entity.setMerchantName(data.merchantName());
        entity.setMerchantId(data.merchantID());
        entity.setAmount(data.amount());
        entity.setCurrency(data.currency());
        entity.setBookingDateTime(data.bookingDateTime());
        entity.setValueDateTime(data.valueDateTime());
        return entity;
    }

    public IncomingTransaction toDomain(IncomingTransactionEntity entity) {
        TransactionData data = new TransactionData(
                CreditDebitCode.valueOf(entity.getCreditDebitIndicator()),
                entity.getMcc(),
                entity.getMerchantName(),
                entity.getMerchantId(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getBookingDateTime(),
                entity.getValueDateTime()
        );
        return IncomingTransaction.restore(
                entity.getIncomingTransactionId(),
                entity.getExternalTransactionId(),
                entity.getWatchProfileId(),
                IncomingTransactionStatus.valueOf(entity.getStatus()),
                data,
                entity.getReceivedAt(),
                entity.getProcessedAt(),
                entity.getErrorMessage()
        );
    }

    public MatchAttemptEntity toEntity(MatchAttempt attempt) {
        MatchAttemptEntity entity = new MatchAttemptEntity();
        entity.setMatchAttemptId(attempt.getMatchAttemptID());
        entity.setIncomingTransactionId(attempt.getIncomingTransactionID());
        entity.setWatchAccountId(attempt.getWatchAccountID());
        entity.setResult(attempt.getResult().name());
        entity.setAttemptedAt(attempt.getAttemptedAt());

        List<ConditionSnapshotEntity> snapshotEntities = new ArrayList<>();
        for (ConditionSnapshot snapshot : attempt.getSnapshots()) {
            ConditionSnapshotEntity snapshotEntity = new ConditionSnapshotEntity();
            snapshotEntity.setId(UUID.randomUUID());
            snapshotEntity.setMatchAttempt(entity);
            snapshotEntity.setExpectedValue(snapshot.expectedValue());
            snapshotEntity.setActualValue(snapshot.actualValue());
            snapshotEntity.setPassed(snapshot.passed());
            snapshotEntities.add(snapshotEntity);
        }
        entity.setSnapshots(snapshotEntities);
        return entity;
    }

    public MatchAttempt toDomain(MatchAttemptEntity entity) {
        List<ConditionSnapshot> snapshots = entity.getSnapshots().stream()
                .map(snapshot -> new ConditionSnapshot(
                        snapshot.getExpectedValue(),
                        snapshot.getActualValue(),
                        snapshot.isPassed()))
                .toList();
        return MatchAttempt.restore(
                entity.getMatchAttemptId(),
                entity.getIncomingTransactionId(),
                entity.getWatchAccountId(),
                MatchingResult.valueOf(entity.getResult()),
                snapshots,
                entity.getAttemptedAt()
        );
    }

    public TriggerEventEntity toEntity(TriggerEvent event) {
        TriggerEventEntity entity = new TriggerEventEntity();
        entity.setTriggerEventId(event.getTriggerEventID());
        entity.setMatchAttemptId(event.getMatchAttemptID());
        entity.setExternalUserScenarioId(event.getExternalUserScenarioID());
        entity.setExternalUserId(event.getExternalUserID());
        entity.setDeliveryStatus(event.getDeliveryStatus().name());
        entity.setRetryCount(event.getRetryCount());
        entity.setScheduledAt(event.getScheduledAt());
        entity.setDeliveredAt(event.getDeliveredAt());
        entity.setLastRetryAt(event.getLastRetryAt());

        TriggerPayload payload = event.getPayload();
        entity.setTriggerTransactionId(payload.triggerTransactionID());
        entity.setMatchedMcc(payload.matchedMCC());
        entity.setMatchedMerchantName(payload.matchedMerchantName());
        entity.setMatchedAmount(payload.matchedAmount());
        entity.setMatchedCurrency(payload.matchedCurrency());
        entity.setScenarioTypeCode(payload.scenarioTypeCode());
        entity.setOccuredAt(payload.occuredAt());
        applyDebitConfig(entity, payload.debitConfig());

        List<DeliveryAttemptEntity> attemptEntities = new ArrayList<>();
        for (DeliveryAttempt attempt : event.getDeliveryAttempts()) {
            DeliveryAttemptEntity attemptEntity = new DeliveryAttemptEntity();
            attemptEntity.setId(UUID.randomUUID());
            attemptEntity.setTriggerEvent(entity);
            attemptEntity.setAttemptNumber(attempt.attemptNumber());
            attemptEntity.setAttemptedAt(attempt.attemptedAt());
            attemptEntity.setSuccess(attempt.success());
            attemptEntity.setHttpStatus(attempt.httpStatus());
            attemptEntity.setErrorMessage(attempt.errorMessage());
            attemptEntities.add(attemptEntity);
        }
        entity.setDeliveryAttempts(attemptEntities);
        return entity;
    }

    public TriggerEvent toDomain(TriggerEventEntity entity) {
        TriggerPayload payload = new TriggerPayload(
                entity.getTriggerTransactionId(),
                entity.getMatchedMcc(),
                entity.getMatchedMerchantName(),
                entity.getMatchedAmount(),
                entity.getMatchedCurrency(),
                entity.getScenarioTypeCode(),
                readDebitConfig(entity),
                entity.getOccuredAt()
        );
        List<DeliveryAttempt> attempts = entity.getDeliveryAttempts().stream()
                .map(attempt -> new DeliveryAttempt(
                        attempt.getAttemptNumber(),
                        attempt.getAttemptedAt(),
                        attempt.isSuccess(),
                        attempt.getHttpStatus() != null ? attempt.getHttpStatus() : 0,
                        attempt.getErrorMessage()))
                .toList();
        return TriggerEvent.restore(
                entity.getTriggerEventId(),
                entity.getMatchAttemptId(),
                entity.getExternalUserScenarioId(),
                entity.getExternalUserId(),
                TriggerEventDeliveryStatus.valueOf(entity.getDeliveryStatus()),
                payload,
                attempts,
                entity.getRetryCount(),
                entity.getScheduledAt(),
                entity.getDeliveredAt(),
                entity.getLastRetryAt()
        );
    }

    private ActiveRuleEntity toRuleEntity(ActiveRule rule) {
        ActiveRuleEntity entity = new ActiveRuleEntity();
        entity.setActiveRuleId(rule.getActiveRuleID());
        entity.setExternalUserScenarioId(rule.getExternalUserScenarioID());
        entity.setScenarioTypeCode(rule.getScenarioTypeCode());
        entity.setVersion(rule.getVersion());
        entity.setEnabled(rule.isEnabled());
        entity.setRegisteredAt(rule.getRegisteredAt());
        entity.setUpdatedAt(rule.getUpdatedAt());

        List<RuleConditionEntity> conditionEntities = new ArrayList<>();
        for (RuleCondition condition : rule.getConditions()) {
            RuleConditionEntity conditionEntity = new RuleConditionEntity();
            conditionEntity.setConditionId(condition.conditionID());
            conditionEntity.setActiveRule(entity);
            conditionEntity.setField(condition.field().name());
            conditionEntity.setOperator(condition.operator().name());
            conditionEntity.setValue(condition.value());
            conditionEntities.add(conditionEntity);
        }
        entity.setConditions(conditionEntities);
        return entity;
    }

    private ActiveRule toRuleDomain(ActiveRuleEntity entity) {
        List<RuleCondition> conditions = entity.getConditions().stream()
                .map(condition -> new RuleCondition(
                        condition.getConditionId(),
                        RuleField.valueOf(condition.getField()),
                        RuleOperator.valueOf(condition.getOperator()),
                        condition.getValue()))
                .toList();
        return new ActiveRule(
                entity.getActiveRuleId(),
                entity.getExternalUserScenarioId(),
                entity.getScenarioTypeCode(),
                entity.getVersion(),
                entity.isEnabled(),
                conditions,
                entity.getRegisteredAt(),
                entity.getUpdatedAt()
        );
    }

    private static void applyDebitConfig(WatchProfileEntity entity, DebitConfig debitConfig) {
        if (debitConfig == null) {
            return;
        }
        entity.setDebitAmount(debitConfig.debitAmount());
        entity.setDebitCurrency(debitConfig.currency());
        entity.setRecipientPaymentToken(debitConfig.recipientPaymentToken());
        entity.setConsentId(debitConfig.consentId());
        entity.setSourceAccountId(debitConfig.sourceAccountId());
    }

    private static void applyDebitConfig(TriggerEventEntity entity, DebitConfig debitConfig) {
        if (debitConfig == null) {
            return;
        }
        entity.setDebitAmount(debitConfig.debitAmount());
        entity.setDebitCurrency(debitConfig.currency());
        entity.setRecipientPaymentToken(debitConfig.recipientPaymentToken());
        entity.setConsentId(debitConfig.consentId());
        entity.setSourceAccountId(debitConfig.sourceAccountId());
    }

    private static DebitConfig readDebitConfig(WatchProfileEntity entity) {
        if (entity.getDebitAmount() == null && entity.getDebitCurrency() == null) {
            return null;
        }
        return new DebitConfig(
                entity.getDebitAmount(),
                entity.getDebitCurrency(),
                entity.getRecipientPaymentToken(),
                entity.getConsentId(),
                entity.getSourceAccountId()
        );
    }

    private static DebitConfig readDebitConfig(TriggerEventEntity entity) {
        if (entity.getDebitAmount() == null && entity.getDebitCurrency() == null) {
            return null;
        }
        return new DebitConfig(
                entity.getDebitAmount(),
                entity.getDebitCurrency(),
                entity.getRecipientPaymentToken(),
                entity.getConsentId(),
                entity.getSourceAccountId()
        );
    }
}
