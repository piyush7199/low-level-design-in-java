package org.lld.practice.design_rate_limiter.improved_solution;

import org.lld.practice.design_rate_limiter.improved_solution.factory.RateLimiterFactory;
import org.lld.practice.design_rate_limiter.improved_solution.limiter.RateLimiter;
import org.lld.practice.design_rate_limiter.improved_solution.models.RateLimitResult;

import java.time.Duration;

/**
 * Demo application for the Rate Limiter.
 * 
 * Demonstrates:
 * - Token Bucket algorithm with burst capacity
 * - Fixed Window algorithm
 * - Sliding Window Counter algorithm
 * - Sliding Window Log algorithm
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚦 RATE LIMITER - DEMO                                     ║");
        System.out.println("║     Design Patterns: Strategy, Factory                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // Demo 1: Token Bucket
        demoTokenBucket();
        
        // Demo 2: Fixed Window
        demoFixedWindow();
        
        // Demo 3: Sliding Window Counter
        demoSlidingWindowCounter();
        
        // Demo 4: Compare Algorithms
        compareAlgorithms();
        
        // Summary
        printSummary();
    }
    
    private static void demoTokenBucket() throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 1: Token Bucket Algorithm");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // 5 tokens/second, burst capacity of 10
        RateLimiter limiter = RateLimiterFactory.createTokenBucket(5, 10);
        String clientId = "user-123";
        
        System.out.println("📦 Config: 5 tokens/sec, burst capacity: 10");
        System.out.println("\n1. Burst test - making 10 requests immediately:");
        System.out.println("─".repeat(50));
        
        int allowed = 0;
        int denied = 0;
        for (int i = 1; i <= 12; i++) {
            RateLimitResult result = limiter.tryAcquire(clientId);
            if (result.isAllowed()) {
                allowed++;
                System.out.printf("   Request %2d: ✅ ALLOWED (remaining: %d)%n", 
                        i, result.getRemaining());
            } else {
                denied++;
                System.out.printf("   Request %2d: ❌ DENIED (retry after: %ds)%n", 
                        i, result.getRetryAfterSeconds());
            }
        }
        System.out.printf("\n   Summary: %d allowed, %d denied%n", allowed, denied);
        
        System.out.println("\n2. Wait 2 seconds for token refill...");
        Thread.sleep(2000);
        
        System.out.println("\n3. Making more requests after refill:");
        System.out.println("─".repeat(50));
        for (int i = 1; i <= 5; i++) {
            RateLimitResult result = limiter.tryAcquire(clientId);
            System.out.printf("   Request %d: %s (remaining: %d)%n", 
                    i, 
                    result.isAllowed() ? "✅ ALLOWED" : "❌ DENIED",
                    result.getRemaining());
        }
        
        System.out.println();
    }
    
    private static void demoFixedWindow() throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 2: Fixed Window Algorithm");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // 5 requests per 5 seconds
        RateLimiter limiter = RateLimiterFactory.createFixedWindow(5, Duration.ofSeconds(5));
        String clientId = "api-key-abc";
        
        System.out.println("📦 Config: 5 requests per 5 seconds");
        System.out.println("\n1. Making requests within window:");
        System.out.println("─".repeat(50));
        
        for (int i = 1; i <= 7; i++) {
            RateLimitResult result = limiter.tryAcquire(clientId);
            System.out.printf("   Request %d: %s (remaining: %d/%d)%n", 
                    i, 
                    result.isAllowed() ? "✅ ALLOWED" : "❌ DENIED",
                    result.getRemaining(),
                    result.getLimit());
        }
        
        System.out.println("\n2. Waiting for window reset (5 seconds)...");
        Thread.sleep(5000);
        
        System.out.println("\n3. New window - requests allowed again:");
        System.out.println("─".repeat(50));
        for (int i = 1; i <= 3; i++) {
            RateLimitResult result = limiter.tryAcquire(clientId);
            System.out.printf("   Request %d: %s (remaining: %d/%d)%n", 
                    i, 
                    result.isAllowed() ? "✅ ALLOWED" : "❌ DENIED",
                    result.getRemaining(),
                    result.getLimit());
        }
        
        System.out.println();
    }
    
    private static void demoSlidingWindowCounter() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 3: Sliding Window Counter Algorithm");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // 10 requests per 10 seconds
        RateLimiter limiter = RateLimiterFactory.createSlidingWindowCounter(10, Duration.ofSeconds(10));
        String clientId = "service-xyz";
        
        System.out.println("📦 Config: 10 requests per 10 seconds (sliding)");
        System.out.println("   Uses weighted average of current + previous window");
        System.out.println("\n1. Making requests:");
        System.out.println("─".repeat(50));
        
        for (int i = 1; i <= 12; i++) {
            RateLimitResult result = limiter.tryAcquire(clientId);
            System.out.printf("   Request %2d: %s (remaining: ~%d)%n", 
                    i, 
                    result.isAllowed() ? "✅ ALLOWED" : "❌ DENIED",
                    result.getRemaining());
        }
        
        System.out.println();
    }
    
    private static void compareAlgorithms() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 4: Algorithm Comparison");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        System.out.println("Scenario: 5 requests allowed per 5 seconds");
        System.out.println("Test: Make 8 requests rapidly\n");
        
        // Create all limiters with same config
        RateLimiter tokenBucket = RateLimiterFactory.createTokenBucket(1, 5);
        RateLimiter fixedWindow = RateLimiterFactory.createFixedWindow(5, Duration.ofSeconds(5));
        RateLimiter slidingCounter = RateLimiterFactory.createSlidingWindowCounter(5, Duration.ofSeconds(5));
        RateLimiter slidingLog = RateLimiterFactory.createSlidingWindowLog(5, Duration.ofSeconds(5));
        
        RateLimiter[] limiters = {tokenBucket, fixedWindow, slidingCounter, slidingLog};
        String[] clients = {"client-1", "client-2", "client-3", "client-4"};
        
        System.out.println("Results (8 rapid requests each):");
        System.out.println("─".repeat(60));
        System.out.printf("%-25s | Allowed | Denied%n", "Algorithm");
        System.out.println("─".repeat(60));
        
        for (int i = 0; i < limiters.length; i++) {
            int allowed = 0;
            int denied = 0;
            
            for (int j = 0; j < 8; j++) {
                RateLimitResult result = limiters[i].tryAcquire(clients[i]);
                if (result.isAllowed()) allowed++;
                else denied++;
            }
            
            System.out.printf("%-25s | %7d | %6d%n", 
                    limiters[i].getAlgorithmName(), allowed, denied);
        }
        
        System.out.println("─".repeat(60));
        System.out.println("\nObservations:");
        System.out.println("  - Token Bucket: Allowed burst up to capacity (5)");
        System.out.println("  - Fixed Window: Allowed exactly 5, blocked rest");
        System.out.println("  - Sliding Counter: Similar to fixed but smoother");
        System.out.println("  - Sliding Log: Most accurate, blocked after 5");
        System.out.println();
    }
    
    private static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY CONCEPTS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Strategy Pattern: Different rate limiting algorithms");
        System.out.println("✅ Factory Pattern: RateLimiterFactory creates instances");
        System.out.println("✅ Builder Pattern: RateLimitConfig configuration");
        System.out.println();
        System.out.println("🎯 Algorithm Selection Guide:");
        System.out.println("   Token Bucket    → When you need burst tolerance");
        System.out.println("   Fixed Window    → Simple use cases, OK with boundary spikes");
        System.out.println("   Sliding Counter → High traffic, memory efficient");
        System.out.println("   Sliding Log     → Precision critical, lower traffic");
        System.out.println();
        System.out.println("📋 HTTP Rate Limit Headers:");
        System.out.println("   X-RateLimit-Limit     → Maximum requests allowed");
        System.out.println("   X-RateLimit-Remaining → Requests remaining in window");
        System.out.println("   X-RateLimit-Reset     → Timestamp when limit resets");
        System.out.println("   Retry-After           → Seconds to wait before retrying");
    }
}

