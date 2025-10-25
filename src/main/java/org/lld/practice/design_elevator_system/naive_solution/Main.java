package org.lld.practice.design_elevator_system.naive_solution;

public class Main {
    public static void main(String[] args) {
        Elevator elevator = new Elevator(10);
        
        System.out.println("=== Scenario: Multiple Floor Requests ===\n");
        
        elevator.requestFloor(5);
        elevator.requestFloor(3);
        elevator.requestFloor(7);
        
        // Simulate elevator movement
        for (int i = 0; i < 15; i++) {
            elevator.move();
            try {
                Thread.sleep(500); // Simulate time delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

