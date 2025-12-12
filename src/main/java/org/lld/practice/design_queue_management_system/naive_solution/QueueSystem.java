package org.lld.practice.design_queue_management_system.naive_solution;

import java.util.*;

/**
 * Naive implementation of Queue Management System.
 * 
 * This is a "God Class" anti-pattern that demonstrates common mistakes:
 * - Violates Single Responsibility Principle (handles everything)
 * - Violates Open/Closed Principle (must modify to add features)
 * - No proper state management for tokens
 * - No priority handling
 * - Not thread-safe
 * - Tight coupling between all concerns
 * 
 * DO NOT use this pattern in production!
 */
public class QueueSystem {
    private int tokenCounter = 0;
    private final List<String> waitingQueue = new ArrayList<>();
    private final Map<String, String> counterTokens = new HashMap<>();
    private final Map<String, Long> tokenStartTimes = new HashMap<>();
    
    /**
     * Generate a new token for a customer.
     * 
     * Problems:
     * - No validation
     * - Token numbering not thread-safe
     * - No proper token entity
     */
    public String generateToken(String customerName, String type) {
        tokenCounter++;
        String token = type.toUpperCase().charAt(0) + "-" + String.format("%03d", tokenCounter);
        waitingQueue.add(token);
        tokenStartTimes.put(token, System.currentTimeMillis());
        
        // Everything mixed together - display, notification, logging
        System.out.println("🎫 Token generated: " + token + " for " + customerName);
        System.out.println("📊 Current queue size: " + waitingQueue.size());
        
        // Simulating SMS notification (tightly coupled)
        sendSMS(customerName, "Your token is: " + token);
        
        return token;
    }
    
    /**
     * Call the next token for a counter.
     * 
     * Problems:
     * - No priority handling
     * - Simple FIFO only
     * - Not thread-safe (race condition possible)
     * - Display, notification, analytics all mixed
     */
    public void callNextToken(String counterId) {
        if (waitingQueue.isEmpty()) {
            System.out.println("⚠️ No tokens waiting in queue");
            return;
        }
        
        // Race condition: multiple counters could get same token!
        String token = waitingQueue.remove(0);
        counterTokens.put(counterId, token);
        
        // All concerns mixed together
        updateDisplayBoard(counterId, token);
        sendSMS("Customer", "Your token " + token + " is being called at Counter " + counterId);
        logAnalytics("Token called", token, counterId);
        
        System.out.println("📢 Counter " + counterId + " calling token: " + token);
    }
    
    /**
     * Mark service as complete for a counter.
     * 
     * Problems:
     * - No proper state tracking
     * - What if token was never called?
     */
    public void completeService(String counterId) {
        String token = counterTokens.remove(counterId);
        if (token == null) {
            System.out.println("⚠️ No token being served at counter " + counterId);
            return;
        }
        
        Long startTime = tokenStartTimes.remove(token);
        long waitTime = System.currentTimeMillis() - startTime;
        
        System.out.println("✅ Service completed for token: " + token);
        System.out.println("⏱️ Total time: " + (waitTime / 1000) + " seconds");
        
        updateDisplayBoard(counterId, null);
        logAnalytics("Service completed", token, counterId);
    }
    
    /**
     * Get current queue status.
     * 
     * Problem: Exposes internal list directly (not immutable)
     */
    public List<String> getWaitingTokens() {
        return waitingQueue;  // Should return defensive copy!
    }
    
    /**
     * Add a new counter.
     * 
     * Problem: No proper counter entity, just string IDs
     */
    public void addCounter(String counterId) {
        counterTokens.put(counterId, null);
        System.out.println("➕ Counter " + counterId + " added");
    }
    
    // ========== Tightly coupled helper methods ==========
    
    private void sendSMS(String recipient, String message) {
        // Simulated SMS - should be separate service
        System.out.println("📱 SMS to " + recipient + ": " + message);
    }
    
    private void updateDisplayBoard(String counterId, String token) {
        // Simulated display update - should be observer
        if (token != null) {
            System.out.println("🖥️ Display: Counter " + counterId + " → Token " + token);
        } else {
            System.out.println("🖥️ Display: Counter " + counterId + " → Available");
        }
    }
    
    private void logAnalytics(String event, String token, String counterId) {
        // Simulated analytics - should be observer
        System.out.println("📈 Analytics: " + event + " | Token: " + token + " | Counter: " + counterId);
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) {
        System.out.println("=== Naive Queue System Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        QueueSystem system = new QueueSystem();
        
        // Add counters
        system.addCounter("C1");
        system.addCounter("C2");
        
        System.out.println();
        
        // Generate tokens
        system.generateToken("John Doe", "Regular");
        system.generateToken("Jane Smith", "VIP");
        system.generateToken("Bob Wilson", "Regular");
        
        System.out.println();
        
        // Call tokens (notice: VIP waits same as Regular - no priority!)
        system.callNextToken("C1");
        system.callNextToken("C2");
        
        System.out.println();
        
        // Complete service
        system.completeService("C1");
        
        System.out.println();
        
        // Call next
        system.callNextToken("C1");
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. VIP token (V-002) waited behind Regular token (R-001)");
        System.out.println("2. All logic in one class (God Class)");
        System.out.println("3. Not thread-safe for concurrent access");
        System.out.println("4. Cannot extend without modifying existing code");
    }
}

