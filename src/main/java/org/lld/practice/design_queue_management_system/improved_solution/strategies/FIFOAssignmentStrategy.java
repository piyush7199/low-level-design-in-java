package org.lld.practice.design_queue_management_system.improved_solution.strategies;

import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.List;
import java.util.Optional;

/**
 * First-In-First-Out token assignment strategy.
 * 
 * Selects the oldest token that the counter can serve.
 * Simple and fair, but doesn't consider priority levels.
 */
public class FIFOAssignmentStrategy implements TokenAssignmentStrategy {

    @Override
    public Optional<Token> getNextToken(List<Token> waitingTokens, List<TokenType> supportedTypes) {
        if (waitingTokens == null || waitingTokens.isEmpty()) {
            return Optional.empty();
        }
        
        // Find the first token that matches supported types
        return waitingTokens.stream()
                .filter(token -> supportedTypes.contains(token.getType()))
                .findFirst();
    }
}

