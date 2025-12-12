package org.lld.practice.design_queue_management_system.improved_solution.states;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenStatus;

import java.time.LocalDateTime;

/**
 * Serving state - Token is currently being served at a counter.
 * 
 * Valid transitions:
 * - SERVING -> COMPLETED (completeService)
 * - SERVING -> NO_SHOW (markNoShow)
 */
public class ServingState implements TokenState {

    @Override
    public void callToken(Token token, Counter counter) {
        throw new IllegalStateException("Token is already being served at " + 
                token.getAssignedCounter().getCounterName());
    }

    @Override
    public void completeService(Token token) {
        token.setCompletedAt(LocalDateTime.now());
        token.setStatus(TokenStatus.COMPLETED);
        token.setState(new CompletedState());
        
        // Free up the counter
        Counter counter = token.getAssignedCounter();
        if (counter != null) {
            counter.setCurrentToken(null);
        }
        
        System.out.printf("✅ Token %s service completed (Wait: %ds, Service: %ds)%n",
                token.getTokenNumber(),
                token.getWaitTimeSeconds(),
                token.getServiceTimeSeconds());
    }

    @Override
    public void cancelToken(Token token) {
        throw new IllegalStateException("Cannot cancel - token is already being served");
    }

    @Override
    public void markNoShow(Token token) {
        token.setCompletedAt(LocalDateTime.now());
        token.setStatus(TokenStatus.NO_SHOW);
        token.setState(new NoShowState());
        
        // Free up the counter
        Counter counter = token.getAssignedCounter();
        if (counter != null) {
            counter.setCurrentToken(null);
        }
        
        System.out.printf("👻 Token %s marked as NO-SHOW%n", token.getTokenNumber());
    }

    @Override
    public String getStateName() {
        return "SERVING";
    }
}

