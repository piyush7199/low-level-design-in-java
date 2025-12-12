package org.lld.practice.design_wallet_system.improved_solution.models;

/**
 * Enum for ledger entry type (double-entry bookkeeping).
 */
public enum EntryType {
    DEBIT,   // Decreases balance (money going out)
    CREDIT   // Increases balance (money coming in)
}

