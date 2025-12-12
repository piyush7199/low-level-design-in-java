package org.lld.practice.design_queue_management_system.improved_solution;

import org.lld.practice.design_queue_management_system.improved_solution.models.*;
import org.lld.practice.design_queue_management_system.improved_solution.observers.AnalyticsObserver;
import org.lld.practice.design_queue_management_system.improved_solution.observers.CustomerNotificationObserver;
import org.lld.practice.design_queue_management_system.improved_solution.observers.DisplayBoardObserver;
import org.lld.practice.design_queue_management_system.improved_solution.observers.QueueObserver;
import org.lld.practice.design_queue_management_system.improved_solution.services.CounterService;
import org.lld.practice.design_queue_management_system.improved_solution.services.QueueService;
import org.lld.practice.design_queue_management_system.improved_solution.services.TokenService;
import org.lld.practice.design_queue_management_system.improved_solution.strategies.TokenAssignmentStrategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main entry point for the Queue Management System.
 * Implements the Singleton pattern to ensure single system instance.
 * 
 * Coordinates all services, strategies, and observers.
 * 
 * Design Patterns Used:
 * - Singleton: Single system instance
 * - Strategy: Token assignment algorithms
 * - Observer: Display, notifications, analytics
 * - State: Token lifecycle management
 */
public class QueueManagementSystem {
    
    private static volatile QueueManagementSystem instance;
    
    private final TokenService tokenService;
    private final QueueService queueService;
    private final CounterService counterService;
    private final DisplayBoardObserver displayBoard;
    private final AnalyticsObserver analytics;

    private QueueManagementSystem(TokenAssignmentStrategy assignmentStrategy) {
        this.tokenService = new TokenService();
        this.queueService = new QueueService(assignmentStrategy);
        this.counterService = new CounterService();
        
        // Initialize default observers
        this.displayBoard = new DisplayBoardObserver();
        this.analytics = new AnalyticsObserver();
        
        // Attach default observers
        queueService.attach(displayBoard);
        queueService.attach(analytics);
        queueService.attach(new CustomerNotificationObserver());
    }

    /**
     * Get the singleton instance with default configuration.
     * Thread-safe using double-checked locking.
     */
    public static QueueManagementSystem getInstance(TokenAssignmentStrategy assignmentStrategy) {
        if (instance == null) {
            synchronized (QueueManagementSystem.class) {
                if (instance == null) {
                    instance = new QueueManagementSystem(assignmentStrategy);
                }
            }
        }
        return instance;
    }

    /**
     * Reset the singleton instance (useful for testing).
     */
    public static void resetInstance() {
        synchronized (QueueManagementSystem.class) {
            instance = null;
        }
    }

    // ========== Counter Management ==========

    /**
     * Add a new counter to the system.
     */
    public void addCounter(Counter counter) {
        counterService.addCounter(counter);
        displayBoard.registerCounter(counter.getCounterId());
    }

    /**
     * Open a counter for service.
     */
    public void openCounter(String counterId, String operatorName) {
        counterService.openCounter(counterId, operatorName);
    }

    /**
     * Close a counter.
     */
    public void closeCounter(String counterId) {
        counterService.closeCounter(counterId);
    }

    // ========== Token Operations ==========

    /**
     * Generate a new token for a customer.
     */
    public Token generateToken(Customer customer, TokenType type) {
        Token token = tokenService.generateToken(customer, type);
        queueService.addToken(token);
        return token;
    }

    /**
     * Call the next token to a specific counter.
     */
    public Optional<Token> callNextToken(String counterId) {
        Optional<Counter> counterOpt = counterService.getCounter(counterId);
        
        if (counterOpt.isEmpty()) {
            System.out.printf("⚠️ Counter %s not found%n", counterId);
            return Optional.empty();
        }
        
        Counter counter = counterOpt.get();
        
        if (!counter.isAvailable()) {
            System.out.printf("⚠️ Counter %s is not available (status: %s)%n", 
                    counterId, counter.getStatus());
            return Optional.empty();
        }
        
        Optional<Token> tokenOpt = queueService.getNextToken(counter.getServesTokenTypes());
        
        if (tokenOpt.isEmpty()) {
            System.out.println("⚠️ No tokens waiting in queue for this counter");
            return Optional.empty();
        }
        
        Token token = tokenOpt.get();
        queueService.callToken(token, counter);
        
        return Optional.of(token);
    }

    /**
     * Complete service for the current token at a counter.
     */
    public void completeService(String counterId) {
        Optional<Counter> counterOpt = counterService.getCounter(counterId);
        
        if (counterOpt.isEmpty()) {
            System.out.printf("⚠️ Counter %s not found%n", counterId);
            return;
        }
        
        Counter counter = counterOpt.get();
        Token currentToken = counter.getCurrentToken();
        
        if (currentToken == null) {
            System.out.printf("⚠️ No token being served at counter %s%n", counterId);
            return;
        }
        
        queueService.completeService(currentToken);
    }

    /**
     * Mark current token as no-show at a counter.
     */
    public void markNoShow(String counterId) {
        Optional<Counter> counterOpt = counterService.getCounter(counterId);
        
        if (counterOpt.isEmpty()) {
            System.out.printf("⚠️ Counter %s not found%n", counterId);
            return;
        }
        
        Counter counter = counterOpt.get();
        Token currentToken = counter.getCurrentToken();
        
        if (currentToken == null) {
            System.out.printf("⚠️ No token being served at counter %s%n", counterId);
            return;
        }
        
        queueService.markNoShow(currentToken);
    }

    /**
     * Cancel a token by its number.
     */
    public void cancelToken(String tokenNumber) {
        Optional<Token> tokenOpt = tokenService.findToken(tokenNumber);
        
        if (tokenOpt.isEmpty()) {
            System.out.printf("⚠️ Token %s not found%n", tokenNumber);
            return;
        }
        
        queueService.cancelToken(tokenOpt.get());
    }

    // ========== Queue Status ==========

    /**
     * Get the current waiting queue.
     */
    public List<Token> getWaitingQueue() {
        return queueService.getWaitingQueue();
    }

    /**
     * Get count of waiting tokens.
     */
    public int getWaitingCount() {
        return queueService.getWaitingCount();
    }

    /**
     * Get the display board state.
     */
    public Map<String, String> getDisplayBoard() {
        return displayBoard.getDisplayState();
    }

    // ========== Observer Management ==========

    /**
     * Add a custom observer.
     */
    public void addObserver(QueueObserver observer) {
        queueService.attach(observer);
    }

    /**
     * Remove an observer.
     */
    public void removeObserver(QueueObserver observer) {
        queueService.detach(observer);
    }

    // ========== Analytics ==========

    /**
     * Print the analytics report.
     */
    public void printAnalyticsReport() {
        analytics.printReport();
    }

    /**
     * Get average wait time in seconds.
     */
    public double getAverageWaitTime() {
        return analytics.getAverageWaitTimeSeconds();
    }

    /**
     * Get average service time in seconds.
     */
    public double getAverageServiceTime() {
        return analytics.getAverageServiceTimeSeconds();
    }
}

