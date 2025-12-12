package org.lld.practice.design_task_scheduler.improved_solution.models;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Represents a scheduled task.
 */
public class Task implements Comparable<Task> {
    
    private final String taskId;
    private final String name;
    private final Runnable command;
    private final TaskPriority priority;
    private final ScheduleType scheduleType;
    private final Duration interval;
    private final RetryPolicy retryPolicy;
    private final Instant createdAt;
    
    private Instant scheduledTime;
    private TaskStatus status;
    private int attemptCount;
    private Instant lastExecutedAt;
    private String lastError;

    private Task(Builder builder) {
        this.taskId = "TASK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.name = builder.name;
        this.command = builder.command;
        this.priority = builder.priority;
        this.scheduleType = builder.scheduleType;
        this.scheduledTime = builder.scheduledTime;
        this.interval = builder.interval;
        this.retryPolicy = builder.retryPolicy;
        this.createdAt = Instant.now();
        this.status = TaskStatus.PENDING;
        this.attemptCount = 0;
    }

    // ========== Execution ==========

    /**
     * Execute the task and return result.
     */
    public TaskResult execute() {
        Instant startTime = Instant.now();
        attemptCount++;
        status = TaskStatus.RUNNING;
        
        try {
            command.run();
            lastExecutedAt = Instant.now();
            Duration executionTime = Duration.between(startTime, lastExecutedAt);
            status = TaskStatus.COMPLETED;
            lastError = null;
            
            return TaskResult.success(taskId, startTime, executionTime, attemptCount);
            
        } catch (Exception e) {
            lastExecutedAt = Instant.now();
            Duration executionTime = Duration.between(startTime, lastExecutedAt);
            lastError = e.getMessage();
            
            if (retryPolicy.shouldRetry(attemptCount)) {
                status = TaskStatus.RETRYING;
            } else {
                status = TaskStatus.FAILED;
            }
            
            return TaskResult.failure(taskId, startTime, executionTime, e.getMessage(), attemptCount);
        }
    }

    /**
     * Reschedule task for next execution (for recurring tasks).
     */
    public void rescheduleForNextRun() {
        if (scheduleType == ScheduleType.ONE_TIME) {
            return;
        }
        
        if (scheduleType == ScheduleType.FIXED_RATE) {
            // Schedule from original time + interval
            scheduledTime = scheduledTime.plus(interval);
        } else if (scheduleType == ScheduleType.FIXED_DELAY) {
            // Schedule from completion time + interval
            scheduledTime = Instant.now().plus(interval);
        }
        
        status = TaskStatus.PENDING;
    }

    /**
     * Schedule for retry with backoff delay.
     */
    public void scheduleRetry() {
        Duration retryDelay = retryPolicy.getDelayForAttempt(attemptCount);
        scheduledTime = Instant.now().plus(retryDelay);
        status = TaskStatus.PENDING;
    }

    /**
     * Cancel the task.
     */
    public void cancel() {
        if (status != TaskStatus.RUNNING) {
            status = TaskStatus.CANCELLED;
        }
    }

    // ========== Getters ==========

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public Duration getInterval() {
        return interval;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public Instant getLastExecutedAt() {
        return lastExecutedAt;
    }

    public String getLastError() {
        return lastError;
    }

    public boolean isRecurring() {
        return scheduleType != ScheduleType.ONE_TIME;
    }

    public boolean isReadyToRun() {
        return status == TaskStatus.PENDING && 
               !scheduledTime.isAfter(Instant.now());
    }

    public boolean canRetry() {
        return status == TaskStatus.RETRYING && 
               retryPolicy.shouldRetry(attemptCount);
    }

    // ========== Comparable ==========

    @Override
    public int compareTo(Task other) {
        // First by scheduled time (earlier first)
        int timeCompare = this.scheduledTime.compareTo(other.scheduledTime);
        if (timeCompare != 0) return timeCompare;
        
        // Then by priority (higher value = more important = first)
        return Integer.compare(other.priority.getValue(), this.priority.getValue());
    }

    @Override
    public String toString() {
        return String.format("Task{id='%s', name='%s', priority=%s, status=%s, scheduled=%s}",
                taskId, name, priority, status, scheduledTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    // ========== Builder ==========

    public static Builder builder(String name, Runnable command) {
        return new Builder(name, command);
    }

    public static class Builder {
        private final String name;
        private final Runnable command;
        private TaskPriority priority = TaskPriority.NORMAL;
        private ScheduleType scheduleType = ScheduleType.ONE_TIME;
        private Instant scheduledTime = Instant.now();
        private Duration interval = Duration.ZERO;
        private RetryPolicy retryPolicy = RetryPolicy.DEFAULT;

        public Builder(String name, Runnable command) {
            this.name = Objects.requireNonNull(name);
            this.command = Objects.requireNonNull(command);
        }

        public Builder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder scheduleAt(Instant time) {
            this.scheduledTime = time;
            return this;
        }

        public Builder delay(Duration delay) {
            this.scheduledTime = Instant.now().plus(delay);
            return this;
        }

        public Builder fixedRate(Duration interval) {
            this.scheduleType = ScheduleType.FIXED_RATE;
            this.interval = interval;
            return this;
        }

        public Builder fixedDelay(Duration interval) {
            this.scheduleType = ScheduleType.FIXED_DELAY;
            this.interval = interval;
            return this;
        }

        public Builder retryPolicy(RetryPolicy policy) {
            this.retryPolicy = policy;
            return this;
        }

        public Builder noRetry() {
            this.retryPolicy = RetryPolicy.NO_RETRY;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}

