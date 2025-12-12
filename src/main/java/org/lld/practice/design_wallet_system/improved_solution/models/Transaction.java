package org.lld.practice.design_wallet_system.improved_solution.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a financial transaction.
 */
public class Transaction {
    
    private final String transactionId;
    private final String idempotencyKey;
    private final TransactionType type;
    private final Money amount;
    private final String sourceWalletId;
    private final String targetWalletId;
    private final String description;
    private final Instant createdAt;
    
    private TransactionStatus status;
    private String failureReason;
    private Instant completedAt;

    private Transaction(Builder builder) {
        this.transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.idempotencyKey = builder.idempotencyKey;
        this.type = builder.type;
        this.amount = builder.amount;
        this.sourceWalletId = builder.sourceWalletId;
        this.targetWalletId = builder.targetWalletId;
        this.description = builder.description;
        this.createdAt = Instant.now();
        this.status = TransactionStatus.PENDING;
    }

    // ========== Status Transitions ==========

    public void markProcessing() {
        this.status = TransactionStatus.PROCESSING;
    }

    public void complete() {
        this.status = TransactionStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public void fail(String reason) {
        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
        this.completedAt = Instant.now();
    }

    public void cancel() {
        if (status == TransactionStatus.PENDING) {
            this.status = TransactionStatus.CANCELLED;
            this.completedAt = Instant.now();
        }
    }

    // ========== Getters ==========

    public String getTransactionId() {
        return transactionId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public TransactionType getType() {
        return type;
    }

    public Money getAmount() {
        return amount;
    }

    public String getSourceWalletId() {
        return sourceWalletId;
    }

    public String getTargetWalletId() {
        return targetWalletId;
    }

    public String getDescription() {
        return description;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public boolean isCompleted() {
        return status == TransactionStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == TransactionStatus.FAILED;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', type=%s, amount=%s, status=%s, source='%s', target='%s'}",
                transactionId, type, amount, status, 
                sourceWalletId != null ? sourceWalletId : "N/A",
                targetWalletId != null ? targetWalletId : "N/A");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    // ========== Builder ==========

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String idempotencyKey;
        private TransactionType type;
        private Money amount;
        private String sourceWalletId;
        private String targetWalletId;
        private String description;

        public Builder idempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder amount(Money amount) {
            this.amount = amount;
            return this;
        }

        public Builder sourceWalletId(String sourceWalletId) {
            this.sourceWalletId = sourceWalletId;
            return this;
        }

        public Builder targetWalletId(String targetWalletId) {
            this.targetWalletId = targetWalletId;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Transaction build() {
            Objects.requireNonNull(type, "Transaction type is required");
            Objects.requireNonNull(amount, "Amount is required");
            if (idempotencyKey == null) {
                idempotencyKey = UUID.randomUUID().toString();
            }
            return new Transaction(this);
        }
    }
}

