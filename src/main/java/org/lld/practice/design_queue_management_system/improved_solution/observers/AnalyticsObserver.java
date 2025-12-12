package org.lld.practice.design_queue_management_system.improved_solution.observers;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Observer that collects analytics and metrics about queue operations.
 * Tracks wait times, service times, tokens processed, etc.
 */
public class AnalyticsObserver implements QueueObserver {
    
    private final AtomicInteger totalTokensGenerated = new AtomicInteger(0);
    private final AtomicInteger totalTokensServed = new AtomicInteger(0);
    private final AtomicInteger totalTokensCancelled = new AtomicInteger(0);
    private final AtomicLong totalWaitTimeSeconds = new AtomicLong(0);
    private final AtomicLong totalServiceTimeSeconds = new AtomicLong(0);
    
    private final Map<String, AtomicInteger> tokensPerCounter = new HashMap<>();
    private final Map<TokenType, AtomicInteger> tokensPerType = new HashMap<>();

    @Override
    public void onTokenGenerated(Token token) {
        totalTokensGenerated.incrementAndGet();
        tokensPerType.computeIfAbsent(token.getType(), k -> new AtomicInteger(0)).incrementAndGet();
        
        System.out.printf("📊 [Analytics] Token generated: %s (Type: %s) | Total: %d%n",
                token.getTokenNumber(), token.getType(), totalTokensGenerated.get());
    }

    @Override
    public void onTokenCalled(Token token, Counter counter) {
        tokensPerCounter.computeIfAbsent(counter.getCounterId(), k -> new AtomicInteger(0))
                .incrementAndGet();
        
        System.out.printf("📊 [Analytics] Token called: %s at %s | Wait time: %ds%n",
                token.getTokenNumber(), counter.getCounterId(), token.getWaitTimeSeconds());
    }

    @Override
    public void onTokenCompleted(Token token) {
        totalTokensServed.incrementAndGet();
        totalWaitTimeSeconds.addAndGet(token.getWaitTimeSeconds());
        totalServiceTimeSeconds.addAndGet(token.getServiceTimeSeconds());
        
        System.out.printf("📊 [Analytics] Service completed: %s | Service time: %ds%n",
                token.getTokenNumber(), token.getServiceTimeSeconds());
    }

    @Override
    public void onTokenCancelled(Token token) {
        totalTokensCancelled.incrementAndGet();
        
        System.out.printf("📊 [Analytics] Token cancelled: %s%n", token.getTokenNumber());
    }

    @Override
    public void onQueueUpdated(List<Token> waitingTokens) {
        // Could track queue length over time for trend analysis
    }
    
    /**
     * Get average wait time in seconds.
     */
    public double getAverageWaitTimeSeconds() {
        int served = totalTokensServed.get();
        return served > 0 ? (double) totalWaitTimeSeconds.get() / served : 0;
    }
    
    /**
     * Get average service time in seconds.
     */
    public double getAverageServiceTimeSeconds() {
        int served = totalTokensServed.get();
        return served > 0 ? (double) totalServiceTimeSeconds.get() / served : 0;
    }
    
    /**
     * Print comprehensive analytics report.
     */
    public void printReport() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          📊 ANALYTICS REPORT            ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Total Tokens Generated: %-15d ║%n", totalTokensGenerated.get());
        System.out.printf("║ Total Tokens Served:    %-15d ║%n", totalTokensServed.get());
        System.out.printf("║ Total Tokens Cancelled: %-15d ║%n", totalTokensCancelled.get());
        System.out.printf("║ Avg Wait Time:          %-12.1f sec ║%n", getAverageWaitTimeSeconds());
        System.out.printf("║ Avg Service Time:       %-12.1f sec ║%n", getAverageServiceTimeSeconds());
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Tokens by Type:                        ║");
        tokensPerType.forEach((type, count) ->
            System.out.printf("║   %-20s: %-14d ║%n", type, count.get()));
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Tokens by Counter:                     ║");
        tokensPerCounter.forEach((counter, count) ->
            System.out.printf("║   %-20s: %-14d ║%n", counter, count.get()));
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}

