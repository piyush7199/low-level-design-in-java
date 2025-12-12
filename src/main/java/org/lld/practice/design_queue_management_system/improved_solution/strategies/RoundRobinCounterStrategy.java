package org.lld.practice.design_queue_management_system.improved_solution.strategies;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round-robin counter selection strategy.
 * 
 * Distributes tokens evenly across available counters
 * that can serve the given token type.
 */
public class RoundRobinCounterStrategy implements CounterSelectionStrategy {
    
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public Optional<Counter> selectCounter(List<Counter> availableCounters, TokenType tokenType) {
        if (availableCounters == null || availableCounters.isEmpty()) {
            return Optional.empty();
        }
        
        // Filter counters that can serve this token type
        List<Counter> eligibleCounters = availableCounters.stream()
                .filter(counter -> counter.canServe(tokenType))
                .filter(Counter::isAvailable)
                .toList();
        
        if (eligibleCounters.isEmpty()) {
            return Optional.empty();
        }
        
        // Round-robin selection
        int index = currentIndex.getAndUpdate(i -> (i + 1) % eligibleCounters.size());
        return Optional.of(eligibleCounters.get(index % eligibleCounters.size()));
    }
}

