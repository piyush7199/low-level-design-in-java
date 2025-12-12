package org.lld.practice.design_queue_management_system.improved_solution.states;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;

/**
 * State interface for Token lifecycle management.
 * Implements the State pattern to handle token state transitions.
 * 
 * State transitions:
 * - WAITING -> SERVING (when called)
 * - WAITING -> CANCELLED (when customer cancels)
 * - SERVING -> COMPLETED (when service finishes)
 * - SERVING -> NO_SHOW (when customer doesn't appear)
 */
public interface TokenState {
    
    /**
     * Call the token to a counter for service.
     * Valid only in WAITING state.
     */
    void callToken(Token token, Counter counter);
    
    /**
     * Complete the service for this token.
     * Valid only in SERVING state.
     */
    void completeService(Token token);
    
    /**
     * Cancel the token.
     * Valid only in WAITING state.
     */
    void cancelToken(Token token);
    
    /**
     * Mark the token as no-show (customer didn't appear).
     * Valid only in SERVING state.
     */
    void markNoShow(Token token);
    
    /**
     * Get the name of the current state.
     */
    String getStateName();
}

