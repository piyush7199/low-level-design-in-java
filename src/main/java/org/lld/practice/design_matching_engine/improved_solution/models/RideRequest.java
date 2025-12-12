package org.lld.practice.design_matching_engine.improved_solution.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a ride request from a rider.
 */
public class RideRequest {
    
    private final String requestId;
    private final Rider rider;
    private final Location pickupLocation;
    private final Location dropoffLocation;
    private final VehicleType preferredVehicleType;
    private final Instant requestedAt;
    private final int passengerCount;

    public RideRequest(Rider rider, Location pickupLocation, Location dropoffLocation, 
                       VehicleType preferredVehicleType, int passengerCount) {
        this.requestId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.rider = Objects.requireNonNull(rider);
        this.pickupLocation = Objects.requireNonNull(pickupLocation);
        this.dropoffLocation = Objects.requireNonNull(dropoffLocation);
        this.preferredVehicleType = Objects.requireNonNull(preferredVehicleType);
        this.requestedAt = Instant.now();
        this.passengerCount = passengerCount;
    }

    public String getRequestId() {
        return requestId;
    }

    public Rider getRider() {
        return rider;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public VehicleType getPreferredVehicleType() {
        return preferredVehicleType;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    /**
     * Get estimated trip distance in km.
     */
    public double getTripDistance() {
        return pickupLocation.distanceTo(dropoffLocation);
    }

    /**
     * Get estimated trip duration in minutes.
     */
    public int getEstimatedDurationMinutes() {
        return pickupLocation.estimateETAMinutes(dropoffLocation);
    }

    @Override
    public String toString() {
        return String.format("RideRequest{id='%s', rider='%s', from=%s, to=%s, vehicle=%s}",
                requestId, rider.getName(), pickupLocation, dropoffLocation, preferredVehicleType);
    }
}

