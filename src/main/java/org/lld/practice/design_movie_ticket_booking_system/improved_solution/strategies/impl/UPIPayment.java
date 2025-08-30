package org.lld.practice.design_movie_ticket_booking_system.improved_solution.strategies.impl;

import org.lld.practice.design_movie_ticket_booking_system.improved_solution.strategies.PaymentStrategy;

public class UPIPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing UPI card payment of $" + amount);
        return true; // Assume success
    }
}
