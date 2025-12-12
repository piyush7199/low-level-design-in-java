package org.lld.practice.design_logger_system.improved_solution.formatters;

import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

/**
 * JSON formatter for log messages.
 */
public class JSONFormatter implements LogFormatter {
    
    @Override
    public String format(LogMessage logMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
          .append("\"timestamp\":\"").append(logMessage.getTimestamp()).append("\",")
          .append("\"level\":\"").append(logMessage.getLevel()).append("\",")
          .append("\"source\":\"").append(logMessage.getSource()).append("\",")
          .append("\"message\":\"").append(escapeJson(logMessage.getMessage())).append("\"");
        
        if (logMessage.getThrowable() != null) {
            sb.append(",\"exception\":\"").append(escapeJson(logMessage.getThrowable().getMessage())).append("\"");
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    private String escapeJson(String str) {
        return str.replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}

