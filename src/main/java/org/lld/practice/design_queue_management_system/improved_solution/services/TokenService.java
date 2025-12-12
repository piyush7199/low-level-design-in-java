package org.lld.practice.design_queue_management_system.improved_solution.services;

import org.lld.practice.design_queue_management_system.improved_solution.models.Customer;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsible for token generation and management.
 * Handles token numbering and lookup operations.
 */
public class TokenService {
    
    private final Map<TokenType, AtomicInteger> tokenCounters = new ConcurrentHashMap<>();
    private final Map<String, Token> tokenRegistry = new ConcurrentHashMap<>();

    public TokenService() {
        // Initialize counters for each token type
        for (TokenType type : TokenType.values()) {
            tokenCounters.put(type, new AtomicInteger(0));
        }
    }

    /**
     * Generate a new token for a customer.
     * Token number format: {TYPE_PREFIX}-{SEQUENCE_NUMBER}
     * Example: R-001, V-005, S-012
     */
    public Token generateToken(Customer customer, TokenType type) {
        Objects.requireNonNull(customer, "Customer cannot be null");
        Objects.requireNonNull(type, "Token type cannot be null");
        
        int sequence = tokenCounters.get(type).incrementAndGet();
        String tokenNumber = String.format("%s-%03d", type.getPrefix(), sequence);
        
        Token token = new Token(tokenNumber, type, customer);
        tokenRegistry.put(tokenNumber, token);
        
        return token;
    }

    /**
     * Find a token by its number.
     */
    public Optional<Token> findToken(String tokenNumber) {
        return Optional.ofNullable(tokenRegistry.get(tokenNumber));
    }

    /**
     * Get total tokens generated for a specific type.
     */
    public int getTokenCount(TokenType type) {
        return tokenCounters.get(type).get();
    }

    /**
     * Get total tokens generated across all types.
     */
    public int getTotalTokenCount() {
        return tokenCounters.values().stream()
                .mapToInt(AtomicInteger::get)
                .sum();
    }

    /**
     * Reset all counters (useful for daily reset).
     */
    public void resetCounters() {
        tokenCounters.values().forEach(counter -> counter.set(0));
        tokenRegistry.clear();
    }
}

