package org.lld.practice.design_cache_management_system.improved_solution.cache;

import org.lld.practice.design_cache_management_system.improved_solution.models.CacheStats;

import java.util.Optional;

/**
 * Interface for cache operations.
 * Defines the contract for all cache implementations.
 *
 * @param <K> Type of cache keys
 * @param <V> Type of cache values
 */
public interface Cache<K, V> {
    
    /**
     * Get a value from the cache.
     * 
     * @param key The key to look up
     * @return Optional containing the value if found and not expired, empty otherwise
     */
    Optional<V> get(K key);
    
    /**
     * Put a value in the cache.
     * May trigger eviction if cache is at capacity.
     * 
     * @param key The key
     * @param value The value to cache
     */
    void put(K key, V value);
    
    /**
     * Remove a value from the cache.
     * 
     * @param key The key to remove
     * @return Optional containing the removed value if it existed
     */
    Optional<V> remove(K key);
    
    /**
     * Check if key exists in cache.
     * Note: Does not update access statistics.
     * 
     * @param key The key to check
     * @return true if key exists and is not expired
     */
    boolean containsKey(K key);
    
    /**
     * Get current number of entries in cache.
     */
    int size();
    
    /**
     * Get maximum capacity of cache.
     */
    int capacity();
    
    /**
     * Check if cache is empty.
     */
    boolean isEmpty();
    
    /**
     * Clear all entries from cache.
     */
    void clear();
    
    /**
     * Get cache statistics.
     */
    CacheStats getStats();
}

