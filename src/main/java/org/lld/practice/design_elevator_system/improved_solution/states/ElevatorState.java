package org.lld.practice.design_elevator_system.improved_solution.states;

public interface ElevatorState {
    void move();
    void openDoors();
    void closeDoors();
}

