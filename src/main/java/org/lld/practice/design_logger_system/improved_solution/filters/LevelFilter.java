package org.lld.practice.design_logger_system.improved_solution.filters;

import org.lld.practice.design_logger_system.improved_solution.models.LogLevel;
import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * Filter that filters logs based on log level.
 */
public class LevelFilter implements LogFilter {
    private final LogLevel minimumLevel;
    
    public LevelFilter(LogLevel minimumLevel) {
        this.minimumLevel = minimumLevel;
    }
    
    @Override
    public boolean shouldLog(LogMessage logMessage) {
        return logMessage.getLevel().shouldLog(minimumLevel);
    }
}

