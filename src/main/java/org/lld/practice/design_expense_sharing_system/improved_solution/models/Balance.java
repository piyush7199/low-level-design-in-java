package org.lld.practice.design_expense_sharing_system.improved_solution.models;

/**
 * Represents a balance between two users.
 */
public class Balance {
    private final String fromUserId;
    private final String toUserId;
    private double amount; // Amount fromUserId owes toUserId
    
    public Balance(String fromUserId, String toUserId, double amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }
    
    public String getFromUserId() {
        return fromUserId;
    }
    
    public String getToUserId() {
        return toUserId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void addAmount(double amount) {
        this.amount += amount;
    }
    
    public void subtractAmount(double amount) {
        this.amount -= amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
}

