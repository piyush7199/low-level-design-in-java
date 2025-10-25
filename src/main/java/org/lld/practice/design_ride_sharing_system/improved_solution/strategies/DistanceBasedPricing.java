package org.lld.practice.design_ride_sharing_system.improved_solution.strategies;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;

public class DistanceBasedPricing implements PricingStrategy {
    private static final double PRICE_PER_KM = 2.5;

    @Override
    public double calculateFare(Ride ride) {
        double distance = ride.getDistance();
        double baseFare = ride.getRideType().getBaseFare();
        double multiplier = ride.getRideType().getFareMultiplier();
        
        double fare = baseFare + (distance * PRICE_PER_KM * multiplier);
        
        System.out.println("[Pricing] Distance: " + String.format("%.2f", distance) + " km");
        System.out.println("[Pricing] Base fare: $" + baseFare);
        System.out.println("[Pricing] Multiplier: " + multiplier + "x (" + ride.getRideType() + ")");
        
        return Math.round(fare * 100.0) / 100.0; // Round to 2 decimal places
    }
}

