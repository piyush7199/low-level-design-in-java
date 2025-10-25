package org.lld.practice.design_food_delivery_system.improved_solution.strategies;

public class WalletPayment implements PaymentMethod {
    
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[WalletPayment] Processing wallet payment of $" + amount);
        // Simulate payment processing
        return true;
    }
}

