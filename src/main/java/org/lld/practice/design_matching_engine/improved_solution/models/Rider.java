package org.lld.practice.design_matching_engine.improved_solution.models;

import java.util.Objects;

/**
 * Represents a rider/customer requesting a ride.
 */
public class Rider {
    
    private final String riderId;
    private final String name;
    private final String phoneNumber;
    private double rating;
    private int totalRides;

    public Rider(String riderId, String name, String phoneNumber) {
        this.riderId = Objects.requireNonNull(riderId);
        this.name = Objects.requireNonNull(name);
        this.phoneNumber = phoneNumber;
        this.rating = 5.0;
        this.totalRides = 0;
    }

    public String getRiderId() {
        return riderId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = Math.max(1.0, Math.min(5.0, rating));
    }

    public int getTotalRides() {
        return totalRides;
    }

    public void incrementRides() {
        this.totalRides++;
    }

    @Override
    public String toString() {
        return String.format("Rider{id='%s', name='%s', rating=%.1f}", riderId, name, rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rider rider = (Rider) o;
        return Objects.equals(riderId, rider.riderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riderId);
    }
}

