package org.lld.practice.design_matching_engine.improved_solution.models;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a driver/delivery partner in the system.
 */
public class Driver {
    
    private final String driverId;
    private final String name;
    private final VehicleType vehicleType;
    
    private Location currentLocation;
    private DriverStatus status;
    private double rating;
    private int totalRides;
    private int acceptedRides;
    private Instant lastRideCompletedAt;
    private Instant availableSince;

    public Driver(String driverId, String name, VehicleType vehicleType, Location location) {
        this.driverId = Objects.requireNonNull(driverId);
        this.name = Objects.requireNonNull(name);
        this.vehicleType = Objects.requireNonNull(vehicleType);
        this.currentLocation = Objects.requireNonNull(location);
        this.status = DriverStatus.OFFLINE;
        this.rating = 4.5;
        this.totalRides = 0;
        this.acceptedRides = 0;
    }

    // ========== Status Management ==========

    public void goOnline() {
        this.status = DriverStatus.AVAILABLE;
        this.availableSince = Instant.now();
    }

    public void goOffline() {
        this.status = DriverStatus.OFFLINE;
        this.availableSince = null;
    }

    public void startRide() {
        this.status = DriverStatus.BUSY;
    }

    public void completeRide() {
        this.status = DriverStatus.AVAILABLE;
        this.lastRideCompletedAt = Instant.now();
        this.availableSince = Instant.now();
        this.totalRides++;
    }

    public void setPendingMatch() {
        this.status = DriverStatus.PENDING_MATCH;
    }

    // ========== Metrics ==========

    public void recordAcceptance(boolean accepted) {
        if (accepted) {
            acceptedRides++;
        }
    }

    public double getAcceptanceRate() {
        if (totalRides == 0) return 100.0;
        return (double) acceptedRides / totalRides * 100.0;
    }

    /**
     * Get how long the driver has been waiting for a ride (in seconds).
     */
    public long getIdleTimeSeconds() {
        if (availableSince == null) return 0;
        return Instant.now().getEpochSecond() - availableSince.getEpochSecond();
    }

    // ========== Getters and Setters ==========

    public String getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void updateLocation(Location location) {
        this.currentLocation = location;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == DriverStatus.AVAILABLE;
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

    @Override
    public String toString() {
        return String.format("Driver{id='%s', name='%s', vehicle=%s, status=%s, rating=%.1f}",
                driverId, name, vehicleType, status, rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(driverId, driver.driverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId);
    }
}

