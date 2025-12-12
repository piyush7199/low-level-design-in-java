package org.lld.practice.design_cache_management_system.improved_solution.models;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Statistics for cache operations.
 * Thread-safe using atomic counters.
 */
public class CacheStats {
    
    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong puts = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);
    private final AtomicLong expirations = new AtomicLong(0);
    private final AtomicLong removals = new AtomicLong(0);

    public void recordHit() {
        hits.incrementAndGet();
    }

    public void recordMiss() {
        misses.incrementAndGet();
    }

    public void recordPut() {
        puts.incrementAndGet();
    }

    public void recordEviction() {
        evictions.incrementAndGet();
    }

    public void recordExpiration() {
        expirations.incrementAndGet();
    }

    public void recordRemoval() {
        removals.incrementAndGet();
    }

    public long getHits() {
        return hits.get();
    }

    public long getMisses() {
        return misses.get();
    }

    public long getPuts() {
        return puts.get();
    }

    public long getEvictions() {
        return evictions.get();
    }

    public long getExpirations() {
        return expirations.get();
    }

    public long getRemovals() {
        return removals.get();
    }

    /**
     * Get total number of requests (hits + misses).
     */
    public long getTotalRequests() {
        return hits.get() + misses.get();
    }

    /**
     * Get cache hit ratio (0.0 to 1.0).
     */
    public double getHitRatio() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (double) hits.get() / total;
    }

    /**
     * Get cache miss ratio (0.0 to 1.0).
     */
    public double getMissRatio() {
        return 1.0 - getHitRatio();
    }

    /**
     * Reset all statistics.
     */
    public void reset() {
        hits.set(0);
        misses.set(0);
        puts.set(0);
        evictions.set(0);
        expirations.set(0);
        removals.set(0);
    }

    @Override
    public String toString() {
        return String.format(
                "CacheStats{hits=%d, misses=%d, hitRatio=%.2f%%, puts=%d, evictions=%d, expirations=%d}",
                hits.get(), misses.get(), getHitRatio() * 100, puts.get(), evictions.get(), expirations.get());
    }

    /**
     * Print formatted statistics report.
     */
    public void printReport() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          📊 CACHE STATISTICS            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Total Requests:    %-19d ║%n", getTotalRequests());
        System.out.printf("║ Cache Hits:        %-19d ║%n", hits.get());
        System.out.printf("║ Cache Misses:      %-19d ║%n", misses.get());
        System.out.printf("║ Hit Ratio:         %-17.2f%% ║%n", getHitRatio() * 100);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Total Puts:        %-19d ║%n", puts.get());
        System.out.printf("║ Evictions:         %-19d ║%n", evictions.get());
        System.out.printf("║ Expirations:       %-19d ║%n", expirations.get());
        System.out.printf("║ Manual Removals:   %-19d ║%n", removals.get());
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}

