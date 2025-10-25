package org.lld.practice.design_elevator_system.naive_solution;

import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private int currentFloor;
    private String direction; // "UP", "DOWN", "IDLE"
    private List<Integer> requests;
    private int maxFloor;
    
    public Elevator(int maxFloor) {
        this.currentFloor = 0;
        this.direction = "IDLE";
        this.requests = new ArrayList<>();
        this.maxFloor = maxFloor;
    }
    
    public void requestFloor(int floor) {
        if (floor < 0 || floor > maxFloor) {
            System.out.println("Invalid floor: " + floor);
            return;
        }
        
        if (!requests.contains(floor)) {
            requests.add(floor);
            System.out.println("Floor " + floor + " requested.");
        }
    }
    
    public void move() {
        if (requests.isEmpty()) {
            direction = "IDLE";
            System.out.println("Elevator is idle at floor " + currentFloor);
            return;
        }
        
        // Simple FCFS approach
        int targetFloor = requests.get(0);
        
        if (targetFloor > currentFloor) {
            direction = "UP";
            currentFloor++;
            System.out.println("Moving UP. Current floor: " + currentFloor);
        } else if (targetFloor < currentFloor) {
            direction = "DOWN";
            currentFloor--;
            System.out.println("Moving DOWN. Current floor: " + currentFloor);
        }
        
        if (currentFloor == targetFloor) {
            openDoor();
            closeDoor();
            requests.remove(0);
        }
    }
    
    public void openDoor() {
        System.out.println("Door opening at floor " + currentFloor);
    }
    
    public void closeDoor() {
        System.out.println("Door closing at floor " + currentFloor);
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public String getDirection() {
        return direction;
    }
}

