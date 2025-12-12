package org.lld.practice.design_wallet_system.improved_solution.models;

/**
 * Enum representing wallet status.
 */
public enum WalletStatus {
    ACTIVE,     // Normal operation
    FROZEN,     // Temporarily suspended (fraud investigation, etc.)
    CLOSED      // Permanently closed
}

