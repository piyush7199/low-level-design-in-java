package org.lld.practice.design_matching_engine.improved_solution.models;

/**
 * Enum representing different vehicle types.
 */
public enum VehicleType {
    BIKE(1, 1.0),
    AUTO(3, 1.2),
    CAR(4, 1.5),
    SUV(6, 2.0),
    PREMIUM(4, 3.0);

    private final int capacity;
    private final double priceMultiplier;

    VehicleType(int capacity, double priceMultiplier) {
        this.capacity = capacity;
        this.priceMultiplier = priceMultiplier;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}

