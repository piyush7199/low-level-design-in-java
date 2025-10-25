package org.lld.practice.design_ride_sharing_system.improved_solution.states;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;

public class CancelledState implements RideState {
    private final Ride ride;

    public CancelledState(Ride ride) {
        this.ride = ride;
    }

    @Override
    public void accept() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride is cancelled");
    }

    @Override
    public void start() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride is cancelled");
    }

    @Override
    public void complete() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride is cancelled");
    }

    @Override
    public void cancel() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride already cancelled");
    }
}

