package org.lld.practice.design_elevator_system.improved_solution;

import org.lld.practice.design_elevator_system.improved_solution.models.Direction;
import org.lld.practice.design_elevator_system.improved_solution.models.Request;
import org.lld.practice.design_elevator_system.improved_solution.states.*;

import java.util.*;

public class Elevator {
    private final int elevatorId;
    private int currentFloor;
    private Direction direction;
    private ElevatorState currentState;
    private final TreeSet<Integer> upRequests;
    private final TreeSet<Integer> downRequests;
    private final int maxFloor;
    private final int capacity;
    private int currentLoad;

    public Elevator(int elevatorId, int maxFloor, int capacity) {
        this.elevatorId = elevatorId;
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.currentState = new IdleState(this);
        this.upRequests = new TreeSet<>();
        this.downRequests = new TreeSet<>(Collections.reverseOrder());
        this.maxFloor = maxFloor;
        this.capacity = capacity;
        this.currentLoad = 0;
    }

    public void addRequest(Request request) {
        int floor = request.getTargetFloor();
        
        if (floor < 0 || floor > maxFloor) {
            System.out.println("[Elevator " + elevatorId + "] Invalid floor: " + floor);
            return;
        }
        
        if (floor > currentFloor) {
            upRequests.add(floor);
        } else if (floor < currentFloor) {
            downRequests.add(floor);
        }
        
        System.out.println("[Elevator " + elevatorId + "] Request added for floor " + floor);
    }

    public void move() {
        currentState.move();
    }

    public void processCurrentFloor() {
        boolean hasRequest = false;
        
        if (direction == Direction.UP && upRequests.contains(currentFloor)) {
            hasRequest = true;
            upRequests.remove(currentFloor);
        } else if (direction == Direction.DOWN && downRequests.contains(currentFloor)) {
            hasRequest = true;
            downRequests.remove(currentFloor);
        }
        
        if (hasRequest) {
            currentState.openDoors();
            currentState.closeDoors();
        }
        
        if (upRequests.isEmpty() && downRequests.isEmpty()) {
            direction = Direction.IDLE;
            setState(new IdleState(this));
        }
    }

    public void moveUp() {
        if (currentFloor < maxFloor) {
            currentFloor++;
            direction = Direction.UP;
            System.out.println("[Elevator " + elevatorId + "] Moving UP to floor " + currentFloor);
        }
    }

    public void moveDown() {
        if (currentFloor > 0) {
            currentFloor--;
            direction = Direction.DOWN;
            System.out.println("[Elevator " + elevatorId + "] Moving DOWN to floor " + currentFloor);
        }
    }

    public void determineDirection() {
        if (!upRequests.isEmpty() && (direction == Direction.UP || direction == Direction.IDLE)) {
            direction = Direction.UP;
            setState(new MovingUpState(this));
        } else if (!downRequests.isEmpty() && (direction == Direction.DOWN || direction == Direction.IDLE)) {
            direction = Direction.DOWN;
            setState(new MovingDownState(this));
        } else if (!upRequests.isEmpty()) {
            direction = Direction.UP;
            setState(new MovingUpState(this));
        } else if (!downRequests.isEmpty()) {
            direction = Direction.DOWN;
            setState(new MovingDownState(this));
        } else {
            direction = Direction.IDLE;
            setState(new IdleState(this));
        }
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setState(ElevatorState state) {
        this.currentState = state;
    }

    public ElevatorState getCurrentState() {
        return currentState;
    }

    public TreeSet<Integer> getUpRequests() {
        return upRequests;
    }

    public TreeSet<Integer> getDownRequests() {
        return downRequests;
    }

    public boolean isIdle() {
        return direction == Direction.IDLE && upRequests.isEmpty() && downRequests.isEmpty();
    }

    public int getDistanceToFloor(int floor) {
        return Math.abs(currentFloor - floor);
    }
}

