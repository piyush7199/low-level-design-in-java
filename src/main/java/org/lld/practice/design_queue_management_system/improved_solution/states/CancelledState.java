package org.lld.practice.design_queue_management_system.improved_solution.states;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;

/**
 * Cancelled state - Token was cancelled by the customer.
 * This is a terminal state - no further transitions allowed.
 */
public class CancelledState implements TokenState {

    @Override
    public void callToken(Token token, Counter counter) {
        throw new IllegalStateException("Token has been cancelled");
    }

    @Override
    public void completeService(Token token) {
        throw new IllegalStateException("Token has been cancelled");
    }

    @Override
    public void cancelToken(Token token) {
        throw new IllegalStateException("Token is already cancelled");
    }

    @Override
    public void markNoShow(Token token) {
        throw new IllegalStateException("Token has been cancelled");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}

