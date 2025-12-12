package org.lld.practice.design_rate_limiter.improved_solution.models;

/**
 * Enum representing different rate limiting algorithms.
 */
public enum RateLimitAlgorithm {
    /**
     * Token Bucket: Allows bursts up to bucket capacity.
     * Tokens refill at a steady rate.
     * Best for: APIs that need burst tolerance.
     */
    TOKEN_BUCKET,
    
    /**
     * Sliding Window Log: Stores timestamps of all requests.
     * Most accurate but memory-intensive.
     * Best for: When precision is critical.
     */
    SLIDING_WINDOW_LOG,
    
    /**
     * Sliding Window Counter: Weighted average of two windows.
     * Good balance of accuracy and memory.
     * Best for: High-traffic systems.
     */
    SLIDING_WINDOW_COUNTER,
    
    /**
     * Fixed Window: Simple counter per time window.
     * Has boundary spike issue but simplest to implement.
     * Best for: Simple rate limiting needs.
     */
    FIXED_WINDOW
}

