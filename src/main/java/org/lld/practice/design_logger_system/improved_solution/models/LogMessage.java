package org.lld.practice.design_logger_system.improved_solution.models;

import java.time.LocalDateTime;

/**
 * Represents a log message with all its metadata.
 */
public class LogMessage {
    private final LogLevel level;
    private final String message;
    private final LocalDateTime timestamp;
    private final String source;
    private final Throwable throwable;
    
    public LogMessage(LogLevel level, String message, String source) {
        this(level, message, source, null);
    }
    
    public LogMessage(LogLevel level, String message, String source, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.source = source;
        this.throwable = throwable;
        this.timestamp = LocalDateTime.now();
    }
    
    public LogLevel getLevel() {
        return level;
    }
    
    public String getMessage() {
        return message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getSource() {
        return source;
    }
    
    public Throwable getThrowable() {
        return throwable;
    }
}

