package org.lld.practice.design_cache_management_system.naive_solution;

import java.util.HashMap;
import java.util.Map;

/**
 * Naive implementation of a Cache.
 * 
 * This demonstrates common anti-patterns:
 * - No real eviction policy (random eviction)
 * - O(n) operations for finding eviction candidate
 * - No TTL support
 * - Not thread-safe
 * - No statistics tracking
 * - Violates Open/Closed Principle
 * 
 * DO NOT use this pattern in production!
 */
public class SimpleCache<K, V> {
    
    private final Map<K, V> cache = new HashMap<>();
    private final int maxSize;
    
    public SimpleCache(int maxSize) {
        this.maxSize = maxSize;
    }
    
    /**
     * Get value from cache.
     * 
     * Problems:
     * - No access tracking for LRU/LFU
     * - No TTL checking
     * - No hit/miss statistics
     */
    public V get(K key) {
        return cache.get(key);
    }
    
    /**
     * Put value in cache.
     * 
     * Problems:
     * - Random eviction (just removes first key found)
     * - O(n) to find first key via iterator
     * - Not thread-safe
     */
    public void put(K key, V value) {
        if (cache.size() >= maxSize && !cache.containsKey(key)) {
            // BAD: Random eviction - just removes any entry
            // HashMap doesn't guarantee order, so this is unpredictable
            K keyToRemove = cache.keySet().iterator().next();
            cache.remove(keyToRemove);
            System.out.println("⚠️ Evicted key (random): " + keyToRemove);
        }
        cache.put(key, value);
    }
    
    /**
     * Remove value from cache.
     */
    public V remove(K key) {
        return cache.remove(key);
    }
    
    /**
     * Get current size.
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Clear all entries.
     */
    public void clear() {
        cache.clear();
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) {
        System.out.println("=== Naive Cache Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        SimpleCache<String, String> cache = new SimpleCache<>(3);
        
        // Add entries
        System.out.println("Adding entries...");
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");
        
        System.out.println("Cache size: " + cache.size());
        
        // Access some entries (not tracked!)
        System.out.println("\nAccessing user:1 multiple times (but cache doesn't track this!)");
        cache.get("user:1");
        cache.get("user:1");
        cache.get("user:1");
        
        // Add new entry - causes eviction
        System.out.println("\nAdding user:4 (will trigger eviction)...");
        cache.put("user:4", "Diana");
        
        // Check what was evicted
        System.out.println("\nChecking cache contents:");
        System.out.println("user:1 = " + cache.get("user:1"));  // Might be null!
        System.out.println("user:2 = " + cache.get("user:2"));
        System.out.println("user:3 = " + cache.get("user:3"));
        System.out.println("user:4 = " + cache.get("user:4"));
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. user:1 was accessed 3 times but might still be evicted");
        System.out.println("2. No LRU/LFU policy - eviction is essentially random");
        System.out.println("3. No TTL - entries never expire");
        System.out.println("4. No statistics - can't measure cache effectiveness");
        System.out.println("5. Not thread-safe for concurrent access");
    }
}

