package org.lld.practice.design_wallet_system.improved_solution;

import org.lld.practice.design_wallet_system.improved_solution.models.Money;
import org.lld.practice.design_wallet_system.improved_solution.models.Transaction;
import org.lld.practice.design_wallet_system.improved_solution.models.Wallet;
import org.lld.practice.design_wallet_system.improved_solution.services.LedgerService;
import org.lld.practice.design_wallet_system.improved_solution.services.TransactionService;
import org.lld.practice.design_wallet_system.improved_solution.services.WalletService;

import java.util.Optional;

/**
 * Main entry point for the Wallet System.
 * Coordinates between wallet, transaction, and ledger services.
 */
public class WalletSystem {
    
    private final WalletService walletService;
    private final LedgerService ledgerService;
    private final TransactionService transactionService;

    public WalletSystem() {
        this.walletService = new WalletService();
        this.ledgerService = new LedgerService();
        this.transactionService = new TransactionService(walletService, ledgerService);
    }

    // ========== Wallet Operations ==========

    public Wallet createWallet(String userId) {
        return walletService.createWallet(userId);
    }

    public Optional<Wallet> getWallet(String walletId) {
        return walletService.getWallet(walletId);
    }

    public Optional<Wallet> getWalletByUserId(String userId) {
        return walletService.getWalletByUserId(userId);
    }

    public Money getBalance(String walletId) {
        return walletService.getWalletOrThrow(walletId).getBalance();
    }

    // ========== Transaction Operations ==========

    /**
     * Add money to a wallet.
     */
    public Transaction topUp(String walletId, Money amount, String idempotencyKey) {
        return transactionService.credit(walletId, amount, idempotencyKey, "Wallet top-up");
    }

    /**
     * Withdraw money from a wallet.
     */
    public Transaction withdraw(String walletId, Money amount, String idempotencyKey) {
        return transactionService.debit(walletId, amount, idempotencyKey, "Withdrawal");
    }

    /**
     * Make a payment from a wallet.
     */
    public Transaction pay(String walletId, Money amount, String idempotencyKey, String merchant) {
        return transactionService.debit(walletId, amount, idempotencyKey, "Payment to " + merchant);
    }

    /**
     * Transfer money between wallets.
     */
    public Transaction transfer(String fromWalletId, String toWalletId, 
                                Money amount, String idempotencyKey) {
        return transactionService.transfer(fromWalletId, toWalletId, amount, 
                idempotencyKey, "P2P Transfer");
    }

    /**
     * Refund a transaction.
     */
    public Transaction refund(String transactionId, String idempotencyKey) {
        return transactionService.refund(transactionId, idempotencyKey);
    }

    // ========== Ledger Operations ==========

    /**
     * Print account statement for a wallet.
     */
    public void printStatement(String walletId) {
        ledgerService.printStatement(walletId);
    }

    /**
     * Verify that books are balanced.
     */
    public boolean verifyBooksBalanced() {
        return ledgerService.verifyBooksBalanced();
    }

    /**
     * Verify wallet balance matches ledger.
     */
    public boolean verifyWalletBalance(String walletId) {
        Wallet wallet = walletService.getWalletOrThrow(walletId);
        return ledgerService.verifyWalletBalance(walletId, wallet.getBalance());
    }

    // ========== Services Access ==========

    public WalletService getWalletService() {
        return walletService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public LedgerService getLedgerService() {
        return ledgerService;
    }

    // ========== System Status ==========

    public void printStatus() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        💰 WALLET SYSTEM STATUS          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Total Wallets: %-24d ║%n", walletService.getAllWallets().size());
        System.out.printf("║ Total Transactions: %-19d ║%n", transactionService.getTransactionCount());
        System.out.printf("║ Total Ledger Entries: %-17d ║%n", ledgerService.getTotalEntryCount());
        System.out.printf("║ Books Balanced: %-23s ║%n", verifyBooksBalanced() ? "✅ YES" : "❌ NO");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}

