package org.lld.practice.design_wallet_system.improved_solution;

import org.lld.practice.design_wallet_system.improved_solution.exceptions.InsufficientBalanceException;
import org.lld.practice.design_wallet_system.improved_solution.models.Money;
import org.lld.practice.design_wallet_system.improved_solution.models.Transaction;
import org.lld.practice.design_wallet_system.improved_solution.models.Wallet;

/**
 * Demo application for the Wallet System.
 * 
 * Demonstrates:
 * - Wallet creation and top-up
 * - Payments and withdrawals
 * - P2P transfers
 * - Idempotency protection
 * - Double-entry ledger
 * - Balance verification
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     💰 PAYMENT WALLET / LEDGER SYSTEM - DEMO                   ║");
        System.out.println("║     Features: Double-entry Ledger, Idempotency, Money Class   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        WalletSystem system = new WalletSystem();
        
        // Demo 1: Create wallets and top-up
        demoWalletCreation(system);
        
        // Demo 2: P2P Transfer
        demoTransfer(system);
        
        // Demo 3: Payment
        demoPayment(system);
        
        // Demo 4: Idempotency
        demoIdempotency(system);
        
        // Demo 5: Insufficient Balance
        demoInsufficientBalance(system);
        
        // Demo 6: View Statements
        demoStatements(system);
        
        // Demo 7: Verification
        demoVerification(system);
        
        // Summary
        printSummary();
    }
    
    private static void demoWalletCreation(WalletSystem system) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 1: Wallet Creation and Top-up");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Create wallets
        Wallet aliceWallet = system.createWallet("Alice");
        Wallet bobWallet = system.createWallet("Bob");
        Wallet merchantWallet = system.createWallet("CoffeeShop");
        
        System.out.println();
        
        // Top-up wallets
        system.topUp(aliceWallet.getWalletId(), Money.of(1000), "topup-alice-001");
        system.topUp(bobWallet.getWalletId(), Money.of(500), "topup-bob-001");
        
        System.out.println();
        System.out.println("📊 Balances after top-up:");
        System.out.printf("   Alice: %s%n", aliceWallet.getBalance());
        System.out.printf("   Bob: %s%n", bobWallet.getBalance());
        System.out.printf("   CoffeeShop: %s%n", merchantWallet.getBalance());
        System.out.println();
    }
    
    private static void demoTransfer(WalletSystem system) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 2: P2P Transfer (Alice → Bob)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Wallet aliceWallet = system.getWalletByUserId("Alice").orElseThrow();
        Wallet bobWallet = system.getWalletByUserId("Bob").orElseThrow();
        
        System.out.println("Before transfer:");
        System.out.printf("   Alice: %s, Bob: %s%n", 
                aliceWallet.getBalance(), bobWallet.getBalance());
        
        Transaction tx = system.transfer(
                aliceWallet.getWalletId(), 
                bobWallet.getWalletId(),
                Money.of(150),
                "transfer-001");
        
        System.out.printf("%nTransaction: %s%n", tx);
        
        System.out.println("\nAfter transfer:");
        System.out.printf("   Alice: %s, Bob: %s%n", 
                aliceWallet.getBalance(), bobWallet.getBalance());
        System.out.println();
    }
    
    private static void demoPayment(WalletSystem system) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 3: Payment (Alice pays CoffeeShop)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Wallet aliceWallet = system.getWalletByUserId("Alice").orElseThrow();
        Wallet merchantWallet = system.getWalletByUserId("CoffeeShop").orElseThrow();
        
        // Alice pays for coffee
        Transaction paymentTx = system.pay(
                aliceWallet.getWalletId(),
                Money.of(5.50),
                "payment-coffee-001",
                "CoffeeShop");
        
        // Merchant receives payment (in real system, this would be automatic)
        system.topUp(merchantWallet.getWalletId(), Money.of(5.50), "receive-coffee-001");
        
        System.out.println("\nAfter payment:");
        System.out.printf("   Alice: %s (paid %s for coffee)%n", 
                aliceWallet.getBalance(), Money.of(5.50));
        System.out.printf("   CoffeeShop: %s%n", merchantWallet.getBalance());
        System.out.println();
    }
    
    private static void demoIdempotency(WalletSystem system) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 4: Idempotency Protection");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Wallet bobWallet = system.getWalletByUserId("Bob").orElseThrow();
        Money balanceBefore = bobWallet.getBalance();
        
        System.out.println("Bob's balance before: " + balanceBefore);
        System.out.println("\nSimulating duplicate top-up requests (same idempotency key)...");
        
        // Same idempotency key - should be processed only once
        String idempotencyKey = "topup-bob-duplicate-test";
        
        Transaction tx1 = system.topUp(bobWallet.getWalletId(), Money.of(100), idempotencyKey);
        System.out.printf("Request 1: %s%n", tx1.getTransactionId());
        
        Transaction tx2 = system.topUp(bobWallet.getWalletId(), Money.of(100), idempotencyKey);
        System.out.printf("Request 2: %s (duplicate detected!)%n", tx2.getTransactionId());
        
        Transaction tx3 = system.topUp(bobWallet.getWalletId(), Money.of(100), idempotencyKey);
        System.out.printf("Request 3: %s (duplicate detected!)%n", tx3.getTransactionId());
        
        System.out.println("\n✅ All three requests returned same transaction ID: " + 
                (tx1.getTransactionId().equals(tx2.getTransactionId()) && 
                 tx2.getTransactionId().equals(tx3.getTransactionId())));
        
        System.out.printf("Bob's balance after 3 requests: %s (only charged once!)%n", 
                bobWallet.getBalance());
        System.out.println();
    }
    
    private static void demoInsufficientBalance(WalletSystem system) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 5: Insufficient Balance Handling");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Wallet bobWallet = system.getWalletByUserId("Bob").orElseThrow();
        
        System.out.printf("Bob's balance: %s%n", bobWallet.getBalance());
        System.out.println("Attempting to withdraw $10,000...\n");
        
        try {
            system.withdraw(bobWallet.getWalletId(), Money.of(10000), "withdraw-large");
        } catch (InsufficientBalanceException e) {
            System.out.printf("❌ Exception caught: %s%n", e.getMessage());
            System.out.printf("Bob's balance unchanged: %s%n", bobWallet.getBalance());
        }
        System.out.println();
    }
    
    private static void demoStatements(WalletSystem system) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 6: Account Statements (Ledger)");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        Wallet aliceWallet = system.getWalletByUserId("Alice").orElseThrow();
        Wallet bobWallet = system.getWalletByUserId("Bob").orElseThrow();
        
        system.printStatement(aliceWallet.getWalletId());
        system.printStatement(bobWallet.getWalletId());
    }
    
    private static void demoVerification(WalletSystem system) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 7: Balance Verification & Reconciliation");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Verify books are balanced (double-entry accounting)
        boolean booksBalanced = system.verifyBooksBalanced();
        System.out.printf("📊 Books Balanced (Debits = Credits): %s%n", 
                booksBalanced ? "✅ YES" : "❌ NO");
        
        // Verify each wallet's balance matches ledger
        System.out.println("\n📊 Wallet Balance Verification:");
        for (var wallet : system.getWalletService().getAllWallets()) {
            boolean matches = system.verifyWalletBalance(wallet.getWalletId());
            System.out.printf("   %s (%s): %s%n", 
                    wallet.getUserId(), 
                    wallet.getBalance(),
                    matches ? "✅ Verified" : "❌ Mismatch!");
        }
        
        system.printStatus();
    }
    
    private static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY CONCEPTS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Money Class: BigDecimal-based, no floating-point errors");
        System.out.println("✅ Double-Entry Ledger: Every transaction creates balanced entries");
        System.out.println("✅ Idempotency: Duplicate requests return same result");
        System.out.println("✅ Thread-Safety: ReadWriteLock for concurrent access");
        System.out.println("✅ Transaction Lifecycle: PENDING → PROCESSING → COMPLETED/FAILED");
        System.out.println("✅ Audit Trail: Complete history via ledger entries");
        System.out.println();
        System.out.println("🎯 Interview Discussion Points:");
        System.out.println("   - Database transactions for atomicity");
        System.out.println("   - Saga pattern for distributed transfers");
        System.out.println("   - Event sourcing for transaction history");
        System.out.println("   - Optimistic locking with version numbers");
        System.out.println("   - Dead letter queues for failed transactions");
    }
}

