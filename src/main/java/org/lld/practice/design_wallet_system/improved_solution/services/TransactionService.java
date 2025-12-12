package org.lld.practice.design_wallet_system.improved_solution.services;

import org.lld.practice.design_wallet_system.improved_solution.exceptions.InsufficientBalanceException;
import org.lld.practice.design_wallet_system.improved_solution.models.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for processing transactions.
 * Handles credits, debits, and transfers with idempotency support.
 */
public class TransactionService {
    
    private static final String SYSTEM_WALLET_ID = "SYSTEM";
    
    private final WalletService walletService;
    private final LedgerService ledgerService;
    
    // Transaction storage
    private final Map<String, Transaction> transactionsById = new ConcurrentHashMap<>();
    
    // Idempotency cache
    private final Map<String, Transaction> idempotencyCache = new ConcurrentHashMap<>();

    public TransactionService(WalletService walletService, LedgerService ledgerService) {
        this.walletService = walletService;
        this.ledgerService = ledgerService;
    }

    /**
     * Credit (add money) to a wallet - e.g., top-up, receive payment.
     */
    public Transaction credit(String walletId, Money amount, String idempotencyKey, String description) {
        // Check idempotency
        Transaction existing = checkIdempotency(idempotencyKey);
        if (existing != null) {
            return existing;
        }
        
        Wallet wallet = walletService.getWalletOrThrow(walletId);
        
        // Create transaction
        Transaction tx = Transaction.builder()
                .idempotencyKey(idempotencyKey)
                .type(TransactionType.CREDIT)
                .amount(amount)
                .targetWalletId(walletId)
                .description(description)
                .build();
        
        try {
            tx.markProcessing();
            
            // Credit the wallet
            wallet.credit(amount);
            
            // Record ledger entries (double-entry)
            // DEBIT from external/system (money coming in)
            ledgerService.recordEntry(tx.getTransactionId(), SYSTEM_WALLET_ID,
                    EntryType.DEBIT, amount, Money.ZERO, "Out: " + description);
            
            // CREDIT to user wallet
            ledgerService.recordEntry(tx.getTransactionId(), walletId,
                    EntryType.CREDIT, amount, wallet.getBalance(), description);
            
            tx.complete();
            System.out.printf("✅ %s: Credited %s to wallet %s%n", 
                    tx.getTransactionId(), amount, wallet.getUserId());
            
        } catch (Exception e) {
            tx.fail(e.getMessage());
            throw e;
        }
        
        saveTransaction(tx);
        return tx;
    }

    /**
     * Debit (remove money) from a wallet - e.g., payment, withdrawal.
     */
    public Transaction debit(String walletId, Money amount, String idempotencyKey, String description) {
        // Check idempotency
        Transaction existing = checkIdempotency(idempotencyKey);
        if (existing != null) {
            return existing;
        }
        
        Wallet wallet = walletService.getWalletOrThrow(walletId);
        
        // Create transaction
        Transaction tx = Transaction.builder()
                .idempotencyKey(idempotencyKey)
                .type(TransactionType.DEBIT)
                .amount(amount)
                .sourceWalletId(walletId)
                .description(description)
                .build();
        
        try {
            tx.markProcessing();
            
            // Debit the wallet (will throw if insufficient balance)
            wallet.debit(amount);
            
            // Record ledger entries (double-entry)
            // DEBIT from user wallet
            ledgerService.recordEntry(tx.getTransactionId(), walletId,
                    EntryType.DEBIT, amount, wallet.getBalance(), description);
            
            // CREDIT to external/system (money going out)
            ledgerService.recordEntry(tx.getTransactionId(), SYSTEM_WALLET_ID,
                    EntryType.CREDIT, amount, Money.ZERO, "In: " + description);
            
            tx.complete();
            System.out.printf("✅ %s: Debited %s from wallet %s%n", 
                    tx.getTransactionId(), amount, wallet.getUserId());
            
        } catch (InsufficientBalanceException e) {
            tx.fail(e.getMessage());
            System.out.printf("❌ %s: Failed - %s%n", tx.getTransactionId(), e.getMessage());
            saveTransaction(tx);
            throw e;
        } catch (Exception e) {
            tx.fail(e.getMessage());
            throw e;
        }
        
        saveTransaction(tx);
        return tx;
    }

