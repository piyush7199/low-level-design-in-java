package org.lld.practice.design_expense_sharing_system.improved_solution.services;

import org.lld.practice.design_expense_sharing_system.improved_solution.models.Balance;

import java.util.*;

/**
 * Graph-based balance tracker that maintains debts between users.
 * Uses graph algorithms for debt simplification.
 */
public class BalanceGraph {
    // Map: fromUserId -> (toUserId -> Balance)
    private final Map<String, Map<String, Balance>> graph;
    
    public BalanceGraph() {
        this.graph = new HashMap<>();
    }
    
    /**
     * Adds a debt from one user to another.
     * 
     * @param fromUserId User who owes
     * @param toUserId User who is owed
     * @param amount Amount owed
     */
    public void addDebt(String fromUserId, String toUserId, double amount) {
        if (amount <= 0) {
            return;
        }
        
        graph.computeIfAbsent(fromUserId, k -> new HashMap<>());
        graph.computeIfAbsent(toUserId, k -> new HashMap<>());
        
        Balance balance = graph.get(fromUserId).computeIfAbsent(
            toUserId, k -> new Balance(fromUserId, toUserId, 0.0)
        );
        balance.addAmount(amount);
    }
    
    /**
     * Simplifies debts by removing circular dependencies.
     * If A owes B $10 and B owes A $5, simplify to A owes B $5.
     */
    public void simplifyDebts() {
        Set<String> visited = new HashSet<>();
        
        for (String userId : graph.keySet()) {
            if (!visited.contains(userId)) {
                simplifyForUser(userId, visited);
            }
        }
    }
    
    private void simplifyForUser(String userId, Set<String> visited) {
        visited.add(userId);
        
        Map<String, Balance> debts = graph.get(userId);
        if (debts == null) {
            return;
        }
        
        for (String creditor : new HashSet<>(debts.keySet())) {
            Balance balance = debts.get(creditor);
            if (balance == null || balance.getAmount() <= 0) {
                continue;
            }
            
            // Check if creditor owes this user
            Map<String, Balance> creditorDebts = graph.get(creditor);
            if (creditorDebts != null && creditorDebts.containsKey(userId)) {
                Balance reverseBalance = creditorDebts.get(userId);
                
                // Simplify: reduce both debts
                double minAmount = Math.min(balance.getAmount(), reverseBalance.getAmount());
                balance.subtractAmount(minAmount);
                reverseBalance.subtractAmount(minAmount);
                
                // Remove if zero
                if (balance.getAmount() <= 0.01) {
                    debts.remove(creditor);
                }
                if (reverseBalance.getAmount() <= 0.01) {
                    creditorDebts.remove(userId);
                }
            }
        }
    }
    
    /**
     * Gets all balances for a user.
     * 
     * @param userId The user ID
     * @return List of balances where this user is involved
     */
    public List<Balance> getBalancesForUser(String userId) {
        List<Balance> balances = new ArrayList<>();
        
        // Balances where user owes others
        Map<String, Balance> debts = graph.get(userId);
        if (debts != null) {
            balances.addAll(debts.values());
        }
        
        // Balances where others owe this user
        for (Map.Entry<String, Map<String, Balance>> entry : graph.entrySet()) {
            if (!entry.getKey().equals(userId)) {
                Balance balance = entry.getValue().get(userId);
                if (balance != null && balance.getAmount() > 0.01) {
                    balances.add(balance);
                }
            }
        }
        
        return balances;
    }
    
    /**
     * Gets simplified balances for a group.
     * 
     * @return List of all non-zero balances
     */
    public List<Balance> getAllBalances() {
        List<Balance> allBalances = new ArrayList<>();
        
        for (Map<String, Balance> debts : graph.values()) {
            for (Balance balance : debts.values()) {
                if (balance.getAmount() > 0.01) {
                    allBalances.add(balance);
                }
            }
        }
        
        return allBalances;
    }
}

