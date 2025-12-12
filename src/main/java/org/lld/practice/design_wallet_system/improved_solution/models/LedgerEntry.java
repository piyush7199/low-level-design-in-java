package org.lld.practice.design_wallet_system.improved_solution.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a single entry in the ledger (double-entry bookkeeping).
 * Each transaction creates exactly two ledger entries that balance out.
 */
public class LedgerEntry {
    
    private final String entryId;
    private final String transactionId;
    private final String walletId;
    private final EntryType entryType;
    private final Money amount;
    private final Money balanceAfter;
    private final String description;
    private final Instant createdAt;

    public LedgerEntry(String transactionId, String walletId, EntryType entryType,
                       Money amount, Money balanceAfter, String description) {
        this.entryId = "LE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.transactionId = Objects.requireNonNull(transactionId);
        this.walletId = Objects.requireNonNull(walletId);
        this.entryType = Objects.requireNonNull(entryType);
        this.amount = Objects.requireNonNull(amount);
        this.balanceAfter = Objects.requireNonNull(balanceAfter);
        this.description = description;
        this.createdAt = Instant.now();
    }

    // ========== Getters ==========

    public String getEntryId() {
        return entryId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getWalletId() {
        return walletId;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public Money getAmount() {
        return amount;
    }

    public Money getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the signed amount (positive for credit, negative for debit).
     */
    public Money getSignedAmount() {
        return entryType == EntryType.CREDIT ? amount : amount.negate();
    }

    @Override
    public String toString() {
        String sign = entryType == EntryType.CREDIT ? "+" : "-";
        return String.format("LedgerEntry{id='%s', txn='%s', wallet='%s', %s%s, balance=%s}",
                entryId, transactionId, walletId, sign, amount, balanceAfter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerEntry that = (LedgerEntry) o;
        return Objects.equals(entryId, that.entryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryId);
    }
}

