package org.lld.practice.design_ride_sharing_system.improved_solution.strategies;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;

public interface PricingStrategy {
    double calculateFare(Ride ride);
}

