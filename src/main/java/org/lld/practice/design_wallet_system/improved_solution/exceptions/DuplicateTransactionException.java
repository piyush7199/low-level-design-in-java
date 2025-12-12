package org.lld.practice.design_wallet_system.improved_solution.exceptions;

/**
 * Exception thrown when a duplicate transaction is detected via idempotency key.
 */
public class DuplicateTransactionException extends RuntimeException {
    
    private final String existingTransactionId;
    
    public DuplicateTransactionException(String idempotencyKey, String existingTransactionId) {
        super("Duplicate transaction detected for idempotency key: " + idempotencyKey);
        this.existingTransactionId = existingTransactionId;
    }
    
    public String getExistingTransactionId() {
        return existingTransactionId;
    }
}

