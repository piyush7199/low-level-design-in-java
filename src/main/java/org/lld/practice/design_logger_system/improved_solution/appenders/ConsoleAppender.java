package org.lld.practice.design_logger_system.improved_solution.appenders;

import org.lld.practice.design_logger_system.improved_solution.formatters.LogFormatter;
import org.lld.practice.design_logger_system.improved_solution.formatters.PlainTextFormatter;
import org.lld.practice.design_logger_system.improved_solution.models.LogLevel;
import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * Appender that writes logs to the console (System.out/System.err).
 */
public class ConsoleAppender implements LogAppender {
    private final LogFormatter formatter;
    private LogLevel minimumLevel;
    
    public ConsoleAppender(LogLevel minimumLevel) {
        this(minimumLevel, new PlainTextFormatter());
    }
    
    public ConsoleAppender(LogLevel minimumLevel, LogFormatter formatter) {
        this.minimumLevel = minimumLevel;
        this.formatter = formatter;
    }
    
    @Override
    public void append(LogMessage logMessage) {
        if (!logMessage.getLevel().shouldLog(minimumLevel)) {
            return;
        }
        
        String formattedMessage = formatter.format(logMessage);
        
        // Write ERROR and FATAL to stderr, others to stdout
        if (logMessage.getLevel() == LogLevel.ERROR || logMessage.getLevel() == LogLevel.FATAL) {
            System.err.println(formattedMessage);
        } else {
            System.out.println(formattedMessage);
        }
    }
    
    @Override
    public LogLevel getMinimumLevel() {
        return minimumLevel;
    }
    
    @Override
    public void setMinimumLevel(LogLevel level) {
        this.minimumLevel = level;
    }
    
    @Override
    public void close() {
        // Console appender doesn't need cleanup
    }
}

