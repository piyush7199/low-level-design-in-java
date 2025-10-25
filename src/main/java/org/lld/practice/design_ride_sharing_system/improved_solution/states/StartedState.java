package org.lld.practice.design_ride_sharing_system.improved_solution.states;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.RideStatus;

import java.time.LocalDateTime;

public class StartedState implements RideState {
    private final Ride ride;

    public StartedState(Ride ride) {
        this.ride = ride;
    }

    @Override
    public void accept() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride already started");
    }

    @Override
    public void start() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride already started");
    }

    @Override
    public void complete() {
        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());
        ride.setState(new CompletedState(ride));
        System.out.println("[Ride " + ride.getRideId() + "] Status: STARTED → COMPLETED");
    }

    @Override
    public void cancel() {
        System.out.println("[Ride " + ride.getRideId() + "] Cannot cancel ride - already started");
    }
}

