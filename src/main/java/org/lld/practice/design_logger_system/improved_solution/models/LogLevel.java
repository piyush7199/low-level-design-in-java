package org.lld.practice.design_logger_system.improved_solution.models;

/**
 * Enum representing different log levels with hierarchy.
 * Lower ordinal values represent higher priority.
 */
public enum LogLevel {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    FATAL(4);
    
    private final int priority;
    
    LogLevel(int priority) {
        this.priority = priority;
    }
    
    public int getPriority() {
        return priority;
    }
    
    /**
     * Checks if this log level should be logged given a minimum level.
     * 
     * @param minimumLevel The minimum log level to accept
     * @return true if this level should be logged
     */
    public boolean shouldLog(LogLevel minimumLevel) {
        return this.priority >= minimumLevel.priority;
    }
}

