package org.lld.practice.design_logger_system.improved_solution.filters;

import org.lld.practice.design_logger_system.improved_solution.models.LogMessage;

import java.util.Set;

/**
 * Filter that filters logs based on source.
 */
public class SourceFilter implements LogFilter {
    private final Set<String> allowedSources;
    
    public SourceFilter(Set<String> allowedSources) {
        this.allowedSources = allowedSources;
    }
    
    @Override
    public boolean shouldLog(LogMessage logMessage) {
        return allowedSources.isEmpty() || allowedSources.contains(logMessage.getSource());
    }
}

