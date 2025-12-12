package org.lld.practice.design_queue_management_system.improved_solution.models;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a service counter in the queue management system.
 * Each counter can serve specific token types and has an operator assigned.
 */
public class Counter {
    private final String counterId;
    private final String counterName;
    private final List<TokenType> servesTokenTypes;
    private String operatorName;
    private CounterStatus status;
    private Token currentToken;

    public Counter(String counterId, String counterName, List<TokenType> servesTokenTypes) {
        this.counterId = Objects.requireNonNull(counterId, "Counter ID cannot be null");
        this.counterName = Objects.requireNonNull(counterName, "Counter name cannot be null");
        this.servesTokenTypes = Collections.unmodifiableList(servesTokenTypes);
        this.status = CounterStatus.CLOSED;
        this.currentToken = null;
    }

    public String getCounterId() {
        return counterId;
    }

    public String getCounterName() {
        return counterName;
    }

    public List<TokenType> getServesTokenTypes() {
        return servesTokenTypes;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public CounterStatus getStatus() {
        return status;
    }

    public void setStatus(CounterStatus status) {
        this.status = status;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(Token currentToken) {
        this.currentToken = currentToken;
        this.status = (currentToken != null) ? CounterStatus.BUSY : CounterStatus.AVAILABLE;
    }

    /**
     * Check if this counter can serve a given token type.
     */
    public boolean canServe(TokenType tokenType) {
        return servesTokenTypes.contains(tokenType);
    }

    /**
     * Check if counter is available to serve.
     */
    public boolean isAvailable() {
        return status == CounterStatus.AVAILABLE;
    }

    /**
     * Open the counter for service.
     */
    public void open(String operatorName) {
        this.operatorName = operatorName;
        this.status = CounterStatus.AVAILABLE;
    }

    /**
     * Close the counter.
     */
    public void close() {
        this.status = CounterStatus.CLOSED;
        this.currentToken = null;
    }

    @Override
    public String toString() {
        return String.format("Counter{id='%s', name='%s', status=%s, currentToken=%s}",
                counterId, counterName, status,
                currentToken != null ? currentToken.getTokenNumber() : "none");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counter counter = (Counter) o;
        return Objects.equals(counterId, counter.counterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counterId);
    }
}

