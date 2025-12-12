package org.lld.practice.design_rate_limiter.improved_solution.strategies;

import org.lld.practice.design_rate_limiter.improved_solution.models.ClientBucket;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;

import java.time.Duration;
import java.time.Instant;

/**
 * Fixed Window rate limiting strategy.
 * 
 * How it works:
 * - Divides time into fixed windows (e.g., 1 minute)
 * - Counts requests in current window
 * - Resets counter at window boundary
 * 
 * Pros:
 * - Simple to implement
 * - Memory efficient (just a counter)
 * - Predictable reset times
 * 
 * Cons:
 * - Boundary problem: Can allow 2x burst at window edges
 *   (e.g., 100 requests at 0:59 and 100 more at 1:01)
 */
public class FixedWindowStrategy implements RateLimitStrategy {

    @Override
    public RateLimitResult tryAcquire(ClientBucket bucket, int permits) {
        boolean allowed = bucket.tryAcquireFixedWindow(permits);
        
        int limit = bucket.getConfig().getMaxRequests();
        int remaining = bucket.getFixedWindowRemaining();
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
        return "Fixed Window";
    }
}

