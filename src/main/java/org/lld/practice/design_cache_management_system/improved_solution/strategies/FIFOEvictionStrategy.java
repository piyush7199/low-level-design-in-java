package org.lld.practice.design_cache_management_system.improved_solution.strategies;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * First In First Out (FIFO) eviction strategy.
 * 
 * Uses a Queue (LinkedList) for O(1) operations:
 * - Insertion at tail: O(1)
 * - Eviction from head: O(1)
 * 
 * Access order doesn't affect eviction - only insertion order matters.
 *
 * @param <K> Type of cache keys
 */
public class FIFOEvictionStrategy<K> implements EvictionStrategy<K> {
    
    private final Queue<K> insertionOrder = new LinkedList<>();
    private final Set<K> keySet = new HashSet<>();

    @Override
    public void recordAccess(K key) {
        // FIFO doesn't care about access - only insertion order
        // No-op
    }

    @Override
    public void recordInsertion(K key) {
        if (keySet.contains(key)) {
            // Key already exists, don't add again
            return;
        }
        
        insertionOrder.offer(key);
        keySet.add(key);
    }

    @Override
    public Optional<K> getEvictionCandidate() {
        // Return the oldest inserted key
        return Optional.ofNullable(insertionOrder.peek());
    }

    @Override
    public void remove(K key) {
        if (keySet.remove(key)) {
            insertionOrder.remove(key);  // O(n) but rare operation
        }
    }

    @Override
    public void clear() {
        insertionOrder.clear();
        keySet.clear();
    }

    @Override
    public int size() {
        return keySet.size();
    }
}

