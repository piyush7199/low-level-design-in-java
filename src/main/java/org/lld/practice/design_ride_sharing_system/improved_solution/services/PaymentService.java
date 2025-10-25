package org.lld.practice.design_ride_sharing_system.improved_solution.services;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;
import org.lld.practice.design_ride_sharing_system.improved_solution.strategies.PricingStrategy;

public class PaymentService {
    private final PricingStrategy pricingStrategy;

    public PaymentService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double calculateFare(Ride ride) {
        return pricingStrategy.calculateFare(ride);
    }

    public boolean processPayment(Ride ride) {
        System.out.println("[PaymentService] Processing payment for ride: " + ride.getRideId());
        // Simulate payment processing
        return true;
    }
}

