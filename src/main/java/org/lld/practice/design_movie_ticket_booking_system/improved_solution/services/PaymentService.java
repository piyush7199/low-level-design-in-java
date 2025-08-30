package org.lld.practice.design_movie_ticket_booking_system.improved_solution.services;

import org.lld.practice.design_movie_ticket_booking_system.improved_solution.strategies.PaymentStrategy;

public class PaymentService {
    public boolean processPayment(double amount, PaymentStrategy strategy) {
        return strategy.processPayment(amount);
    }
}
