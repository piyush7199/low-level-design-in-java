package org.lld.practice.design_queue_management_system.improved_solution.models;

/**
 * Enum representing different types of tokens with their priority levels.
 * Higher priority value means the token should be served first.
 */
public enum TokenType {
    REGULAR("R", 1),
    SENIOR_CITIZEN("S", 2),
    PRIORITY("P", 3),
    VIP("V", 4);

    private final String prefix;
    private final int priorityLevel;

    TokenType(String prefix, int priorityLevel) {
        this.prefix = prefix;
        this.priorityLevel = priorityLevel;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }
}

