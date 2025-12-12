package org.lld.practice.design_wallet_system.improved_solution.models;

import org.lld.practice.design_wallet_system.improved_solution.exceptions.InsufficientBalanceException;
import org.lld.practice.design_wallet_system.improved_solution.exceptions.WalletOperationException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a digital wallet for a user.
 * Thread-safe with read-write locking.
 */
public class Wallet {
    
    private final String walletId;
    private final String userId;
    private final Instant createdAt;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    private Money balance;
    private WalletStatus status;
    private Instant updatedAt;
    private long version;  // For optimistic locking

    public Wallet(String userId) {
        this.walletId = "W-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.userId = Objects.requireNonNull(userId);
        this.balance = Money.ZERO;
        this.status = WalletStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.version = 1;
    }

    // ========== Balance Operations ==========

    /**
     * Credit (add) money to the wallet.
     */
    public void credit(Money amount) {
        validateActive();
        validatePositiveAmount(amount);
        
        lock.writeLock().lock();
        try {
            this.balance = this.balance.add(amount);
            this.updatedAt = Instant.now();
            this.version++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Debit (remove) money from the wallet.
     * @throws InsufficientBalanceException if balance is insufficient
     */
    public void debit(Money amount) {
        validateActive();
        validatePositiveAmount(amount);
        
        lock.writeLock().lock();
        try {
            if (balance.isLessThan(amount)) {
                throw new InsufficientBalanceException(
                        String.format("Insufficient balance. Available: %s, Required: %s", 
                                balance, amount));
            }
            this.balance = this.balance.subtract(amount);
            this.updatedAt = Instant.now();
            this.version++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Check if wallet has sufficient balance for a debit.
     */
    public boolean hasSufficientBalance(Money amount) {
        lock.readLock().lock();
        try {
            return balance.isGreaterThanOrEqual(amount);
        } finally {
            lock.readLock().unlock();
        }
    }

    // ========== Status Operations ==========

    public void freeze() {
        lock.writeLock().lock();
        try {
            this.status = WalletStatus.FROZEN;
            this.updatedAt = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void unfreeze() {
        lock.writeLock().lock();
        try {
            if (this.status == WalletStatus.FROZEN) {
                this.status = WalletStatus.ACTIVE;
                this.updatedAt = Instant.now();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void close() {
        lock.writeLock().lock();
        try {
            if (!balance.isZero()) {
                throw new WalletOperationException("Cannot close wallet with non-zero balance");
            }
            this.status = WalletStatus.CLOSED;
            this.updatedAt = Instant.now();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // ========== Validation ==========

    private void validateActive() {
        if (status != WalletStatus.ACTIVE) {
            throw new WalletOperationException(
                    "Wallet is not active. Current status: " + status);
        }
    }

    private void validatePositiveAmount(Money amount) {
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    // ========== Getters ==========

    public String getWalletId() {
        return walletId;
    }

    public String getUserId() {
        return userId;
    }

    public Money getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }

    public WalletStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public long getVersion() {
        return version;
    }

    public boolean isActive() {
        return status == WalletStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return String.format("Wallet{id='%s', user='%s', balance=%s, status=%s}",
                walletId, userId, balance, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(walletId, wallet.walletId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId);
    }
}

