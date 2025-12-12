package org.lld.practice.design_expense_sharing_system.naive_solution;

import java.util.Arrays;
import java.util.Map;

/**
 * Demo of naive expense sharing implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Expense Sharing System Demo ===\n");
        
        SimpleExpenseSharing expenseSharing = new SimpleExpenseSharing();
        
        expenseSharing.addExpense("alice", 100.0, Arrays.asList("alice", "bob", "charlie"));
        expenseSharing.addExpense("bob", 60.0, Arrays.asList("alice", "bob"));
        
        System.out.println("Balances:");
        Map<String, Double> balances = expenseSharing.getBalances();
        balances.forEach((user, balance) -> {
            if (balance > 0) {
                System.out.println(user + " is owed: $" + String.format("%.2f", balance));
            } else if (balance < 0) {
                System.out.println(user + " owes: $" + String.format("%.2f", Math.abs(balance)));
            } else {
                System.out.println(user + " is settled up");
            }
        });
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- Recalculates balances from all transactions");
        System.out.println("- No debt simplification");
        System.out.println("- Only supports equal split");
        System.out.println("- No group management");
        System.out.println("- No settlement tracking");
    }
}

