package org.lld.practice.design_wallet_system.improved_solution.exceptions;

/**
 * Exception thrown when a wallet has insufficient balance for an operation.
 */
public class InsufficientBalanceException extends RuntimeException {
    
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

