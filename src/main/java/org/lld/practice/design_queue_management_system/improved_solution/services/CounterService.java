package org.lld.practice.design_queue_management_system.improved_solution.services;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.CounterStatus;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.*;

/**
 * Service responsible for counter management.
 * Handles counter registration, status updates, and availability queries.
 */
public class CounterService {
    
    private final Map<String, Counter> counters = new LinkedHashMap<>();

    /**
     * Register a new counter in the system.
     */
    public void addCounter(Counter counter) {
        Objects.requireNonNull(counter, "Counter cannot be null");
        counters.put(counter.getCounterId(), counter);
    }

    /**
     * Open a counter for service.
     */
    public void openCounter(String counterId, String operatorName) {
        Counter counter = getCounterOrThrow(counterId);
        counter.open(operatorName);
        System.out.printf("🟢 Counter %s opened by %s%n", counterId, operatorName);
    }

    /**
     * Close a counter.
     */
    public void closeCounter(String counterId) {
        Counter counter = getCounterOrThrow(counterId);
        counter.close();
        System.out.printf("🔴 Counter %s closed%n", counterId);
    }

    /**
     * Get a counter by ID.
     */
    public Optional<Counter> getCounter(String counterId) {
        return Optional.ofNullable(counters.get(counterId));
    }

    /**
     * Get all available counters that can serve a specific token type.
     */
    public List<Counter> getAvailableCounters(TokenType tokenType) {
        return counters.values().stream()
                .filter(counter -> counter.getStatus() == CounterStatus.AVAILABLE)
                .filter(counter -> counter.canServe(tokenType))
                .toList();
    }

    /**
     * Get all available counters.
     */
    public List<Counter> getAllAvailableCounters() {
        return counters.values().stream()
                .filter(Counter::isAvailable)
                .toList();
    }

    /**
     * Get all counters.
     */
    public List<Counter> getAllCounters() {
        return new ArrayList<>(counters.values());
    }

    /**
     * Check if any counter is available for a token type.
     */
    public boolean hasAvailableCounter(TokenType tokenType) {
        return counters.values().stream()
                .anyMatch(counter -> counter.isAvailable() && counter.canServe(tokenType));
    }

    private Counter getCounterOrThrow(String counterId) {
        Counter counter = counters.get(counterId);
        if (counter == null) {
            throw new IllegalArgumentException("Counter not found: " + counterId);
        }
        return counter;
    }
}

