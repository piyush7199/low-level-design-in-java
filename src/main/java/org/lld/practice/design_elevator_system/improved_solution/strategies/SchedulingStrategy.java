package org.lld.practice.design_elevator_system.improved_solution.strategies;

import org.lld.practice.design_elevator_system.improved_solution.Elevator;
import org.lld.practice.design_elevator_system.improved_solution.models.Direction;

import java.util.List;

public interface SchedulingStrategy {
    Elevator selectElevator(List<Elevator> elevators, int requestFloor, Direction direction);
}

