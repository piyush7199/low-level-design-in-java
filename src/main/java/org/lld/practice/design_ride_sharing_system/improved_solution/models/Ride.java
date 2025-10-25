package org.lld.practice.design_ride_sharing_system.improved_solution.models;

import org.lld.practice.design_ride_sharing_system.improved_solution.states.*;

import java.time.LocalDateTime;

public class Ride {
    private final String rideId;
    private final Rider rider;
    private final Driver driver;
    private final Location pickupLocation;
    private final Location dropoffLocation;
    private final RideType rideType;
    private final LocalDateTime requestTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double fare;
    private RideState currentState;
    private RideStatus status;

    public Ride(String rideId, Rider rider, Driver driver, Location pickupLocation, 
                Location dropoffLocation, RideType rideType) {
        this.rideId = rideId;
        this.rider = rider;
        this.driver = driver;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.rideType = rideType;
        this.requestTime = LocalDateTime.now();
        this.currentState = new RequestedState(this);
        this.status = RideStatus.REQUESTED;
        this.fare = 0.0;
    }

    public String getRideId() {
        return rideId;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public RideType getRideType() {
        return rideType;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public RideState getCurrentState() {
        return currentState;
    }

    public void setState(RideState state) {
        this.currentState = state;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public void acceptRide() {
        currentState.accept();
    }

    public void startRide() {
        currentState.start();
    }

    public void completeRide() {
        currentState.complete();
    }

    public void cancelRide() {
        currentState.cancel();
    }

    public double getDistance() {
        return pickupLocation.distanceTo(dropoffLocation);
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id='" + rideId + '\'' +
                ", rider=" + rider.getName() +
                ", driver=" + driver.getName() +
                ", type=" + rideType +
                ", status=" + status +
                ", fare=$" + String.format("%.2f", fare) +
                '}';
    }
}

