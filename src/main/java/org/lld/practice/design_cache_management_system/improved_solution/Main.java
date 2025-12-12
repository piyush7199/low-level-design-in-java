package org.lld.practice.design_cache_management_system.improved_solution;

import org.lld.practice.design_cache_management_system.improved_solution.cache.Cache;
import org.lld.practice.design_cache_management_system.improved_solution.factory.CacheFactory;
import org.lld.practice.design_cache_management_system.improved_solution.models.CacheConfig;
import org.lld.practice.design_cache_management_system.improved_solution.models.EvictionPolicy;

import java.time.Duration;

/**
 * Demo application for the Cache Management System.
 * 
 * Demonstrates:
 * - LRU Cache with O(1) operations
 * - LFU Cache with frequency tracking
 * - TTL expiry support
 * - Cache statistics tracking
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     💾 CACHE MANAGEMENT SYSTEM - DEMO                          ║");
        System.out.println("║     Design Patterns: Strategy, Factory, Decorator             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // Demo 1: LRU Cache
        demoLRUCache();
        
        // Demo 2: LFU Cache
        demoLFUCache();
        
        // Demo 3: TTL Cache
        demoTTLCache();
        
        // Demo 4: Compare Eviction Strategies
        compareEvictionStrategies();
        
        // Summary
        printSummary();
    }
    
    private static void demoLRUCache() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 1: LRU Cache (Least Recently Used)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Cache<String, String> cache = CacheFactory.createLRUCache(3);
        
        System.out.println("📦 Cache capacity: 3");
        System.out.println("\n1. Adding entries: user:1, user:2, user:3");
        cache.put("user:1", "Alice");
        cache.put("user:2", "Bob");
        cache.put("user:3", "Charlie");
        System.out.printf("   Cache size: %d/%d%n", cache.size(), cache.capacity());
        
        System.out.println("\n2. Accessing user:1 (makes it most recently used)");
        cache.get("user:1");
        
        System.out.println("\n3. Adding user:4 (triggers eviction of LEAST recently used)");
        cache.put("user:4", "Diana");
        
        System.out.println("\n4. Checking cache contents:");
        System.out.printf("   user:1 = %s (accessed recently, should exist)%n", 
                cache.get("user:1").orElse("NOT FOUND"));
        System.out.printf("   user:2 = %s (not accessed, should be evicted)%n", 
                cache.get("user:2").orElse("NOT FOUND ✓"));
        System.out.printf("   user:3 = %s%n", cache.get("user:3").orElse("NOT FOUND"));
        System.out.printf("   user:4 = %s%n", cache.get("user:4").orElse("NOT FOUND"));
        
        System.out.println("\n📊 Cache Statistics:");
        cache.getStats().printReport();
    }
    
    private static void demoLFUCache() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 2: LFU Cache (Least Frequently Used)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Cache<String, String> cache = CacheFactory.createLFUCache(3);
        
        System.out.println("📦 Cache capacity: 3");
        System.out.println("\n1. Adding entries: product:1, product:2, product:3");
        cache.put("product:1", "Laptop");
        cache.put("product:2", "Phone");
        cache.put("product:3", "Tablet");
        
        System.out.println("\n2. Simulating access patterns:");
        System.out.println("   - product:1 accessed 5 times (popular item)");
        for (int i = 0; i < 5; i++) cache.get("product:1");
        
        System.out.println("   - product:2 accessed 2 times");
        cache.get("product:2");
        cache.get("product:2");
        
        System.out.println("   - product:3 accessed 1 time (least popular)");
        // Already accessed once during put
        
        System.out.println("\n3. Adding product:4 (triggers eviction of LEAST frequently used)");
        cache.put("product:4", "Watch");
        
        System.out.println("\n4. Checking cache contents:");
        System.out.printf("   product:1 = %s (5 accesses, should exist)%n", 
                cache.get("product:1").orElse("NOT FOUND"));
        System.out.printf("   product:2 = %s (2 accesses, should exist)%n", 
                cache.get("product:2").orElse("NOT FOUND"));
        System.out.printf("   product:3 = %s (1 access, should be evicted)%n", 
                cache.get("product:3").orElse("NOT FOUND ✓"));
        System.out.printf("   product:4 = %s%n", cache.get("product:4").orElse("NOT FOUND"));
        
        System.out.println("\n📊 Cache Statistics:");
        cache.getStats().printReport();
    }
    
    private static void demoTTLCache() throws InterruptedException {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 3: TTL Cache (Time-To-Live Expiry)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Create cache with 2-second TTL
        CacheConfig config = CacheConfig.builder()
                .capacity(10)
                .evictionPolicy(EvictionPolicy.LRU)
                .ttl(Duration.ofSeconds(2))
                .build();
        
        Cache<String, String> cache = CacheFactory.createCache(config);
        
        System.out.println("📦 Cache with 2-second TTL");
        System.out.println("\n1. Adding session token");
        cache.put("session:abc123", "user_data_here");
        
        System.out.println("\n2. Immediately checking (should exist):");
        System.out.printf("   session:abc123 = %s%n", 
                cache.get("session:abc123").orElse("NOT FOUND"));
        
        System.out.println("\n3. Waiting 1 second...");
        Thread.sleep(1000);
        System.out.printf("   session:abc123 = %s (still valid)%n", 
                cache.get("session:abc123").orElse("NOT FOUND"));
        
        System.out.println("\n4. Waiting 2 more seconds (total 3s, TTL is 2s)...");
        Thread.sleep(2000);
        System.out.printf("   session:abc123 = %s (expired!)%n", 
                cache.get("session:abc123").orElse("EXPIRED ✓"));
        
        System.out.println("\n📊 Cache Statistics:");
        cache.getStats().printReport();
    }
    
    private static void compareEvictionStrategies() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 4: Comparing Eviction Strategies");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Same access pattern, different strategies
        String[] keys = {"A", "B", "C"};
        String[] accessPattern = {"A", "A", "A", "B", "C"};  // A is accessed most frequently
        
        System.out.println("Scenario: Cache size 2, keys A/B/C, access pattern: A, A, A, B, C");
        System.out.println("Question: When adding new key D, which key gets evicted?\n");
        
        // Test LRU
        Cache<String, Integer> lruCache = CacheFactory.createLRUCache(2);
        simulateAndEvict(lruCache, keys, accessPattern, "LRU");
        
        // Test LFU
        Cache<String, Integer> lfuCache = CacheFactory.createLFUCache(2);
        simulateAndEvict(lfuCache, keys, accessPattern, "LFU");
        
        // Test FIFO
        Cache<String, Integer> fifoCache = CacheFactory.createFIFOCache(2);
        simulateAndEvict(fifoCache, keys, accessPattern, "FIFO");
        
        System.out.println("Observations:");
        System.out.println("  - LRU evicts A (least recently accessed, even though frequently used)");
        System.out.println("  - LFU evicts B or C (tied for lowest frequency)");
        System.out.println("  - FIFO evicts A (first inserted, regardless of access)");
    }
    
    private static void simulateAndEvict(Cache<String, Integer> cache, 
                                         String[] keys, 
                                         String[] accessPattern,
                                         String strategyName) {
        // Insert initial keys (only first 2 fit)
        cache.put("A", 1);
        cache.put("B", 2);
        
        // Simulate access pattern
        for (String key : accessPattern) {
            cache.get(key);
        }
        
        // Now add C which triggers eviction
        System.out.printf("%s: Adding 'C'... ", strategyName);
        cache.put("C", 3);
        
        // Check what was evicted
        boolean hasA = cache.containsKey("A");
        boolean hasB = cache.containsKey("B");
        boolean hasC = cache.containsKey("C");
        
        String evicted = !hasA ? "A" : (!hasB ? "B" : "?");
        System.out.printf("Evicted: %s | Remaining: %s %s %s%n", 
                evicted,
                hasA ? "A" : "-",
                hasB ? "B" : "-",
                hasC ? "C" : "-");
    }
    
    private static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY CONCEPTS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Strategy Pattern: LRU, LFU, FIFO eviction policies");
        System.out.println("✅ Factory Pattern: CacheFactory creates configured caches");
        System.out.println("✅ Decorator Pattern: ThreadSafeCache wraps any cache");
        System.out.println("✅ Builder Pattern: CacheConfig for flexible configuration");
        System.out.println("✅ O(1) Operations: HashMap + Doubly Linked List for LRU");
        System.out.println("✅ TTL Expiry: Time-based cache invalidation");
        System.out.println("✅ Statistics: Hit ratio, evictions, expirations tracking");
        System.out.println("\n🎯 When to use each strategy:");
        System.out.println("   LRU - Temporal locality (recent items likely accessed again)");
        System.out.println("   LFU - Frequency matters (popular items should stay cached)");
        System.out.println("   FIFO - Simple age-based eviction (oldest items expire first)");
    }
}

