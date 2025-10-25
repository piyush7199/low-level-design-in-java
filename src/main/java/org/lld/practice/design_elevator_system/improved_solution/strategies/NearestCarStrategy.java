package org.lld.practice.design_elevator_system.improved_solution.strategies;

import org.lld.practice.design_elevator_system.improved_solution.Elevator;
import org.lld.practice.design_elevator_system.improved_solution.models.Direction;

import java.util.List;

public class NearestCarStrategy implements SchedulingStrategy {
    
    @Override
    public Elevator selectElevator(List<Elevator> elevators, int requestFloor, Direction direction) {
        Elevator nearestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : elevators) {
            if (elevator.isIdle()) {
                int distance = elevator.getDistanceToFloor(requestFloor);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestElevator = elevator;
                }
            }
        }
        
        // If no idle elevator, find the nearest one moving in the same direction
        if (nearestElevator == null) {
            for (Elevator elevator : elevators) {
                if (elevator.getDirection() == direction) {
                    int distance = elevator.getDistanceToFloor(requestFloor);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestElevator = elevator;
                    }
                }
            }
        }
        
        // If still no elevator, pick any
        if (nearestElevator == null && !elevators.isEmpty()) {
            nearestElevator = elevators.get(0);
        }
        
        return nearestElevator;
    }
}

