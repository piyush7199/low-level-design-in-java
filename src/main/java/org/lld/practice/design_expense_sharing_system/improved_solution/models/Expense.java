package org.lld.practice.design_expense_sharing_system.improved_solution.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents an expense with split information.
 */
public class Expense {
    private final String expenseId;
    private final String groupId;
    private final String paidBy;
    private final double amount;
    private final String description;
    private final LocalDateTime createdAt;
    private final Map<String, Double> splits; // userId -> amount owed
    private ExpenseStatus status;
    
    public Expense(String groupId, String paidBy, double amount, String description,
                   Map<String, Double> splits) {
        this.expenseId = UUID.randomUUID().toString();
        this.groupId = groupId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.splits = new HashMap<>(splits);
        this.status = ExpenseStatus.ACTIVE;
    }
    
    public String getExpenseId() {
        return expenseId;
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public String getPaidBy() {
        return paidBy;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public Map<String, Double> getSplits() {
        return new HashMap<>(splits);
    }
    
    public double getSplitForUser(String userId) {
        return splits.getOrDefault(userId, 0.0);
    }
    
    public ExpenseStatus getStatus() {
        return status;
    }
    
    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }
}

