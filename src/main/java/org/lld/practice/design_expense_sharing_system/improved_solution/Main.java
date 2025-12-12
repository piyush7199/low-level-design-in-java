package org.lld.practice.design_expense_sharing_system.improved_solution;

import org.lld.practice.design_expense_sharing_system.improved_solution.models.Balance;
import org.lld.practice.design_expense_sharing_system.improved_solution.models.Expense;
import org.lld.practice.design_expense_sharing_system.improved_solution.models.Group;
import org.lld.practice.design_expense_sharing_system.improved_solution.services.ExpenseSharingService;
import org.lld.practice.design_expense_sharing_system.improved_solution.strategies.PercentageSplitStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo of improved expense sharing system with debt simplification.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Improved Expense Sharing System Demo ===\n");
        
        ExpenseSharingService service = new ExpenseSharingService();
        
        System.out.println("1. Creating a group:");
        Group group = service.createGroup("Trip to Paris", "alice");
        System.out.println("Group created: " + group.getName() + " (ID: " + group.getGroupId() + ")");
        
        System.out.println("\n2. Adding members:");
        service.addMemberToGroup(group.getGroupId(), "bob");
        service.addMemberToGroup(group.getGroupId(), "charlie");
        service.addMemberToGroup(group.getGroupId(), "diana");
        System.out.println("Members: " + group.getMemberIds());
        
        System.out.println("\n3. Adding expenses with equal split:");
        Expense expense1 = service.addExpense(group.getGroupId(), "alice", 100.0,
                "Dinner", Arrays.asList("alice", "bob", "charlie"));
        System.out.println("Expense added: " + expense1.getDescription() + " - $" + expense1.getAmount());
        
        Expense expense2 = service.addExpense(group.getGroupId(), "bob", 60.0,
                "Taxi", Arrays.asList("alice", "bob"));
        System.out.println("Expense added: " + expense2.getDescription() + " - $" + expense2.getAmount());
        
        System.out.println("\n4. Checking balances:");
        List<Balance> balances = service.getGroupBalances(group.getGroupId());
        System.out.println("Group balances:");
        for (Balance balance : balances) {
            System.out.println("  " + balance.getFromUserId() + " owes " + 
                             balance.getToUserId() + ": $" + 
                             String.format("%.2f", balance.getAmount()));
        }
        
        System.out.println("\n5. Adding expense with percentage split:");
        Map<String, Object> splitParams = new HashMap<>();
        Map<String, Double> percentages = new HashMap<>();
        percentages.put("alice", 50.0);
        percentages.put("bob", 30.0);
        percentages.put("charlie", 20.0);
        splitParams.put("percentages", percentages);
        
        Expense expense3 = service.addExpense(group.getGroupId(), "charlie", 200.0,
                "Hotel", Arrays.asList("alice", "bob", "charlie"),
                new PercentageSplitStrategy(), splitParams);
        System.out.println("Expense added: " + expense3.getDescription() + " - $" + expense3.getAmount());
        
        System.out.println("\n6. Updated balances (after debt simplification):");
        balances = service.getGroupBalances(group.getGroupId());
        for (Balance balance : balances) {
            System.out.println("  " + balance.getFromUserId() + " owes " + 
                             balance.getToUserId() + ": $" + 
                             String.format("%.2f", balance.getAmount()));
        }
        
        System.out.println("\n7. Getting user-specific balances:");
        List<Balance> aliceBalances = service.getUserBalances(group.getGroupId(), "alice");
        System.out.println("Alice's balances:");
        for (Balance balance : aliceBalances) {
            if (balance.getFromUserId().equals("alice")) {
                System.out.println("  Alice owes " + balance.getToUserId() + ": $" + 
                                 String.format("%.2f", balance.getAmount()));
            } else {
                System.out.println("  " + balance.getFromUserId() + " owes Alice: $" + 
                                 String.format("%.2f", balance.getAmount()));
            }
        }
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Multiple split strategies (equal, percentage, exact)");
        System.out.println("✓ Debt simplification (reduces number of transactions)");
        System.out.println("✓ Efficient balance tracking using graph algorithms");
        System.out.println("✓ Group management");
        System.out.println("✓ Expense history tracking");
        System.out.println("✓ Strategy pattern for extensible split types");
    }
}

