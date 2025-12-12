package org.lld.practice.design_cache_management_system.improved_solution.models;

import java.time.Duration;

/**
 * Configuration for cache initialization.
 * Uses Builder pattern for flexible configuration.
 */
public class CacheConfig {
    
    private final int capacity;
    private final EvictionPolicy evictionPolicy;
    private final Duration defaultTtl;  // null means no TTL
    private final boolean threadSafe;
    private final boolean enableStats;

    private CacheConfig(Builder builder) {
        this.capacity = builder.capacity;
        this.evictionPolicy = builder.evictionPolicy;
        this.defaultTtl = builder.defaultTtl;
        this.threadSafe = builder.threadSafe;
        this.enableStats = builder.enableStats;
    }

    public int getCapacity() {
        return capacity;
    }

    public EvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }

    public Duration getDefaultTtl() {
        return defaultTtl;
    }

    public boolean isThreadSafe() {
        return threadSafe;
    }

    public boolean isEnableStats() {
        return enableStats;
    }

    public boolean hasTtl() {
        return defaultTtl != null;
    }

    @Override
    public String toString() {
        return String.format("CacheConfig{capacity=%d, policy=%s, ttl=%s, threadSafe=%s}",
                capacity, evictionPolicy, defaultTtl, threadSafe);
    }

    /**
     * Builder for CacheConfig.
     */
    public static class Builder {
        private int capacity = 100;
        private EvictionPolicy evictionPolicy = EvictionPolicy.LRU;
        private Duration defaultTtl = null;
        private boolean threadSafe = true;
        private boolean enableStats = true;

        public Builder capacity(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive");
            }
            this.capacity = capacity;
            return this;
        }

        public Builder evictionPolicy(EvictionPolicy policy) {
            this.evictionPolicy = policy;
            return this;
        }

        public Builder ttl(Duration ttl) {
            this.defaultTtl = ttl;
            return this;
        }

        public Builder threadSafe(boolean threadSafe) {
            this.threadSafe = threadSafe;
            return this;
        }

        public Builder enableStats(boolean enableStats) {
            this.enableStats = enableStats;
            return this;
        }

        public CacheConfig build() {
            return new CacheConfig(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

