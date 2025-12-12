package org.lld.practice.design_queue_management_system.improved_solution.observers;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Observer that manages the display board showing current status.
 * Updates the display when tokens are called or service is completed.
 */
public class DisplayBoardObserver implements QueueObserver {
    
    private final Map<String, String> counterDisplay = new ConcurrentHashMap<>();
    private int totalWaiting = 0;

    @Override
    public void onTokenGenerated(Token token) {
        // Display updated via onQueueUpdated
    }

    @Override
    public void onTokenCalled(Token token, Counter counter) {
        counterDisplay.put(counter.getCounterId(), token.getTokenNumber());
        printDisplay();
    }

    @Override
    public void onTokenCompleted(Token token) {
        Counter counter = token.getAssignedCounter();
        if (counter != null) {
            counterDisplay.put(counter.getCounterId(), "---");
        }
        printDisplay();
    }

    @Override
    public void onTokenCancelled(Token token) {
        // No display update needed for cancellation
    }

    @Override
    public void onQueueUpdated(List<Token> waitingTokens) {
        this.totalWaiting = waitingTokens.size();
    }
    
    private void printDisplay() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          🖥️  DISPLAY BOARD              ║");
        System.out.println("╠════════════════════════════════════════╣");
        
        counterDisplay.forEach((counterId, tokenNumber) -> 
            System.out.printf("║   Counter %-4s  →  Token %-10s   ║%n", counterId, tokenNumber));
        
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║   Customers Waiting: %-17d ║%n", totalWaiting);
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    /**
     * Register a counter on the display board.
     */
    public void registerCounter(String counterId) {
        counterDisplay.put(counterId, "---");
    }
    
    /**
     * Get current display state.
     */
    public Map<String, String> getDisplayState() {
        return Map.copyOf(counterDisplay);
    }
}

