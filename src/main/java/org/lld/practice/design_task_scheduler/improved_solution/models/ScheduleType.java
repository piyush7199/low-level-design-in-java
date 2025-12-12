package org.lld.practice.design_task_scheduler.improved_solution.models;

/**
 * Enum representing task scheduling types.
 */
public enum ScheduleType {
    ONE_TIME,       // Execute once at scheduled time
    FIXED_RATE,     // Execute at fixed intervals from start time
    FIXED_DELAY     // Execute with fixed delay after completion
}

