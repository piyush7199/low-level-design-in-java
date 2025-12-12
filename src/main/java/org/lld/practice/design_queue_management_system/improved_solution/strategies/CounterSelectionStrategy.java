package org.lld.practice.design_queue_management_system.improved_solution.strategies;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.List;
import java.util.Optional;

/**
 * Strategy interface for selecting which counter should serve a token.
 * Useful when multiple counters are available for a token type.
 */
public interface CounterSelectionStrategy {
    
    /**
     * Select the best available counter for a given token type.
     * 
     * @param availableCounters List of currently available counters
     * @param tokenType The type of token to be served
     * @return The selected counter, or empty if no suitable counter
     */
    Optional<Counter> selectCounter(List<Counter> availableCounters, TokenType tokenType);
}

