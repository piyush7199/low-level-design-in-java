package org.lld.practice.design_logger_system.improved_solution.formatters;

import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * Plain text formatter for log messages.
 */
public class PlainTextFormatter implements LogFormatter {
    
    @Override
    public String format(LogMessage logMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(logMessage.getTimestamp())
          .append(" [")
          .append(logMessage.getLevel())
          .append("] ")
          .append(logMessage.getSource())
          .append(" - ")
          .append(logMessage.getMessage());
        
        if (logMessage.getThrowable() != null) {
            sb.append("\nException: ").append(logMessage.getThrowable().getMessage());
        }
        
        return sb.toString();
    }
}

