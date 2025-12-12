package org.lld.practice.design_rate_limiter.improved_solution.strategies;

import org.lld.practice.design_rate_limiter.improved_solution.models.ClientBucket;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;

import java.time.Duration;
import java.time.Instant;

/**
 * Token Bucket rate limiting strategy.
 * 
 * How it works:
 * - Bucket has a capacity (max tokens / burst size)
 * - Tokens are added at a steady refill rate
 * - Each request consumes one or more tokens
 * - If no tokens available, request is denied
 * 
 * Pros:
 * - Allows bursts up to bucket capacity
 * - Smooth rate limiting over time
 * - Simple and efficient
 * 
 * Cons:
 * - Initial burst possible if bucket starts full
 */
public class TokenBucketStrategy implements RateLimitStrategy {

    @Override
    public RateLimitResult tryAcquire(ClientBucket bucket, int permits) {
        boolean allowed = bucket.tryAcquireTokens(permits);
        
        int limit = bucket.getConfig().getBurstCapacity();
        int remaining = (int) bucket.getAvailableTokens();
        Instant resetTime = bucket.getTokenRefillTime(1);
        
        if (allowed) {
            return RateLimitResult.allowed(limit, remaining, resetTime);
        } else {
            Duration retryAfter = Duration.between(Instant.now(), 
                    bucket.getTokenRefillTime(permits));
            if (retryAfter.isNegative()) {
                retryAfter = Duration.ofMillis(100);
            }
            return RateLimitResult.denied(limit, resetTime, retryAfter);
        }
    }

    @Override
    public String getName() {
        return "Token Bucket";
    }
}

