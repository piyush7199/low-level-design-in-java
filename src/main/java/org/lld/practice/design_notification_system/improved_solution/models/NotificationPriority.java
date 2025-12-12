package org.lld.practice.design_notification_system.improved_solution.models;

/**
 * Enum representing notification priority levels.
 */
public enum NotificationPriority {
    LOW(1),
    NORMAL(2),
    HIGH(3),
    URGENT(4);
    
    private final int value;
    
    NotificationPriority(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}

