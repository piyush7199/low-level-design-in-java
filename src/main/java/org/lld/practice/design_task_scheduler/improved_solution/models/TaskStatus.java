package org.lld.practice.design_task_scheduler.improved_solution.models;

/**
 * Enum representing task execution status.
 */
public enum TaskStatus {
    PENDING,        // Waiting to be executed
    RUNNING,        // Currently executing
    COMPLETED,      // Successfully completed
    FAILED,         // Execution failed (after all retries)
    CANCELLED,      // Cancelled by user
    RETRYING        // Failed, waiting for retry
}

