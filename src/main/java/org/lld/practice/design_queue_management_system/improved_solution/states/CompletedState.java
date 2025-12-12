package org.lld.practice.design_queue_management_system.improved_solution.states;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;

/**
 * Completed state - Token service has been completed successfully.
 * This is a terminal state - no further transitions allowed.
 */
public class CompletedState implements TokenState {

    @Override
    public void callToken(Token token, Counter counter) {
        throw new IllegalStateException("Token service already completed");
    }

    @Override
    public void completeService(Token token) {
        throw new IllegalStateException("Token service already completed");
    }

    @Override
    public void cancelToken(Token token) {
        throw new IllegalStateException("Cannot cancel - token service already completed");
    }

    @Override
    public void markNoShow(Token token) {
        throw new IllegalStateException("Cannot mark no-show - token service already completed");
    }

    @Override
    public String getStateName() {
        return "COMPLETED";
    }
}

