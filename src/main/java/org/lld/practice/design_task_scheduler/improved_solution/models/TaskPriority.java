package org.lld.practice.design_task_scheduler.improved_solution.models;

/**
 * Enum representing task priority levels.
 */
public enum TaskPriority {
    LOW(1),
    NORMAL(5),
    HIGH(10),
    CRITICAL(20);

    private final int value;

    TaskPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

