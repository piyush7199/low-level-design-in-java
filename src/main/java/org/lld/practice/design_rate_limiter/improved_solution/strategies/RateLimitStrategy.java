package org.lld.practice.design_rate_limiter.improved_solution.strategies;

import org.lld.practice.design_rate_limiter.improved_solution.models.ClientBucket;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;

/**
 * Strategy interface for rate limiting algorithms.
 * Implements the Strategy pattern for different rate limiting approaches.
 */
public interface RateLimitStrategy {
    
    /**
     * Try to acquire permits for a request.
     * 
     * @param bucket The client's rate limit bucket
     * @param permits Number of permits to acquire (usually 1)
     * @return Result with allowed/denied status and metadata
     */
    RateLimitResult tryAcquire(ClientBucket bucket, int permits);
    
    /**
     * Get the name of this strategy.
     */
    String getName();
}

