package org.lld.practice.design_expense_sharing_system.improved_solution.services;

import org.lld.practice.design_expense_sharing_system.improved_solution.models.Balance;
import org.lld.practice.design_expense_sharing_system.improved_solution.models.Expense;
import org.lld.practice.design_expense_sharing_system.improved_solution.models.ExpenseStatus;
import org.lld.practice.design_expense_sharing_system.improved_solution.models.Group;
import org.lld.practice.design_expense_sharing_system.improved_solution.strategies.EqualSplitStrategy;
import org.lld.practice.design_expense_sharing_system.improved_solution.strategies.SplitStrategy;

import java.util.*;

/**
 * Service for managing expense sharing.
 * Uses Strategy pattern for different split types and graph algorithms for balance tracking.
 */
public class ExpenseSharingService {
    private final Map<String, Group> groups;
    private final Map<String, List<Expense>> groupExpenses;
    private final Map<String, BalanceGraph> groupBalances;
    private SplitStrategy defaultSplitStrategy;
    
    public ExpenseSharingService() {
        this.groups = new HashMap<>();
        this.groupExpenses = new HashMap<>();
        this.groupBalances = new HashMap<>();
        this.defaultSplitStrategy = new EqualSplitStrategy();
    }
    
    /**
     * Creates a new group.
     * 
     * @param name Group name
     * @param createdBy User ID of creator
     * @return The created group
     */
    public Group createGroup(String name, String createdBy) {
        Group group = new Group(name, createdBy);
        groups.put(group.getGroupId(), group);
        groupExpenses.put(group.getGroupId(), new ArrayList<>());
        groupBalances.put(group.getGroupId(), new BalanceGraph());
        return group;
    }
    
    /**
     * Adds a member to a group.
     * 
     * @param groupId The group ID
     * @param userId The user ID to add
     */
    public void addMemberToGroup(String groupId, String userId) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        group.addMember(userId);
    }
    
    /**
     * Adds an expense to a group with equal split.
     * 
     * @param groupId The group ID
     * @param paidBy User who paid
     * @param amount Expense amount
     * @param description Expense description
     * @param participants List of participants
     * @return The created expense
     */
    public Expense addExpense(String groupId, String paidBy, double amount,
                            String description, List<String> participants) {
        return addExpense(groupId, paidBy, amount, description, participants, 
                         defaultSplitStrategy, new HashMap<>());
    }
    
    /**
     * Adds an expense to a group with custom split strategy.
     * 
     * @param groupId The group ID
     * @param paidBy User who paid
     * @param amount Expense amount
     * @param description Expense description
     * @param participants List of participants
     * @param splitStrategy Split strategy to use
     * @param splitParams Parameters for split strategy
     * @return The created expense
     */
    public Expense addExpense(String groupId, String paidBy, double amount,
                            String description, List<String> participants,
                            SplitStrategy splitStrategy, Map<String, Object> splitParams) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        
        // Validate participants are group members
        for (String participant : participants) {
            if (!group.hasMember(participant)) {
                throw new IllegalArgumentException("Participant " + participant + " is not a group member");
            }
        }
        
        // Split the expense
        Map<String, Double> splits = splitStrategy.split(amount, paidBy, participants, splitParams);
        
        // Create expense
        Expense expense = new Expense(groupId, paidBy, amount, description, splits);
        groupExpenses.get(groupId).add(expense);
        
        // Update balances
        updateBalances(groupId, expense);
        
        return expense;
    }
    
    /**
     * Updates balances based on an expense.
     * 
     * @param groupId The group ID
     * @param expense The expense
     */
    private void updateBalances(String groupId, Expense expense) {
        BalanceGraph balanceGraph = groupBalances.get(groupId);
        String paidBy = expense.getPaidBy();
        
        for (Map.Entry<String, Double> split : expense.getSplits().entrySet()) {
            String participant = split.getKey();
            double amount = split.getValue();
            
            if (!participant.equals(paidBy)) {
                // Participant owes the person who paid
                balanceGraph.addDebt(participant, paidBy, amount);
            }
        }
        
        // Simplify debts
        balanceGraph.simplifyDebts();
    }
    
    /**
     * Gets balances for a group.
     * 
     * @param groupId The group ID
     * @return List of balances
     */
    public List<Balance> getGroupBalances(String groupId) {
        BalanceGraph balanceGraph = groupBalances.get(groupId);
        if (balanceGraph == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        return balanceGraph.getAllBalances();
    }
    
    /**
     * Gets balances for a specific user in a group.
     * 
     * @param groupId The group ID
     * @param userId The user ID
     * @return List of balances for the user
     */
    public List<Balance> getUserBalances(String groupId, String userId) {
        BalanceGraph balanceGraph = groupBalances.get(groupId);
        if (balanceGraph == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        return balanceGraph.getBalancesForUser(userId);
    }
    
    /**
     * Settles an expense (marks as paid).
     * 
     * @param expenseId The expense ID
     */
    public void settleExpense(String expenseId) {
        for (List<Expense> expenses : groupExpenses.values()) {
            for (Expense expense : expenses) {
                if (expense.getExpenseId().equals(expenseId)) {
                    expense.setStatus(ExpenseStatus.SETTLED);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Expense not found: " + expenseId);
    }
    
    /**
     * Gets expense history for a group.
     * 
     * @param groupId The group ID
     * @return List of expenses
     */
    public List<Expense> getGroupExpenses(String groupId) {
        return new ArrayList<>(groupExpenses.getOrDefault(groupId, new ArrayList<>()));
    }
}

