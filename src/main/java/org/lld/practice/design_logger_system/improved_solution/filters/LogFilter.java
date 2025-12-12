package org.lld.practice.design_logger_system.improved_solution.filters;

import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * Interface for filtering log messages.
 * Chain of Responsibility pattern: Multiple filters can be chained together.
 */
public interface LogFilter {
    /**
     * Determines if a log message should be logged.
     * 
     * @param logMessage The log message to check
     * @return true if the message should be logged, false otherwise
     */
    boolean shouldLog(LogMessage logMessage);
}

