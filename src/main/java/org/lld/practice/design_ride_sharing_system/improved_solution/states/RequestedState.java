package org.lld.practice.design_ride_sharing_system.improved_solution.states;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Ride;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.RideStatus;

public class RequestedState implements RideState {
    private final Ride ride;

    public RequestedState(Ride ride) {
        this.ride = ride;
    }

    @Override
    public void accept() {
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setState(new AcceptedState(ride));
        System.out.println("[Ride " + ride.getRideId() + "] Status: REQUESTED → ACCEPTED");
    }

    @Override
    public void start() {
        System.out.println("[Ride " + ride.getRideId() + "] Cannot start ride - must be accepted first");
    }

    @Override
    public void complete() {
        System.out.println("[Ride " + ride.getRideId() + "] Cannot complete ride - not started yet");
    }

    @Override
    public void cancel() {
        ride.setStatus(RideStatus.CANCELLED);
        ride.setState(new CancelledState(ride));
        System.out.println("[Ride " + ride.getRideId() + "] Status: REQUESTED → CANCELLED");
    }
}

