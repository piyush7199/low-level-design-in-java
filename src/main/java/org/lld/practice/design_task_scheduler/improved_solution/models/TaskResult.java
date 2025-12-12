package org.lld.practice.design_task_scheduler.improved_solution.models;

import java.time.Duration;
import java.time.Instant;

/**
 * Result of task execution.
 */
public class TaskResult {
    
    private final String taskId;
    private final boolean success;
    private final Instant executedAt;
    private final Duration executionTime;
    private final String errorMessage;
    private final int attemptNumber;

    private TaskResult(String taskId, boolean success, Instant executedAt,
                       Duration executionTime, String errorMessage, int attemptNumber) {
        this.taskId = taskId;
        this.success = success;
        this.executedAt = executedAt;
        this.executionTime = executionTime;
        this.errorMessage = errorMessage;
        this.attemptNumber = attemptNumber;
    }

    public static TaskResult success(String taskId, Instant executedAt, 
                                     Duration executionTime, int attemptNumber) {
        return new TaskResult(taskId, true, executedAt, executionTime, null, attemptNumber);
    }

    public static TaskResult failure(String taskId, Instant executedAt,
                                     Duration executionTime, String error, int attemptNumber) {
        return new TaskResult(taskId, false, executedAt, executionTime, error, attemptNumber);
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean isSuccess() {
        return success;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    @Override
    public String toString() {
        if (success) {
            return String.format("TaskResult{taskId='%s', SUCCESS, time=%dms, attempt=%d}",
                    taskId, executionTime.toMillis(), attemptNumber);
        } else {
            return String.format("TaskResult{taskId='%s', FAILED, error='%s', attempt=%d}",
                    taskId, errorMessage, attemptNumber);
        }
    }
}

