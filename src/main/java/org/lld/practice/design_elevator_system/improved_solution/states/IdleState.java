package org.lld.practice.design_elevator_system.improved_solution.states;

import org.lld.practice.design_elevator_system.improved_solution.Elevator;

public class IdleState implements ElevatorState {
    private final Elevator elevator;

    public IdleState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void move() {
        elevator.determineDirection();
    }

    @Override
    public void openDoors() {
        System.out.println("[Elevator " + elevator.getElevatorId() + "] Doors opening at floor " + elevator.getCurrentFloor());
    }

    @Override
    public void closeDoors() {
        System.out.println("[Elevator " + elevator.getElevatorId() + "] Doors closing at floor " + elevator.getCurrentFloor());
    }
}

