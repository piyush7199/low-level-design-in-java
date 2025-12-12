package org.lld.practice.design_cache_management_system.improved_solution.strategies;

import java.util.Optional;

/**
 * Strategy interface for cache eviction policies.
 * Implements the Strategy pattern to allow different eviction algorithms.
 *
 * @param <K> Type of cache keys
 */
public interface EvictionStrategy<K> {
    
    /**
     * Record that a key was accessed (get operation).
     * Used for LRU/LFU tracking.
     */
    void recordAccess(K key);
    
    /**
     * Record that a new key was inserted.
     */
    void recordInsertion(K key);
    
    /**
     * Get the key that should be evicted next.
     * Returns empty if no candidates available.
     */
    Optional<K> getEvictionCandidate();
    
    /**
     * Remove a key from tracking (when explicitly removed from cache).
     */
    void remove(K key);
    
    /**
     * Clear all tracking data.
     */
    void clear();
    
    /**
     * Get current number of tracked keys.
     */
    int size();
}

