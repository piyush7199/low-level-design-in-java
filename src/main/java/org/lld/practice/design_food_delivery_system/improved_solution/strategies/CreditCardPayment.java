package org.lld.practice.design_food_delivery_system.improved_solution.strategies;

public class CreditCardPayment implements PaymentMethod {
    
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[CreditCardPayment] Processing credit card payment of $" + amount);
        // Simulate payment processing
        return true;
    }
}

