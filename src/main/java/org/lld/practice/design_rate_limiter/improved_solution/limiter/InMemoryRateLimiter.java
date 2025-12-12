package org.lld.practice.design_rate_limiter.improved_solution.limiter;

import org.lld.practice.design_rate_limiter.improved_solution.models.ClientBucket;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitConfig;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;
import org.lld.practice.design_rate_limiter.improved_solution.strategies.RateLimitStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory rate limiter implementation.
 * 
 * Uses a pluggable strategy for different rate limiting algorithms.
 * Maintains per-client buckets for tracking rate limits.
 */
public class InMemoryRateLimiter implements RateLimiter {
    
    private final RateLimitConfig config;
    private final RateLimitStrategy strategy;
    private final Map<String, ClientBucket> clientBuckets = new ConcurrentHashMap<>();

    public InMemoryRateLimiter(RateLimitConfig config, RateLimitStrategy strategy) {
        this.config = config;
        this.strategy = strategy;
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        return tryAcquire(clientId, 1);
    }

    @Override
    public RateLimitResult tryAcquire(String clientId, int permits) {
        ClientBucket bucket = getOrCreateBucket(clientId);
        return strategy.tryAcquire(bucket, permits);
    }

    @Override
    public int getRemainingQuota(String clientId) {
        ClientBucket bucket = clientBuckets.get(clientId);
        if (bucket == null) {
            return config.getMaxRequests();
        }
        
        // Use the appropriate method based on algorithm
        return switch (config.getAlgorithm()) {
            case TOKEN_BUCKET -> (int) bucket.getAvailableTokens();
            case FIXED_WINDOW -> bucket.getFixedWindowRemaining();
            case SLIDING_WINDOW_COUNTER -> bucket.getSlidingWindowCounterRemaining();
            case SLIDING_WINDOW_LOG -> bucket.getSlidingWindowLogRemaining();
        };
    }

    @Override
    public String getAlgorithmName() {
        return strategy.getName();
    }

    /**
     * Get or create a bucket for a client.
     */
    private ClientBucket getOrCreateBucket(String clientId) {
        return clientBuckets.computeIfAbsent(clientId, 
                id -> new ClientBucket(id, config));
    }

    /**
     * Remove a client's bucket (for cleanup).
     */
    public void removeClient(String clientId) {
        clientBuckets.remove(clientId);
    }

    /**
     * Clear all client buckets.
     */
    public void clear() {
        clientBuckets.clear();
    }

    /**
     * Get number of tracked clients.
     */
    public int getClientCount() {
        return clientBuckets.size();
    }
}

