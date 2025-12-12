package org.lld.practice.design_cache_management_system.improved_solution.factory;

import org.lld.practice.design_cache_management_system.improved_solution.cache.Cache;
import org.lld.practice.design_cache_management_system.improved_solution.cache.InMemoryCache;
import org.lld.practice.design_cache_management_system.improved_solution.cache.ThreadSafeCache;
import org.lld.practice.design_cache_management_system.improved_solution.models.CacheConfig;
import org.lld.practice.design_cache_management_system.improved_solution.strategies.*;

/**
 * Factory for creating cache instances.
 * Uses the Factory pattern to centralize cache creation logic.
 */
public class CacheFactory {

    private CacheFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Create a cache based on configuration.
     */
    public static <K, V> Cache<K, V> createCache(CacheConfig config) {
        EvictionStrategy<K> strategy = createEvictionStrategy(config);
        
        Cache<K, V> cache = new InMemoryCache<>(
                config.getCapacity(),
                strategy,
                config.getDefaultTtl()
        );
        
        // Wrap with thread-safe decorator if needed
        if (config.isThreadSafe()) {
            cache = new ThreadSafeCache<>(cache);
        }
        
        return cache;
    }

    /**
     * Create an LRU cache with specified capacity.
     */
    public static <K, V> Cache<K, V> createLRUCache(int capacity) {
        return createCache(CacheConfig.builder()
                .capacity(capacity)
                .evictionPolicy(org.lld.practice.design_cache_management_system.improved_solution.models.EvictionPolicy.LRU)
                .build());
    }

    /**
     * Create an LFU cache with specified capacity.
     */
    public static <K, V> Cache<K, V> createLFUCache(int capacity) {
        return createCache(CacheConfig.builder()
                .capacity(capacity)
                .evictionPolicy(org.lld.practice.design_cache_management_system.improved_solution.models.EvictionPolicy.LFU)
                .build());
    }

    /**
     * Create a FIFO cache with specified capacity.
     */
    public static <K, V> Cache<K, V> createFIFOCache(int capacity) {
        return createCache(CacheConfig.builder()
                .capacity(capacity)
                .evictionPolicy(org.lld.practice.design_cache_management_system.improved_solution.models.EvictionPolicy.FIFO)
                .build());
    }

    /**
     * Create eviction strategy based on policy.
     */
    private static <K> EvictionStrategy<K> createEvictionStrategy(CacheConfig config) {
        return switch (config.getEvictionPolicy()) {
            case LRU -> new LRUEvictionStrategy<>();
            case LFU -> new LFUEvictionStrategy<>();
            case FIFO -> new FIFOEvictionStrategy<>();
        };
    }
}

