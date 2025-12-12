package org.lld.practice.design_queue_management_system.improved_solution.models;

import org.lld.practice.design_queue_management_system.improved_solution.states.TokenState;
import org.lld.practice.design_queue_management_system.improved_solution.states.WaitingState;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a token in the queue management system.
 * Uses State pattern for managing token lifecycle.
 */
public class Token {
    private final String tokenNumber;
    private final TokenType type;
    private final Customer customer;
    private final LocalDateTime createdAt;
    
    private TokenState state;
    private TokenStatus status;
    private Counter assignedCounter;
    private LocalDateTime calledAt;
    private LocalDateTime servedAt;
    private LocalDateTime completedAt;

    public Token(String tokenNumber, TokenType type, Customer customer) {
        this.tokenNumber = Objects.requireNonNull(tokenNumber, "Token number cannot be null");
        this.type = Objects.requireNonNull(type, "Token type cannot be null");
        this.customer = Objects.requireNonNull(customer, "Customer cannot be null");
        this.createdAt = LocalDateTime.now();
        this.status = TokenStatus.WAITING;
        this.state = new WaitingState();
    }

    // ========== State Pattern Methods ==========

    public void callToken(Counter counter) {
        state.callToken(this, counter);
    }

    public void completeService() {
        state.completeService(this);
    }

    public void cancelToken() {
        state.cancelToken(this);
    }

    public void markNoShow() {
        state.markNoShow(this);
    }

    // ========== Getters ==========

    public String getTokenNumber() {
        return tokenNumber;
    }

    public TokenType getType() {
        return type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TokenState getState() {
        return state;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public Counter getAssignedCounter() {
        return assignedCounter;
    }

    public LocalDateTime getCalledAt() {
        return calledAt;
    }

    public LocalDateTime getServedAt() {
        return servedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    // ========== Setters (used by State classes) ==========

    public void setState(TokenState state) {
        this.state = state;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    public void setAssignedCounter(Counter assignedCounter) {
        this.assignedCounter = assignedCounter;
    }

    public void setCalledAt(LocalDateTime calledAt) {
        this.calledAt = calledAt;
    }

    public void setServedAt(LocalDateTime servedAt) {
        this.servedAt = servedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * Calculate wait time in seconds from creation to being called.
     */
    public long getWaitTimeSeconds() {
        if (calledAt == null) {
            return java.time.Duration.between(createdAt, LocalDateTime.now()).getSeconds();
        }
        return java.time.Duration.between(createdAt, calledAt).getSeconds();
    }

    /**
     * Calculate service time in seconds from being called to completion.
     */
    public long getServiceTimeSeconds() {
        if (calledAt == null || completedAt == null) {
            return 0;
        }
        return java.time.Duration.between(calledAt, completedAt).getSeconds();
    }

    @Override
    public String toString() {
        return String.format("Token{number='%s', type=%s, status=%s, customer=%s}",
                tokenNumber, type, status, customer.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(tokenNumber, token.tokenNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenNumber);
    }
}

