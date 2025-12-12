package org.lld.practice.design_expense_sharing_system.naive_solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Naive implementation of expense sharing system.
 * 
 * This demonstrates common pitfalls:
 * - Recalculates balances from all transactions
 * - No debt simplification
 * - Only supports equal split
 * - No group management
 */
public class SimpleExpenseSharing {
    private final List<Expense> expenses = new ArrayList<>();
    
    public void addExpense(String paidBy, double amount, List<String> participants) {
        if (!participants.contains(paidBy)) {
            participants.add(paidBy);
        }
        double perPerson = amount / participants.size();
        expenses.add(new Expense(paidBy, amount, participants, perPerson));
    }
    
    public Map<String, Double> getBalances() {
        Map<String, Double> balances = new HashMap<>();
        
        // Recalculate from all transactions - expensive!
        for (Expense expense : expenses) {
            String paidBy = expense.getPaidBy();
            double perPerson = expense.getPerPerson();
            
            // Person who paid gets credited
            balances.put(paidBy, balances.getOrDefault(paidBy, 0.0) + expense.getAmount());
            
            // Participants owe their share
            for (String participant : expense.getParticipants()) {
                if (!participant.equals(paidBy)) {
                    balances.put(participant, balances.getOrDefault(participant, 0.0) - perPerson);
                } else {
                    balances.put(participant, balances.getOrDefault(participant, 0.0) - perPerson);
                }
            }
        }
        
        return balances;
    }
    
    static class Expense {
        private final String paidBy;
        private final double amount;
        private final List<String> participants;
        private final double perPerson;
        
        public Expense(String paidBy, double amount, List<String> participants, double perPerson) {
            this.paidBy = paidBy;
            this.amount = amount;
            this.participants = new ArrayList<>(participants);
            this.perPerson = perPerson;
        }
        
        public String getPaidBy() {
            return paidBy;
        }
        
        public double getAmount() {
            return amount;
        }
        
        public List<String> getParticipants() {
            return participants;
        }
        
        public double getPerPerson() {
            return perPerson;
        }
    }
}

