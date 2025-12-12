package org.lld.practice.design_wallet_system.improved_solution.exceptions;

/**
 * Exception thrown for invalid wallet operations.
 */
public class WalletOperationException extends RuntimeException {
    
    public WalletOperationException(String message) {
        super(message);
    }
}

