package org.lld.practice.design_queue_management_system.improved_solution.observers;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;

import java.util.List;

/**
 * Observer interface for queue management events.
 * Implements the Observer pattern to decouple notifications from core logic.
 * 
 * Observers can react to:
 * - Token generation
 * - Token being called
 * - Service completion
 * - Queue updates
 */
public interface QueueObserver {
    
    /**
     * Called when a new token is generated.
     */
    void onTokenGenerated(Token token);
    
    /**
     * Called when a token is called to a counter.
     */
    void onTokenCalled(Token token, Counter counter);
    
    /**
     * Called when service is completed for a token.
     */
    void onTokenCompleted(Token token);
    
    /**
     * Called when a token is cancelled.
     */
    void onTokenCancelled(Token token);
    
    /**
     * Called when the waiting queue is updated.
     */
    void onQueueUpdated(List<Token> waitingTokens);
}

