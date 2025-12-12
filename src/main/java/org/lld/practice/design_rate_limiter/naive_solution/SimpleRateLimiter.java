package org.lld.practice.design_rate_limiter.naive_solution;

import java.util.HashMap;
import java.util.Map;

/**
 * Naive implementation of a Rate Limiter.
 * 
 * This demonstrates common anti-patterns:
 * - Fixed window boundary problem (can allow 2x burst)
 * - Not thread-safe (race conditions)
 * - No cleanup of old entries (memory leak)
 * - No remaining quota information
 * - Hardcoded configuration
 * 
 * DO NOT use this pattern in production!
 */
public class SimpleRateLimiter {
    
    private final Map<String, Integer> requestCounts = new HashMap<>();
    private final Map<String, Long> windowStart = new HashMap<>();
    private final int maxRequests = 5;  // Hardcoded - bad!
    private final long windowSizeMs = 10000;  // 10 seconds - hardcoded!
    
    /**
     * Check if request should be allowed.
     * 
     * Problems:
     * - Fixed window boundary issue
     * - Race condition between check and update
     * - No information about when to retry
     */
    public boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        Long start = windowStart.get(clientId);
        
        // Check if we need a new window
        if (start == null || now - start > windowSizeMs) {
            // New window - reset counter
            windowStart.put(clientId, now);
            requestCounts.put(clientId, 1);
            System.out.printf("✅ [%s] Request 1/%d (new window)%n", clientId, maxRequests);
            return true;
        }
        
        // Same window - check count
        int count = requestCounts.getOrDefault(clientId, 0);
        if (count < maxRequests) {
            // Race condition here! Multiple threads could pass this check
            requestCounts.put(clientId, count + 1);
            System.out.printf("✅ [%s] Request %d/%d%n", clientId, count + 1, maxRequests);
            return true;
        }
        
        // Rate limited
        System.out.printf("❌ [%s] RATE LIMITED (at %d/%d)%n", clientId, count, maxRequests);
        return false;
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Naive Rate Limiter Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        SimpleRateLimiter limiter = new SimpleRateLimiter();
        String clientId = "user-123";
        
        System.out.println("Scenario 1: Normal requests within limit");
        System.out.println("─".repeat(50));
        for (int i = 0; i < 7; i++) {
            limiter.allowRequest(clientId);
        }
        
        System.out.println("\n⚠️ Fixed Window Boundary Problem Demo:");
        System.out.println("─".repeat(50));
        System.out.println("Waiting for window to reset...\n");
        Thread.sleep(11000);  // Wait for new window
        
        // Make 4 requests at end of window
        System.out.println("Making 4 requests at end of window 1...");
        for (int i = 0; i < 4; i++) {
            limiter.allowRequest(clientId);
        }
        
        // Wait just past window boundary
        long remaining = 10000 - (System.currentTimeMillis() % 10000);
        System.out.printf("\nWaiting %dms for window boundary...%n%n", remaining + 100);
        Thread.sleep(remaining + 100);
        
        // Make 4 more requests at start of new window
        System.out.println("Making 4 requests at start of window 2...");
        for (int i = 0; i < 4; i++) {
            limiter.allowRequest(clientId);
        }
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. 8 requests passed in ~1 second (limit is 5/10s)!");
        System.out.println("2. No retry-after information when rate limited");
        System.out.println("3. Old client entries never cleaned up (memory leak)");
        System.out.println("4. Not thread-safe for concurrent requests");
        System.out.println("5. Hardcoded limits - not configurable");
    }
}

