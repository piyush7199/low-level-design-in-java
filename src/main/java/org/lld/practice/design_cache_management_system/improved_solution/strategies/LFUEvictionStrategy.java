package org.lld.practice.design_cache_management_system.improved_solution.strategies;

import java.util.*;

/**
 * Least Frequently Used (LFU) eviction strategy.
 * 
 * Uses three data structures for O(1) operations:
 * - HashMap<K, Node>: O(1) lookup of nodes by key
 * - HashMap<Integer, LinkedHashSet<K>>: Groups keys by frequency
 * - minFrequency: Tracks minimum frequency for O(1) eviction
 * 
 * When frequencies are equal, LRU behavior is used (oldest in frequency group).
 *
 * @param <K> Type of cache keys
 */
public class LFUEvictionStrategy<K> implements EvictionStrategy<K> {
    
    private final Map<K, Integer> keyFrequency = new HashMap<>();
    private final Map<Integer, LinkedHashSet<K>> frequencyKeys = new HashMap<>();
    private int minFrequency = 0;

    @Override
    public void recordAccess(K key) {
        if (!keyFrequency.containsKey(key)) {
            return;
        }
        
        int oldFreq = keyFrequency.get(key);
        int newFreq = oldFreq + 1;
        
        // Update frequency
        keyFrequency.put(key, newFreq);
        
        // Remove from old frequency set
        LinkedHashSet<K> oldFreqSet = frequencyKeys.get(oldFreq);
        oldFreqSet.remove(key);
        
        // Update minFrequency if necessary
        if (oldFreqSet.isEmpty()) {
            frequencyKeys.remove(oldFreq);
            if (minFrequency == oldFreq) {
                minFrequency = newFreq;
            }
        }
        
        // Add to new frequency set
        frequencyKeys.computeIfAbsent(newFreq, k -> new LinkedHashSet<>()).add(key);
    }

    @Override
    public void recordInsertion(K key) {
        if (keyFrequency.containsKey(key)) {
            // Key exists, just update access
            recordAccess(key);
            return;
        }
        
        // New key starts with frequency 1
        keyFrequency.put(key, 1);
        frequencyKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFrequency = 1;
    }

    @Override
    public Optional<K> getEvictionCandidate() {
        if (keyFrequency.isEmpty()) {
            return Optional.empty();
        }
        
        LinkedHashSet<K> minFreqSet = frequencyKeys.get(minFrequency);
        if (minFreqSet == null || minFreqSet.isEmpty()) {
            return Optional.empty();
        }
        
        // Return first key in min frequency set (oldest with min frequency)
        return Optional.of(minFreqSet.iterator().next());
    }

    @Override
    public void remove(K key) {
        Integer freq = keyFrequency.remove(key);
        if (freq != null) {
            LinkedHashSet<K> freqSet = frequencyKeys.get(freq);
            if (freqSet != null) {
                freqSet.remove(key);
                if (freqSet.isEmpty()) {
                    frequencyKeys.remove(freq);
                    // Update minFrequency if we removed from it
                    if (freq == minFrequency && !frequencyKeys.isEmpty()) {
                        minFrequency = frequencyKeys.keySet().stream()
                                .min(Integer::compareTo)
                                .orElse(0);
                    }
                }
            }
        }
    }

    @Override
    public void clear() {
        keyFrequency.clear();
        frequencyKeys.clear();
        minFrequency = 0;
    }

    @Override
    public int size() {
        return keyFrequency.size();
    }

    /**
     * Get the frequency of a key.
     */
    public int getFrequency(K key) {
        return keyFrequency.getOrDefault(key, 0);
    }
}