    /**
     * Transfer money between two wallets.
     */
    public Transaction transfer(String sourceWalletId, String targetWalletId, 
                                Money amount, String idempotencyKey, String description) {
        // Check idempotency
        Transaction existing = checkIdempotency(idempotencyKey);
        if (existing != null) {
            return existing;
        }
        
        Wallet sourceWallet = walletService.getWalletOrThrow(sourceWalletId);
        Wallet targetWallet = walletService.getWalletOrThrow(targetWalletId);
        
        // Create transaction
        Transaction tx = Transaction.builder()
                .idempotencyKey(idempotencyKey)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .sourceWalletId(sourceWalletId)
                .targetWalletId(targetWalletId)
                .description(description)
                .build();
        
        try {
            tx.markProcessing();
            
            // Atomic transfer: debit source, credit target
            // In production, this would be in a database transaction
            sourceWallet.debit(amount);
            targetWallet.credit(amount);
            
            // Record ledger entries (double-entry)
            // DEBIT from source
            ledgerService.recordEntry(tx.getTransactionId(), sourceWalletId,
                    EntryType.DEBIT, amount, sourceWallet.getBalance(), 
                    "Transfer to " + targetWallet.getUserId());
            
            // CREDIT to target
            ledgerService.recordEntry(tx.getTransactionId(), targetWalletId,
                    EntryType.CREDIT, amount, targetWallet.getBalance(), 
                    "Transfer from " + sourceWallet.getUserId());
            
            tx.complete();
            System.out.printf("✅ %s: Transferred %s from %s to %s%n", 
                    tx.getTransactionId(), amount, 
                    sourceWallet.getUserId(), targetWallet.getUserId());
            
        } catch (InsufficientBalanceException e) {
            tx.fail(e.getMessage());
            System.out.printf("❌ %s: Failed - %s%n", tx.getTransactionId(), e.getMessage());
            saveTransaction(tx);
            throw e;
        } catch (Exception e) {
            tx.fail(e.getMessage());
            // In production, would need compensation/rollback here
            throw e;
        }
        
        saveTransaction(tx);
        return tx;
    }

    /**
     * Refund a previous transaction.
     */
    public Transaction refund(String originalTransactionId, String idempotencyKey) {
        Transaction originalTx = getTransaction(originalTransactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + originalTransactionId));
        
        if (!originalTx.isCompleted()) {
            throw new IllegalStateException("Can only refund completed transactions");
        }
        
        // Check idempotency
        Transaction existing = checkIdempotency(idempotencyKey);
        if (existing != null) {
            return existing;
        }
        
        // Reverse the original transaction
        switch (originalTx.getType()) {
            case CREDIT -> {
                // Original was a credit, so we debit
                return debit(originalTx.getTargetWalletId(), originalTx.getAmount(),
                        idempotencyKey, "Refund: " + originalTransactionId);
            }
            case DEBIT -> {
                // Original was a debit, so we credit
                return credit(originalTx.getSourceWalletId(), originalTx.getAmount(),
                        idempotencyKey, "Refund: " + originalTransactionId);
            }
            case TRANSFER -> {
                // Reverse the transfer
                return transfer(originalTx.getTargetWalletId(), originalTx.getSourceWalletId(),
                        originalTx.getAmount(), idempotencyKey, 
                        "Refund: " + originalTransactionId);
            }
            default -> throw new IllegalStateException("Cannot refund transaction type: " + originalTx.getType());
        }
    }

    // ========== Helper Methods ==========

    private Transaction checkIdempotency(String idempotencyKey) {
        if (idempotencyKey == null) return null;
        
        Transaction existing = idempotencyCache.get(idempotencyKey);
        if (existing != null) {
            System.out.printf("ℹ️ Duplicate request detected. Returning existing transaction: %s%n", 
                    existing.getTransactionId());
        }
        return existing;
    }

    private void saveTransaction(Transaction tx) {
        transactionsById.put(tx.getTransactionId(), tx);
        if (tx.getIdempotencyKey() != null) {
            idempotencyCache.put(tx.getIdempotencyKey(), tx);
        }
    }

    public Optional<Transaction> getTransaction(String transactionId) {
        return Optional.ofNullable(transactionsById.get(transactionId));
    }

    public Collection<Transaction> getAllTransactions() {
        return Collections.unmodifiableCollection(transactionsById.values());
    }

    public int getTransactionCount() {
        return transactionsById.size();
    }
}

