package org.lld.practice.design_rate_limiter.improved_solution.models;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents the rate limiting state for a single client.
 * Thread-safe using atomic operations.
 */
public class ClientBucket {
    
    private final String clientId;
    private final RateLimitConfig config;
    
    // Token bucket state
    private volatile double tokens;
    private final AtomicLong lastRefillTimeNanos = new AtomicLong(System.nanoTime());
    
    // Fixed/Sliding window state
    private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());
    private final AtomicInteger currentWindowCount = new AtomicInteger(0);
    private final AtomicInteger previousWindowCount = new AtomicInteger(0);
    
    // Sliding window log state (stores request timestamps)
    private final Deque<Long> requestTimestamps = new ConcurrentLinkedDeque<>();

    public ClientBucket(String clientId, RateLimitConfig config) {
        this.clientId = clientId;
        this.config = config;
        this.tokens = config.getBurstCapacity();
    }

    // ========== Token Bucket Methods ==========

    public synchronized boolean tryAcquireTokens(int permits) {
        refillTokens();
        if (tokens >= permits) {
            tokens -= permits;
            return true;
        }
        return false;
    }

    public synchronized double getAvailableTokens() {
        refillTokens();
        return tokens;
    }

    private void refillTokens() {
        long now = System.nanoTime();
        long lastRefill = lastRefillTimeNanos.get();
        double elapsedSeconds = (now - lastRefill) / 1_000_000_000.0;
        
        double newTokens = elapsedSeconds * config.getRefillRate();
        tokens = Math.min(config.getBurstCapacity(), tokens + newTokens);
        lastRefillTimeNanos.set(now);
    }

    public Instant getTokenRefillTime(int permits) {
        double tokensNeeded = permits - tokens;
        if (tokensNeeded <= 0) {
            return Instant.now();
        }
        double secondsNeeded = tokensNeeded / config.getRefillRate();
        return Instant.now().plusMillis((long) (secondsNeeded * 1000));
    }

    // ========== Fixed Window Methods ==========

    public boolean tryAcquireFixedWindow(int permits) {
        long now = System.currentTimeMillis();
        long windowMs = config.getWindowSize().toMillis();
        long currentStart = windowStart.get();
        
        // Check if we need to start a new window
        if (now - currentStart >= windowMs) {
            // New window - reset counter
            windowStart.set(now);
            previousWindowCount.set(currentWindowCount.get());
            currentWindowCount.set(permits);
            return true;
        }
        
        // Same window - check and increment
        int current = currentWindowCount.get();
        if (current + permits <= config.getMaxRequests()) {
            currentWindowCount.addAndGet(permits);
            return true;
        }
        return false;
    }

    public int getFixedWindowRemaining() {
        return Math.max(0, config.getMaxRequests() - currentWindowCount.get());
    }

    public Instant getFixedWindowResetTime() {
        long windowMs = config.getWindowSize().toMillis();
        return Instant.ofEpochMilli(windowStart.get() + windowMs);
    }

    // ========== Sliding Window Counter Methods ==========

    public boolean tryAcquireSlidingWindowCounter(int permits) {
        long now = System.currentTimeMillis();
        long windowMs = config.getWindowSize().toMillis();
        long currentStart = windowStart.get();
        
        // Check if we need to start a new window
        if (now - currentStart >= windowMs) {
            previousWindowCount.set(currentWindowCount.get());
            currentWindowCount.set(0);
            windowStart.set(currentStart + windowMs);
            currentStart = windowStart.get();
        }
        
        // Calculate weighted count
        double positionInWindow = (double) (now - currentStart) / windowMs;
        double weightedCount = previousWindowCount.get() * (1 - positionInWindow) 
                             + currentWindowCount.get();
        
        if (weightedCount + permits <= config.getMaxRequests()) {
            currentWindowCount.addAndGet(permits);
            return true;
        }
        return false;
    }

    public int getSlidingWindowCounterRemaining() {
        long now = System.currentTimeMillis();
        long windowMs = config.getWindowSize().toMillis();
        long currentStart = windowStart.get();
        
        double positionInWindow = (double) (now - currentStart) / windowMs;
        double weightedCount = previousWindowCount.get() * (1 - positionInWindow) 
                             + currentWindowCount.get();
        
        return Math.max(0, (int) (config.getMaxRequests() - weightedCount));
    }

    // ========== Sliding Window Log Methods ==========

    public boolean tryAcquireSlidingWindowLog(int permits) {
        long now = System.currentTimeMillis();
        long windowMs = config.getWindowSize().toMillis();
        long windowStart = now - windowMs;
        
        // Remove expired timestamps
        while (!requestTimestamps.isEmpty() && requestTimestamps.peekFirst() < windowStart) {
            requestTimestamps.pollFirst();
        }
        
        // Check if we have room
        if (requestTimestamps.size() + permits <= config.getMaxRequests()) {
            for (int i = 0; i < permits; i++) {
                requestTimestamps.addLast(now);
            }
            return true;
        }
        return false;
    }

    public int getSlidingWindowLogRemaining() {
        long now = System.currentTimeMillis();
        long windowMs = config.getWindowSize().toMillis();
        long windowStart = now - windowMs;
        
        // Count valid timestamps
        int validCount = (int) requestTimestamps.stream()
                .filter(ts -> ts >= windowStart)
                .count();
        
        return Math.max(0, config.getMaxRequests() - validCount);
    }

    public Instant getSlidingWindowLogResetTime() {
        if (requestTimestamps.isEmpty()) {
            return Instant.now();
        }
        // Reset time is when the oldest request expires
        Long oldest = requestTimestamps.peekFirst();
        if (oldest == null) {
            return Instant.now();
        }
        return Instant.ofEpochMilli(oldest + config.getWindowSize().toMillis());
    }

    // ========== Common Methods ==========

    public String getClientId() {
        return clientId;
    }

    public RateLimitConfig getConfig() {
        return config;
    }
}

