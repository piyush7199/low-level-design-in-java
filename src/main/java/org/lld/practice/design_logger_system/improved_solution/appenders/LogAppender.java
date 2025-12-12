package org.lld.practice.design_logger_system.improved_solution.appenders;

import org.lld.practice.design_logger_system.improved_solution.models.LogLevel;
import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * Interface for log appenders that write logs to different destinations.
 * Strategy pattern: Different implementations handle different output destinations.
 */
public interface LogAppender {
    /**
     * Appends a log message to the destination.
     * 
     * @param logMessage The log message to append
     */
    void append(LogMessage logMessage);
    
    /**
     * Gets the minimum log level for this appender.
     * 
     * @return Minimum log level
     */
    LogLevel getMinimumLevel();
    
    /**
     * Sets the minimum log level for this appender.
     * 
     * @param level Minimum log level
     */
    void setMinimumLevel(LogLevel level);
    
    /**
     * Closes the appender and releases resources.
     */
    void close();
}

