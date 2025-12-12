package org.lld.practice.design_logger_system.improved_solution;

import org.lld.practice.design_logger_system.improved_solution.appenders.LogAppender;
import org.lld.practice.design_logger_system.improved_solution.filters.LogFilter;
import org.lld.practice.design_logger_system.improved_solution.models.LogLevel;
import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages logging operations, appenders, and filters.
 * Observer pattern: Notifies all registered appenders about log events.
 */
public class LogManager {
    private final List<LogAppender> appenders;
    private final List<LogFilter> filters;
    private LogLevel globalMinimumLevel;
    
    public LogManager(LogLevel globalMinimumLevel) {
        this.globalMinimumLevel = globalMinimumLevel;
        this.appenders = new CopyOnWriteArrayList<>(); // Thread-safe
        this.filters = new CopyOnWriteArrayList<>(); // Thread-safe
    }
    
    /**
     * Adds an appender to receive log messages.
     * 
     * @param appender The appender to add
     */
    public void addAppender(LogAppender appender) {
        appenders.add(appender);
    }
    
    /**
     * Removes an appender.
     * 
     * @param appender The appender to remove
     */
    public void removeAppender(LogAppender appender) {
        appenders.remove(appender);
    }
    
    /**
     * Adds a filter to the filter chain.
     * 
     * @param filter The filter to add
     */
    public void addFilter(LogFilter filter) {
        filters.add(filter);
    }
    
    /**
     * Logs a message if it passes all filters.
     * 
     * @param logMessage The log message to log
     */
    public void log(LogMessage logMessage) {
        // Check global minimum level
        if (!logMessage.getLevel().shouldLog(globalMinimumLevel)) {
            return;
        }
        
        // Apply all filters (Chain of Responsibility)
        for (LogFilter filter : filters) {
            if (!filter.shouldLog(logMessage)) {
                return;
            }
        }
        
        // Notify all appenders (Observer pattern)
        for (LogAppender appender : appenders) {
            appender.append(logMessage);
        }
    }
    
    /**
     * Sets the global minimum log level.
     * 
     * @param level The minimum log level
     */
    public void setGlobalMinimumLevel(LogLevel level) {
        this.globalMinimumLevel = level;
    }
    
    /**
     * Closes all appenders and releases resources.
     */
    public void shutdown() {
        for (LogAppender appender : appenders) {
            appender.close();
        }
        appenders.clear();
        filters.clear();
    }
}

