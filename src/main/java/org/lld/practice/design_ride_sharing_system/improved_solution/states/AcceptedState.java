package org.lld.practice.design_ride_sharing_system.improved_solution.states;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.RideStatus;

import java.time.LocalDateTime;

public class AcceptedState implements RideState {
    private final Ride ride;

    public AcceptedState(Ride ride) {
        this.ride = ride;
    }

    @Override
    public void accept() {
        System.out.println("[Ride " + ride.getRideId() + "] Ride already accepted");
    }

    @Override
    public void start() {
        ride.setStatus(RideStatus.STARTED);
        ride.setStartTime(LocalDateTime.now());
        ride.setState(new StartedState(ride));
        System.out.println("[Ride " + ride.getRideId() + "] Status: ACCEPTED → STARTED");
    }

    @Override
    public void complete() {
        System.out.println("[Ride " + ride.getRideId() + "] Cannot complete ride - not started yet");
    }

    @Override
    public void cancel() {
        ride.setStatus(RideStatus.CANCELLED);
        ride.setState(new CancelledState(ride));
        System.out.println("[Ride " + ride.getRideId() + "] Status: ACCEPTED → CANCELLED");
    }
}

