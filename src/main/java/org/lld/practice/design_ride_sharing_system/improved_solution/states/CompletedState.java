package org.lld.practice.design_ride_sharing_system.improved_solution.states;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;

public class CompletedState implements RideState {
    private final Ride ride;

    public CompletedState(Ride ride) {
        this.ride = ride;
    }

    @Override
    public void accept() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride already completed");
    }

    @Override
    public void start() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride already completed");
    }

    @Override
    public void complete() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride already completed");
    }

    @Override
    public void cancel() {
        System.out.println("[Ride " + ride.getRideId() + "] Cannot cancel completed ride");
    }
}

