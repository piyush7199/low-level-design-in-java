package org.lld.practice.design_queue_management_system.improved_solution.services;

import org.lld.practice.design_queue_management_system.improved_solution.models.Counter;
import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenStatus;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;
import org.lld.practice.design_queue_management_system.improved_solution.observers.QueueObserver;
import org.lld.practice.design_queue_management_system.improved_solution.strategies.TokenAssignmentStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service responsible for queue operations.
 * Manages the waiting queue, token assignment, and observer notifications.
 * 
 * Uses the Observer pattern to notify interested parties of queue events.
 */
public class QueueService {
    
    private final List<Token> waitingQueue = new CopyOnWriteArrayList<>();
    private final List<QueueObserver> observers = new CopyOnWriteArrayList<>();
    private final TokenAssignmentStrategy assignmentStrategy;

    public QueueService(TokenAssignmentStrategy assignmentStrategy) {
        this.assignmentStrategy = assignmentStrategy;
    }

    // ========== Observer Management ==========

    /**
     * Attach an observer to receive queue events.
     */
    public void attach(QueueObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Detach an observer.
     */
    public void detach(QueueObserver observer) {
        observers.remove(observer);
    }

    // ========== Queue Operations ==========

    /**
     * Add a token to the waiting queue.
     */
    public void addToken(Token token) {
        waitingQueue.add(token);
        notifyTokenGenerated(token);
        notifyQueueUpdated();
    }

    /**
     * Get the next token for a counter based on the assignment strategy.
     */
    public synchronized Optional<Token> getNextToken(List<TokenType> supportedTypes) {
        Optional<Token> nextToken = assignmentStrategy.getNextToken(
                new ArrayList<>(waitingQueue), supportedTypes);
        
        nextToken.ifPresent(token -> {
            waitingQueue.remove(token);
            notifyQueueUpdated();
        });
        
        return nextToken;
    }

    /**
     * Call a token to a counter.
     */
    public void callToken(Token token, Counter counter) {
        token.callToken(counter);
        notifyTokenCalled(token, counter);
    }

    /**
     * Complete service for a token.
     */
    public void completeService(Token token) {
        token.completeService();
        notifyTokenCompleted(token);
    }

    /**
     * Cancel a token.
     */
    public void cancelToken(Token token) {
        if (token.getStatus() == TokenStatus.WAITING) {
            waitingQueue.remove(token);
        }
        token.cancelToken();
        notifyTokenCancelled(token);
        notifyQueueUpdated();
    }

    /**
     * Mark a token as no-show.
     */
    public void markNoShow(Token token) {
        token.markNoShow();
        notifyTokenCompleted(token);  // Reuse completed notification
    }

    /**
     * Get current waiting queue (immutable copy).
     */
    public List<Token> getWaitingQueue() {
        return Collections.unmodifiableList(new ArrayList<>(waitingQueue));
    }

    /**
     * Get count of waiting tokens.
     */
    public int getWaitingCount() {
        return waitingQueue.size();
    }

    /**
     * Get count of waiting tokens for a specific type.
     */
    public long getWaitingCount(TokenType type) {
        return waitingQueue.stream()
                .filter(token -> token.getType() == type)
                .count();
    }

    // ========== Observer Notifications ==========

    private void notifyTokenGenerated(Token token) {
        observers.forEach(observer -> observer.onTokenGenerated(token));
    }

    private void notifyTokenCalled(Token token, Counter counter) {
        observers.forEach(observer -> observer.onTokenCalled(token, counter));
    }

    private void notifyTokenCompleted(Token token) {
        observers.forEach(observer -> observer.onTokenCompleted(token));
    }

    private void notifyTokenCancelled(Token token) {
        observers.forEach(observer -> observer.onTokenCancelled(token));
    }

    private void notifyQueueUpdated() {
        List<Token> snapshot = new ArrayList<>(waitingQueue);
        observers.forEach(observer -> observer.onQueueUpdated(snapshot));
    }
}

