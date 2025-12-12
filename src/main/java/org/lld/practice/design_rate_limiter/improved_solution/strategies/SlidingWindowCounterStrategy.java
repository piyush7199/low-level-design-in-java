package org.lld.practice.design_rate_limiter.improved_solution.strategies;

import org.lld.practice.design_rate_limiter.improved_solution.models.ClientBucket;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;

import java.time.Duration;
import java.time.Instant;

/**
 * Sliding Window Counter rate limiting strategy.
 * 
 * How it works:
 * - Maintains count for current and previous window
 * - Calculates weighted average based on position in current window
 * - Formula: count = prev_count * (1 - position) + curr_count
 * 
 * Example:
 * - Previous window: 80 requests
 * - Current window: 30 requests
 * - Position in current window: 40%
 * - Weighted count = 80 * 0.6 + 30 = 78
 * 
 * Pros:
 * - Smoother than fixed window
 * - Memory efficient (just two counters)
 * - Good approximation of sliding window
 * 
 * Cons:
 * - Not 100% accurate (weighted average)
 * - Slightly more complex than fixed window
 */
public class SlidingWindowCounterStrategy implements RateLimitStrategy {

    @Override
    public RateLimitResult tryAcquire(ClientBucket bucket, int permits) {
        boolean allowed = bucket.tryAcquireSlidingWindowCounter(permits);
        
        int limit = bucket.getConfig().getMaxRequests();
        int remaining = bucket.getSlidingWindowCounterRemaining();
        Instant resetTime = bucket.getFixedWindowResetTime();
        
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
        return "Sliding Window Counter";
    }
}

