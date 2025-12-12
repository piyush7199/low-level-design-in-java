package org.lld.practice.design_wallet_system.improved_solution.models;

/**
 * Enum representing types of transactions.
 */
public enum TransactionType {
    CREDIT,     // Money added to wallet (top-up)
    DEBIT,      // Money removed from wallet (payment)
    TRANSFER,   // P2P transfer between wallets
    REFUND      // Reversal of a previous transaction
}

