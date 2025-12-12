package org.lld.practice.design_queue_management_system.improved_solution.strategies;

import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.List;
import java.util.Optional;

/**
 * Strategy interface for selecting the next token to be served.
 * Implements the Strategy pattern to allow different assignment algorithms.
 * 
 * Implementations:
 * - FIFOAssignmentStrategy: Simple first-in-first-out
 * - PriorityAssignmentStrategy: Considers token priority levels
 */
public interface TokenAssignmentStrategy {
    
    /**
     * Select the next token to be served from the waiting queue.
     * 
     * @param waitingTokens List of tokens currently waiting
     * @param supportedTypes Token types that the requesting counter can serve
     * @return The next token to serve, or empty if no eligible tokens
     */
    Optional<Token> getNextToken(List<Token> waitingTokens, List<TokenType> supportedTypes);
}

