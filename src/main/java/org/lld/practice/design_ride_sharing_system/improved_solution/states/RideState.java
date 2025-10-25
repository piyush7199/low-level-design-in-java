package org.lld.practice.design_ride_sharing_system.improved_solution.states;

public interface RideState {
    void accept();
    void start();
    void complete();
    void cancel();
}

