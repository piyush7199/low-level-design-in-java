package org.lld.practice.design_rate_limiter.improved_solution.models;

import java.time.Duration;

/**
 * Configuration for rate limiter.
 * Uses Builder pattern for flexible configuration.
 */
public class RateLimitConfig {
    
    private final int maxRequests;
    private final Duration windowSize;
    private final RateLimitAlgorithm algorithm;
    private final int burstCapacity;  // For token bucket

    private RateLimitConfig(Builder builder) {
        this.maxRequests = builder.maxRequests;
        this.windowSize = builder.windowSize;
        this.algorithm = builder.algorithm;
        this.burstCapacity = builder.burstCapacity;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public Duration getWindowSize() {
        return windowSize;
    }

    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
    }

    public int getBurstCapacity() {
        return burstCapacity;
    }

    /**
     * Get refill rate for token bucket (tokens per second).
     */
    public double getRefillRate() {
        return (double) maxRequests / windowSize.getSeconds();
    }

    @Override
    public String toString() {
        return String.format("RateLimitConfig{max=%d, window=%s, algorithm=%s}",
                maxRequests, windowSize, algorithm);
    }

    public static class Builder {
        private int maxRequests = 100;
        private Duration windowSize = Duration.ofMinutes(1);
        private RateLimitAlgorithm algorithm = RateLimitAlgorithm.TOKEN_BUCKET;
        private int burstCapacity = -1;  // -1 means use maxRequests

        public Builder maxRequests(int maxRequests) {
            if (maxRequests <= 0) {
                throw new IllegalArgumentException("Max requests must be positive");
            }
            this.maxRequests = maxRequests;
            return this;
        }

        public Builder windowSize(Duration windowSize) {
            if (windowSize.isZero() || windowSize.isNegative()) {
                throw new IllegalArgumentException("Window size must be positive");
            }
            this.windowSize = windowSize;
            return this;
        }

        public Builder algorithm(RateLimitAlgorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder burstCapacity(int burstCapacity) {
            this.burstCapacity = burstCapacity;
            return this;
        }

        public RateLimitConfig build() {
            if (burstCapacity < 0) {
                burstCapacity = maxRequests;
            }
            return new RateLimitConfig(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

