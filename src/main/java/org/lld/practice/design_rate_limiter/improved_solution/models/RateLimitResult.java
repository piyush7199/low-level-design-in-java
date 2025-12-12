package org.lld.practice.design_rate_limiter.improved_solution.models;

import java.time.Duration;
import java.time.Instant;

/**
 * Result of a rate limit check.
 * Contains all information needed for proper HTTP rate limit headers.
 */
public class RateLimitResult {
    
    private final boolean allowed;
    private final int limit;
    private final int remaining;
    private final Instant resetTime;
    private final Duration retryAfter;

    private RateLimitResult(boolean allowed, int limit, int remaining, 
                           Instant resetTime, Duration retryAfter) {
        this.allowed = allowed;
        this.limit = limit;
        this.remaining = remaining;
        this.resetTime = resetTime;
        this.retryAfter = retryAfter;
    }

    /**
     * Create an "allowed" result.
     */
    public static RateLimitResult allowed(int limit, int remaining, Instant resetTime) {
        return new RateLimitResult(true, limit, remaining, resetTime, null);
    }

    /**
     * Create a "denied" result with retry-after information.
     */
    public static RateLimitResult denied(int limit, Instant resetTime, Duration retryAfter) {
        return new RateLimitResult(false, limit, 0, resetTime, retryAfter);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public int getLimit() {
        return limit;
    }

    public int getRemaining() {
        return remaining;
    }

    public Instant getResetTime() {
        return resetTime;
    }

    public Duration getRetryAfter() {
        return retryAfter;
    }

    /**
     * Get retry-after in seconds (for HTTP header).
     */
    public long getRetryAfterSeconds() {
        return retryAfter != null ? retryAfter.getSeconds() : 0;
    }

    @Override
    public String toString() {
        if (allowed) {
            return String.format("RateLimitResult{ALLOWED, remaining=%d/%d, reset=%s}",
                    remaining, limit, resetTime);
        } else {
            return String.format("RateLimitResult{DENIED, limit=%d, retryAfter=%ds}",
                    limit, getRetryAfterSeconds());
        }
    }
}

