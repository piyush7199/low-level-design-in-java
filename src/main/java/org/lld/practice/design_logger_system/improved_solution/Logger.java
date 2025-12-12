package org.lld.practice.design_logger_system.improved_solution;

import org.lld.practice.design_logger_system.improved_solution.models.LogLevel;
import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * Main logger class that provides a simple interface for logging.
 * Singleton pattern: Provides a single global logger instance.
 */
public class Logger {
    private static Logger instance;
    private final LogManager logManager;
    private final String source;
    
    private Logger(String source, LogManager logManager) {
        this.source = source;
        this.logManager = logManager;
    }
    
    /**
     * Gets or creates the singleton logger instance.
     * 
     * @param source The source identifier for this logger
     * @param logManager The log manager to use
     * @return The logger instance
     */
    public static synchronized Logger getInstance(String source, LogManager logManager) {
        if (instance == null) {
            instance = new Logger(source, logManager);
        }
        return instance;
    }
    
    /**
     * Gets a logger instance for a specific source.
     * 
     * @param source The source identifier
     * @return A new logger instance for the source
     */
    public static Logger getLogger(String source, LogManager logManager) {
        return new Logger(source, logManager);
    }
    
    public void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }
    
    public void info(String message) {
        log(LogLevel.INFO, message, null);
    }
    
    public void warn(String message) {
        log(LogLevel.WARN, message, null);
    }
    
    public void error(String message) {
        log(LogLevel.ERROR, message, null);
    }
    
    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }
    
    public void fatal(String message) {
        log(LogLevel.FATAL, message, null);
    }
    
    public void fatal(String message, Throwable throwable) {
        log(LogLevel.FATAL, message, throwable);
    }
    
    private void log(LogLevel level, String message, Throwable throwable) {
        LogMessage logMessage = new LogMessage(level, message, source, throwable);
        logManager.log(logMessage);
    }
}

