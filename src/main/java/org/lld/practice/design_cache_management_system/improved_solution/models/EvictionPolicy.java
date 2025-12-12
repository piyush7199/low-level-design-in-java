package org.lld.practice.design_cache_management_system.improved_solution.models;

/**
 * Enum representing different cache eviction policies.
 */
public enum EvictionPolicy {
    /**
     * Least Recently Used - evicts the item that hasn't been accessed for the longest time.
     * Best for: Temporal locality patterns where recent access predicts future access.
     */
    LRU,
    
    /**
     * Least Frequently Used - evicts the item with the lowest access count.
     * Best for: Frequency patterns where popular items should stay cached.
     */
    LFU,
    
    /**
     * First In First Out - evicts the oldest inserted item.
     * Best for: Simple scenarios where age matters more than access patterns.
     */
    FIFO
}

