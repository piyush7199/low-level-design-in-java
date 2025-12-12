package org.lld.practice.design_rate_limiter.improved_solution.factory;

import org.lld.practice.design_rate_limiter.improved_solution.limiter.InMemoryRateLimiter;
import org.lld.practice.design_rate_limiter.improved_solution.limiter.RateLimiter;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitAlgorithm;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitConfig;
import org.lld.practice.design_rate_limiter.improved_solution.strategies.*;

import java.time.Duration;

/**
 * Factory for creating rate limiter instances.
 */
public class RateLimiterFactory {

    private RateLimiterFactory() {
        // Private constructor
    }

    /**
     * Create a rate limiter based on configuration.
     */
    public static RateLimiter create(RateLimitConfig config) {
        RateLimitStrategy strategy = createStrategy(config.getAlgorithm());
        return new InMemoryRateLimiter(config, strategy);
    }

    /**
     * Create a Token Bucket rate limiter.
     * 
     * @param requestsPerSecond Maximum requests per second (refill rate)
     * @param burstCapacity Maximum burst size (bucket capacity)
     */
    public static RateLimiter createTokenBucket(int requestsPerSecond, int burstCapacity) {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRequests(requestsPerSecond)
                .windowSize(Duration.ofSeconds(1))
                .algorithm(RateLimitAlgorithm.TOKEN_BUCKET)
                .burstCapacity(burstCapacity)
                .build();
        return create(config);
    }

    /**
     * Create a Fixed Window rate limiter.
     * 
     * @param maxRequests Maximum requests per window
     * @param windowSize Size of the time window
     */
    public static RateLimiter createFixedWindow(int maxRequests, Duration windowSize) {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRequests(maxRequests)
                .windowSize(windowSize)
                .algorithm(RateLimitAlgorithm.FIXED_WINDOW)
                .build();
        return create(config);
    }

    /**
     * Create a Sliding Window Counter rate limiter.
     * 
     * @param maxRequests Maximum requests per window
     * @param windowSize Size of the time window
     */
    public static RateLimiter createSlidingWindowCounter(int maxRequests, Duration windowSize) {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRequests(maxRequests)
                .windowSize(windowSize)
                .algorithm(RateLimitAlgorithm.SLIDING_WINDOW_COUNTER)
                .build();
        return create(config);
    }

    /**
     * Create a Sliding Window Log rate limiter.
     * 
     * @param maxRequests Maximum requests per window
     * @param windowSize Size of the time window
     */
    public static RateLimiter createSlidingWindowLog(int maxRequests, Duration windowSize) {
        RateLimitConfig config = RateLimitConfig.builder()
                .maxRequests(maxRequests)
                .windowSize(windowSize)
                .algorithm(RateLimitAlgorithm.SLIDING_WINDOW_LOG)
                .build();
        return create(config);
    }

    /**
     * Create the appropriate strategy for an algorithm.
     */
    private static RateLimitStrategy createStrategy(RateLimitAlgorithm algorithm) {
        return switch (algorithm) {
            case TOKEN_BUCKET -> new TokenBucketStrategy();
            case FIXED_WINDOW -> new FixedWindowStrategy();
            case SLIDING_WINDOW_COUNTER -> new SlidingWindowCounterStrategy();
            case SLIDING_WINDOW_LOG -> new SlidingWindowLogStrategy();
        };
    }
}

