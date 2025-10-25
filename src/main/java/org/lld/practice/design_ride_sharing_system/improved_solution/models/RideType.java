package org.lld.practice.design_ride_sharing_system.improved_solution.models;

public enum RideType {
    ECONOMY(1.0, 5.0),
    PREMIUM(1.5, 8.0),
    SHARED(0.7, 3.0),
    XL(2.0, 10.0);

    private final double fareMultiplier;
    private final double baseFare;

    RideType(double fareMultiplier, double baseFare) {
        this.fareMultiplier = fareMultiplier;
        this.baseFare = baseFare;
    }

    public double getFareMultiplier() {
        return fareMultiplier;
    }

    public double getBaseFare() {
        return baseFare;
    }
}

