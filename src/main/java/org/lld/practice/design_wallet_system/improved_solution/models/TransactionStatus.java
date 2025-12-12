package org.lld.practice.design_wallet_system.improved_solution.models;

/**
 * Enum representing transaction processing status.
 */
public enum TransactionStatus {
    PENDING,        // Transaction created, not yet processed
    PROCESSING,     // Currently being processed
    COMPLETED,      // Successfully processed
    FAILED,         // Processing failed
    CANCELLED       // Cancelled before processing
}

