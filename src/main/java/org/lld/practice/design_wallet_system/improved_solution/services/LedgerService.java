package org.lld.practice.design_wallet_system.improved_solution.services;

import org.lld.practice.design_wallet_system.improved_solution.models.EntryType;
import org.lld.practice.design_wallet_system.improved_solution.models.LedgerEntry;
import org.lld.practice.design_wallet_system.improved_solution.models.Money;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing the ledger (double-entry bookkeeping).
 * Provides audit trail and reconciliation capabilities.
 */
public class LedgerService {
    
    // All ledger entries
    private final List<LedgerEntry> allEntries = Collections.synchronizedList(new ArrayList<>());
    
    // Index by wallet for quick lookup
    private final Map<String, List<LedgerEntry>> entriesByWallet = new ConcurrentHashMap<>();
    
    // Index by transaction
    private final Map<String, List<LedgerEntry>> entriesByTransaction = new ConcurrentHashMap<>();

    /**
     * Record a ledger entry.
     */
    public LedgerEntry recordEntry(String transactionId, String walletId, 
                                   EntryType entryType, Money amount, 
                                   Money balanceAfter, String description) {
        LedgerEntry entry = new LedgerEntry(
                transactionId, walletId, entryType, amount, balanceAfter, description);
        
        allEntries.add(entry);
        entriesByWallet.computeIfAbsent(walletId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(entry);
        entriesByTransaction.computeIfAbsent(transactionId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(entry);
        
        return entry;
    }

    /**
     * Get all entries for a wallet.
     */
    public List<LedgerEntry> getEntriesForWallet(String walletId) {
        return entriesByWallet.getOrDefault(walletId, Collections.emptyList());
    }

    /**
     * Get all entries for a transaction.
     */
    public List<LedgerEntry> getEntriesForTransaction(String transactionId) {
        return entriesByTransaction.getOrDefault(transactionId, Collections.emptyList());
    }

    /**
     * Get the last N entries for a wallet (most recent first).
     */
    public List<LedgerEntry> getRecentEntries(String walletId, int limit) {
        List<LedgerEntry> entries = getEntriesForWallet(walletId);
        int start = Math.max(0, entries.size() - limit);
        List<LedgerEntry> recent = new ArrayList<>(entries.subList(start, entries.size()));
        Collections.reverse(recent);
        return recent;
    }

    /**
     * Calculate running balance from ledger entries for a wallet.
     * Used for reconciliation and verification.
     */
    public Money calculateBalanceFromLedger(String walletId) {
        return entriesByWallet.getOrDefault(walletId, Collections.emptyList())
                .stream()
                .map(LedgerEntry::getSignedAmount)
                .reduce(Money.ZERO, Money::add);
    }

    /**
     * Verify that a wallet's balance matches the ledger.
     */
    public boolean verifyWalletBalance(String walletId, Money expectedBalance) {
        Money ledgerBalance = calculateBalanceFromLedger(walletId);
        return ledgerBalance.equals(expectedBalance);
    }

    /**
     * Verify that all debits equal all credits (books are balanced).
     * In a proper double-entry system, this should always be true.
     */
    public boolean verifyBooksBalanced() {
        Money totalDebits = allEntries.stream()
                .filter(e -> e.getEntryType() == EntryType.DEBIT)
                .map(LedgerEntry::getAmount)
                .reduce(Money.ZERO, Money::add);
        
        Money totalCredits = allEntries.stream()
                .filter(e -> e.getEntryType() == EntryType.CREDIT)
                .map(LedgerEntry::getAmount)
                .reduce(Money.ZERO, Money::add);
        
        return totalDebits.equals(totalCredits);
    }

    /**
     * Get total number of ledger entries.
     */
    public int getTotalEntryCount() {
        return allEntries.size();
    }

    /**
     * Print a wallet's statement.
     */
    public void printStatement(String walletId) {
        List<LedgerEntry> entries = getEntriesForWallet(walletId);
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.printf("║  ACCOUNT STATEMENT - Wallet: %-40s ║%n", walletId);
        System.out.println("╠═══════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  Type    │    Amount    │   Balance    │ Description                  ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════╣");
        
        for (LedgerEntry entry : entries) {
            String type = entry.getEntryType() == EntryType.CREDIT ? "CREDIT" : "DEBIT ";
            String sign = entry.getEntryType() == EntryType.CREDIT ? "+" : "-";
            System.out.printf("║  %s  │ %s%-10s │ %-12s │ %-28s ║%n",
                    type, sign, entry.getAmount(), entry.getBalanceAfter(),
                    truncate(entry.getDescription(), 28));
        }
        
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 3) + "...";
    }
}

