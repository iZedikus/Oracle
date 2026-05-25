package ru.stepanov.oracle.domain.model.matchattempt;

public record ConditionSnapshot(String expectedValue, String actualValue, boolean passed) {
}
