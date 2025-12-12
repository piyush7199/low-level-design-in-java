package org.lld.practice.design_task_scheduler.improved_solution.models;

import java.time.Duration;

/**
 * Configuration for task retry behavior.
 */
public class RetryPolicy {
    
    public static final RetryPolicy NO_RETRY = new RetryPolicy(0, Duration.ZERO, 1.0);
    public static final RetryPolicy DEFAULT = new RetryPolicy(3, Duration.ofSeconds(1), 2.0);
    
    private final int maxAttempts;
    private final Duration initialDelay;
    private final double backoffMultiplier;

    public RetryPolicy(int maxAttempts, Duration initialDelay, double backoffMultiplier) {
        this.maxAttempts = maxAttempts;
        this.initialDelay = initialDelay;
        this.backoffMultiplier = backoffMultiplier;
    }

    /**
     * Calculate delay before next retry attempt.
     */
    public Duration getDelayForAttempt(int attemptNumber) {
        if (attemptNumber <= 0) return Duration.ZERO;
        
        double multiplier = Math.pow(backoffMultiplier, attemptNumber - 1);
        long delayMs = (long) (initialDelay.toMillis() * multiplier);
        return Duration.ofMillis(delayMs);
    }

    /**
     * Check if retry is allowed for the given attempt number.
     */
    public boolean shouldRetry(int attemptNumber) {
        return attemptNumber < maxAttempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public Duration getInitialDelay() {
        return initialDelay;
    }

    public double getBackoffMultiplier() {
        return backoffMultiplier;
    }

    @Override
    public String toString() {
        return String.format("RetryPolicy{maxAttempts=%d, initialDelay=%s, backoff=%.1fx}",
                maxAttempts, initialDelay, backoffMultiplier);
    }
}

