package org.lld.practice.design_elevator_system.improved_solution;

import org.lld.practice.design_elevator_system.improved_solution.models.Direction;

public class Main {
    public static void main(String[] args) {
        ElevatorSystem system = new ElevatorSystem(2, 10, 10);
        
        System.out.println("\n=== Scenario: Multiple Requests ===");
        
        // External requests from different floors
        system.requestElevator(5, Direction.UP);
        system.requestElevator(3, Direction.DOWN);
        system.requestElevator(7, Direction.UP);
        
        // Internal requests
        system.selectFloor(1, 8);
        system.selectFloor(2, 1);
        
        // Simulate elevator movement
        System.out.println("\n=== Simulating Elevator Movement ===");
        for (int i = 0; i < 20; i++) {
            system.step();
            
            if (i % 5 == 0) {
                system.displayStatus();
            }
            
            try {
                Thread.sleep(800); // Simulate time delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\n=== Final Status ===");
        system.displayStatus();
    }
}

