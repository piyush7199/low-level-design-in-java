package org.lld.practice.design_rate_limiter.improved_solution.limiter;

import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;

/**
 * Interface for rate limiting operations.
 */
public interface RateLimiter {
    
    /**
     * Try to acquire permission for a request.
     * 
     * @param clientId Identifier for the client (user ID, IP, API key)
     * @return Result with allowed/denied status and metadata
     */
    RateLimitResult tryAcquire(String clientId);
    
    /**
     * Try to acquire multiple permits for a request.
     * 
     * @param clientId Identifier for the client
     * @param permits Number of permits to acquire
     * @return Result with allowed/denied status and metadata
     */
    RateLimitResult tryAcquire(String clientId, int permits);
    
    /**
     * Get remaining quota for a client.
     */
    int getRemainingQuota(String clientId);
    
    /**
     * Get the name of the rate limiting algorithm.
     */
    String getAlgorithmName();
}

