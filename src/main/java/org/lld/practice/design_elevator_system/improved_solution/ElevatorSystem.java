package org.lld.practice.design_elevator_system.improved_solution;

import org.lld.practice.design_elevator_system.improved_solution.models.Direction;
import org.lld.practice.design_elevator_system.improved_solution.models.Request;
import org.lld.practice.design_elevator_system.improved_solution.strategies.NearestCarStrategy;
import org.lld.practice.design_elevator_system.improved_solution.strategies.SchedulingStrategy;

import java.util.ArrayList;
import java.util.List;

public class ElevatorSystem {
    private final List<Elevator> elevators;
    private final SchedulingStrategy strategy;
    private final int totalFloors;

    public ElevatorSystem(int numberOfElevators, int totalFloors, int capacity) {
        this.elevators = new ArrayList<>();
        this.totalFloors = totalFloors;
        this.strategy = new NearestCarStrategy();
        
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(i + 1, totalFloors, capacity));
        }
        
        System.out.println("Elevator System initialized with " + numberOfElevators + " elevators.");
    }

    public void requestElevator(int floor, Direction direction) {
        System.out.println("\n[System] External request from floor " + floor + " going " + direction);
        
        Elevator bestElevator = strategy.selectElevator(elevators, floor, direction);
        if (bestElevator != null) {
            Request request = new Request(floor, direction, true);
            bestElevator.addRequest(request);
        } else {
            System.out.println("[System] No elevator available.");
        }
    }

    public void selectFloor(int elevatorId, int targetFloor) {
        if (elevatorId < 1 || elevatorId > elevators.size()) {
            System.out.println("[System] Invalid elevator ID: " + elevatorId);
            return;
        }
        
        Elevator elevator = elevators.get(elevatorId - 1);
        Request request = new Request(targetFloor, Direction.IDLE, false);
        elevator.addRequest(request);
    }

    public void step() {
        for (Elevator elevator : elevators) {
            elevator.move();
        }
    }

    public void displayStatus() {
        System.out.println("\n=== Elevator System Status ===");
        for (Elevator elevator : elevators) {
            System.out.println("Elevator " + elevator.getElevatorId() + ": Floor " + 
                elevator.getCurrentFloor() + ", Direction: " + elevator.getDirection() + 
                ", State: " + elevator.getCurrentState().getClass().getSimpleName());
        }
        System.out.println("==============================\n");
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}

