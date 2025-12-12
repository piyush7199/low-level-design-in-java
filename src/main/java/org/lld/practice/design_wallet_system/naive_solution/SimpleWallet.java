package org.lld.practice.design_wallet_system.naive_solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive implementation of a Payment Wallet.
 * 
 * This demonstrates common anti-patterns:
 * - Using double for currency (precision loss!)
 * - No atomicity in transfers
 * - No idempotency protection
 * - Race conditions possible
 * - No proper ledger/audit trail
 * 
 * DO NOT use this pattern in production!
 */
public class SimpleWallet {
    
    private final String walletId;
    private final String userId;
    private double balance;  // ❌ Never use double for money!
    private final List<String> transactions = new ArrayList<>();
    
    public SimpleWallet(String walletId, String userId) {
        this.walletId = walletId;
        this.userId = userId;
        this.balance = 0.0;
    }
    
    /**
     * Add money to wallet.
     * 
     * Problems:
     * - No validation
     * - No transaction ID
     * - No idempotency
     */
    public void credit(double amount) {
        balance += amount;  // Not thread-safe!
        transactions.add("CREDIT: $" + amount);
        System.out.printf("✅ Credited $%.2f to %s. Balance: $%.2f%n", 
                amount, userId, balance);
    }
    
    /**
     * Remove money from wallet.
     * 
     * Problems:
     * - Check-then-act is not atomic
     * - Race condition possible
     */
    public boolean debit(double amount) {
        if (balance >= amount) {  // Check
            balance -= amount;     // Act - not atomic!
            transactions.add("DEBIT: $" + amount);
            System.out.printf("✅ Debited $%.2f from %s. Balance: $%.2f%n", 
                    amount, userId, balance);
            return true;
        }
        System.out.printf("❌ Insufficient balance for %s%n", userId);
        return false;
    }
    
    /**
     * Transfer money to another wallet.
     * 
     * Problems:
     * - Not atomic! Debit can succeed but credit fail
     * - No rollback mechanism
     * - No transaction record linking both wallets
     */
    public boolean transfer(SimpleWallet to, double amount) {
        System.out.printf("%nTransfer: %s → %s ($%.2f)%n", 
                this.userId, to.userId, amount);
        
        // Step 1: Debit from source
        if (this.debit(amount)) {
            // Step 2: Credit to target
            // If this fails, source already debited! Money lost!
            to.credit(amount);
            return true;
        }
        return false;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public List<String> getTransactions() {
        return transactions;
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) {
        System.out.println("=== Naive Wallet Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        SimpleWallet alice = new SimpleWallet("W1", "Alice");
        SimpleWallet bob = new SimpleWallet("W2", "Bob");
        
        // Add money
        alice.credit(1000.00);
        bob.credit(500.00);
        
        // Transfer
        alice.transfer(bob, 250.00);
        
        // Show balances
        System.out.printf("%nFinal Balances:%n");
        System.out.printf("  Alice: $%.2f%n", alice.getBalance());
        System.out.printf("  Bob: $%.2f%n", bob.getBalance());
        
        // Demonstrate precision issue
        System.out.println("\n⚠️ Precision Problem Demo:");
        SimpleWallet test = new SimpleWallet("W3", "Test");
        test.credit(0.1);
        test.credit(0.2);
        System.out.printf("  0.1 + 0.2 = %.17f (expected 0.30)%n", test.getBalance());
        System.out.println("  This is why we never use double for money!");
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. Using double causes precision loss");
        System.out.println("2. Transfer is not atomic - can lose money");
        System.out.println("3. No transaction IDs or audit trail");
        System.out.println("4. Race conditions possible with concurrent access");
        System.out.println("5. No idempotency - retries cause duplicate charges");
        System.out.println("6. No proper accounting/ledger");
    }
}

