package org.lld.practice.design_queue_management_system.improved_solution.states;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenStatus;

import java.time.LocalDateTime;

/**
 * Waiting state - Token is in queue waiting to be called.
 * 
 * Valid transitions:
 * - WAITING -> SERVING (callToken)
 * - WAITING -> CANCELLED (cancelToken)
 */
public class WaitingState implements TokenState {

    @Override
    public void callToken(Token token, Counter counter) {
        token.setAssignedCounter(counter);
        token.setCalledAt(LocalDateTime.now());
        token.setStatus(TokenStatus.SERVING);
        token.setState(new ServingState());
        
        counter.setCurrentToken(token);
        
        System.out.printf("📢 Token %s called to %s%n", 
                token.getTokenNumber(), counter.getCounterName());
    }

    @Override
    public void completeService(Token token) {
        throw new IllegalStateException("Cannot complete service - token is still waiting in queue");
    }

    @Override
    public void cancelToken(Token token) {
        token.setStatus(TokenStatus.CANCELLED);
        token.setCompletedAt(LocalDateTime.now());
        token.setState(new CancelledState());
        
        System.out.printf("❌ Token %s cancelled%n", token.getTokenNumber());
    }

    @Override
    public void markNoShow(Token token) {
        throw new IllegalStateException("Cannot mark no-show - token hasn't been called yet");
    }

    @Override
    public String getStateName() {
        return "WAITING";
    }
}

