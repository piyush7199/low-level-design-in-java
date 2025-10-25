package org.lld.practice.design_elevator_system.improved_solution.states;

import org.lld.practice.design_elevator_system.improved_solution.Elevator;

public class MovingDownState implements ElevatorState {
    private final Elevator elevator;

    public MovingDownState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void move() {
        if (!elevator.getDownRequests().isEmpty()) {
            int nextFloor = elevator.getDownRequests().first();
            if (elevator.getCurrentFloor() > nextFloor) {
                elevator.moveDown();
            }
            elevator.processCurrentFloor();
        } else {
            elevator.determineDirection();
        }
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

