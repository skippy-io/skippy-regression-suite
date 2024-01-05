package com.example;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ExtensionThatShouldNotBeExecuted implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        System.err.println("ExtensionThatShouldNotBeExecuted was executed");
        return ConditionEvaluationResult.enabled("");
    }

}

