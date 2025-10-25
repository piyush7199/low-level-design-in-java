package org.lld.practice.design_food_delivery_system.improved_solution.services;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.strategies.PaymentMethod;

public class PaymentService {
    
    public boolean processPayment(Order order, PaymentMethod paymentMethod) {
        System.out.println("[PaymentService] Processing payment for order: " + order.getOrderId());
        boolean success = paymentMethod.processPayment(order.getTotalAmount());
        
        if (success) {
            System.out.println("[PaymentService] Payment successful: $" + order.getTotalAmount());
        } else {
            System.out.println("[PaymentService] Payment failed");
        }
        
        return success;
    }
}

