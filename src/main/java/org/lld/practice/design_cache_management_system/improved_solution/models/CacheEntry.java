package org.lld.practice.design_cache_management_system.improved_solution.models;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a cache entry with value and metadata.
 * Tracks access patterns for eviction strategies.
 *
 * @param <V> Type of the cached value
 */
public class CacheEntry<V> {
    
    private final V value;
    private final Instant createdAt;
    private final Instant expiresAt;  // null means no expiry
    
    private Instant lastAccessedAt;
    private int accessCount;

    public CacheEntry(V value) {
        this(value, null);
    }

    public CacheEntry(V value, Instant expiresAt) {
        this.value = Objects.requireNonNull(value, "Value cannot be null");
        this.createdAt = Instant.now();
        this.lastAccessedAt = this.createdAt;
        this.expiresAt = expiresAt;
        this.accessCount = 1;
    }

    /**
     * Record an access to this entry.
     * Updates last accessed time and increments access count.
     */
    public void recordAccess() {
        this.lastAccessedAt = Instant.now();
        this.accessCount++;
    }

    /**
     * Check if this entry has expired.
     */
    public boolean isExpired() {
        if (expiresAt == null) {
            return false;
        }
        return Instant.now().isAfter(expiresAt);
    }

    /**
     * Get time until expiry in milliseconds.
     * Returns -1 if no expiry is set.
     */
    public long getTimeToLiveMillis() {
        if (expiresAt == null) {
            return -1;
        }
        long ttl = expiresAt.toEpochMilli() - Instant.now().toEpochMilli();
        return Math.max(0, ttl);
    }

    public V getValue() {
        return value;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastAccessedAt() {
        return lastAccessedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public int getAccessCount() {
        return accessCount;
    }

    @Override
    public String toString() {
        return String.format("CacheEntry{value=%s, accessCount=%d, expired=%s}",
                value, accessCount, isExpired());
    }
}

