package org.lld.practice.design_logger_system.improved_solution.formatters;

import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * Strategy interface for formatting log messages.
 * Different implementations can format logs as JSON, XML, Plain Text, etc.
 */
public interface LogFormatter {
    /**
     * Formats a log message into a string representation.
     * 
     * @param logMessage The log message to format
     * @return Formatted string representation
     */
    String format(LogMessage logMessage);
}

