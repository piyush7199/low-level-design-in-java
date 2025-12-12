package org.lld.practice.design_queue_management_system.improved_solution.states;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;

/**
 * No-Show state - Customer didn't appear when called.
 * This is a terminal state - no further transitions allowed.
 */
public class NoShowState implements TokenState {

    @Override
    public void callToken(Token token, Counter counter) {
        throw new IllegalStateException("Token marked as no-show");
    }

    @Override
    public void completeService(Token token) {
        throw new IllegalStateException("Token marked as no-show");
    }

    @Override
    public void cancelToken(Token token) {
        throw new IllegalStateException("Token marked as no-show");
    }

    @Override
    public void markNoShow(Token token) {
        throw new IllegalStateException("Token is already marked as no-show");
    }

    @Override
    public String getStateName() {
        return "NO_SHOW";
    }
}

