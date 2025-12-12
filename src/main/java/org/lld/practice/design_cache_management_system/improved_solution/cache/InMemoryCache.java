package org.lld.practice.design_cache_management_system.improved_solution.cache;

import org.lld.practice.design_cache_management_system.improved_solution.models.CacheEntry;
import org.lld.practice.design_cache_management_system.improved_solution.models.CacheStats;
import org.lld.practice.design_cache_management_system.improved_solution.strategies.EvictionStrategy;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory cache implementation with configurable eviction strategy.
 * 
 * Features:
 * - Configurable capacity
 * - Pluggable eviction strategy (LRU, LFU, FIFO)
 * - Optional TTL support
 * - Statistics tracking
 *
 * @param <K> Type of cache keys
 * @param <V> Type of cache values
 */
public class InMemoryCache<K, V> implements Cache<K, V> {
    
    private final Map<K, CacheEntry<V>> storage = new HashMap<>();
    private final EvictionStrategy<K> evictionStrategy;
    private final int capacity;
    private final Duration defaultTtl;
    private final CacheStats stats = new CacheStats();

    public InMemoryCache(int capacity, EvictionStrategy<K> evictionStrategy) {
        this(capacity, evictionStrategy, null);
    }

    public InMemoryCache(int capacity, EvictionStrategy<K> evictionStrategy, Duration defaultTtl) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.evictionStrategy = evictionStrategy;
        this.defaultTtl = defaultTtl;
    }

    @Override
    public Optional<V> get(K key) {
        CacheEntry<V> entry = storage.get(key);
        
        if (entry == null) {
            stats.recordMiss();
            return Optional.empty();
        }
        
        // Check TTL expiration
        if (entry.isExpired()) {
            remove(key);
            stats.recordExpiration();
            stats.recordMiss();
            return Optional.empty();
        }
        
        // Record access for eviction strategy
        entry.recordAccess();
        evictionStrategy.recordAccess(key);
        stats.recordHit();
        
        return Optional.of(entry.getValue());
    }

    @Override
    public void put(K key, V value) {
        // Check if we need to evict
        if (!storage.containsKey(key) && storage.size() >= capacity) {
            evictOne();
        }
        
        // Create entry with TTL if configured
        Instant expiresAt = defaultTtl != null ? Instant.now().plus(defaultTtl) : null;
        CacheEntry<V> entry = new CacheEntry<>(value, expiresAt);
        
        storage.put(key, entry);
        evictionStrategy.recordInsertion(key);
        stats.recordPut();
    }

    /**
     * Put a value with custom TTL.
     */
    public void put(K key, V value, Duration ttl) {
        if (!storage.containsKey(key) && storage.size() >= capacity) {
            evictOne();
        }
        
        Instant expiresAt = ttl != null ? Instant.now().plus(ttl) : null;
        CacheEntry<V> entry = new CacheEntry<>(value, expiresAt);
        
        storage.put(key, entry);
        evictionStrategy.recordInsertion(key);
        stats.recordPut();
    }

    @Override
    public Optional<V> remove(K key) {
        CacheEntry<V> entry = storage.remove(key);
        if (entry != null) {
            evictionStrategy.remove(key);
            stats.recordRemoval();
            return Optional.of(entry.getValue());
        }
        return Optional.empty();
    }

    @Override
    public boolean containsKey(K key) {
        CacheEntry<V> entry = storage.get(key);
        if (entry == null) {
            return false;
        }
        // Check if expired
        if (entry.isExpired()) {
            remove(key);
            return false;
        }
        return true;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public void clear() {
        storage.clear();
        evictionStrategy.clear();
    }

    @Override
    public CacheStats getStats() {
        return stats;
    }

    /**
     * Evict one entry based on eviction strategy.
     */
    private void evictOne() {
        Optional<K> candidate = evictionStrategy.getEvictionCandidate();
        if (candidate.isPresent()) {
            K key = candidate.get();
            storage.remove(key);
            evictionStrategy.remove(key);
            stats.recordEviction();
            System.out.printf("🗑️ Evicted key: %s%n", key);
        }
    }

    /**
     * Clean up expired entries.
     * Can be called periodically or on-demand.
     */
    public int cleanupExpired() {
        int removed = 0;
        var iterator = storage.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (entry.getValue().isExpired()) {
                evictionStrategy.remove(entry.getKey());
                iterator.remove();
                stats.recordExpiration();
                removed++;
            }
        }
        return removed;
    }
}

