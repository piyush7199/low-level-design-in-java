package org.lld.practice.design_rate_limiter.improved_solution.strategies;

import org.lld.practice.design_rate_limiter.improved_solution.models.ClientBucket;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;

import java.time.Duration;
import java.time.Instant;

/**
 * Sliding Window Log rate limiting strategy.
 * 
 * How it works:
 * - Stores timestamp of each request
 * - Removes expired timestamps (older than window)
 * - Counts remaining timestamps to check limit
 * 
 * Pros:
 * - Most accurate - no boundary issues
 * - Precise control over rate limiting
 * - True sliding window behavior
 * 
 * Cons:
 * - High memory usage (stores all timestamps)
 * - O(n) cleanup of expired entries
 * - Not suitable for very high traffic
 */
public class SlidingWindowLogStrategy implements RateLimitStrategy {

    @Override
    public RateLimitResult tryAcquire(ClientBucket bucket, int permits) {
        boolean allowed = bucket.tryAcquireSlidingWindowLog(permits);
        
        int limit = bucket.getConfig().getMaxRequests();
        int remaining = bucket.getSlidingWindowLogRemaining();
        Instant resetTime = bucket.getSlidingWindowLogResetTime();
        
        if (allowed) {
            return RateLimitResult.allowed(limit, remaining, resetTime);
        } else {
            Duration retryAfter = Duration.between(Instant.now(), resetTime);
            if (retryAfter.isNegative()) {
                retryAfter = Duration.ofMillis(100);
            }
            return RateLimitResult.denied(limit, resetTime, retryAfter);
        }
    }

    @Override
    public String getName() {
        return "Sliding Window Log";
    }
}

