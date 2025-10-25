package org.lld.practice.design_elevator_system.improved_solution.models;

public class Request {
    private final int targetFloor;
    private final Direction direction;
    private final boolean isExternal;

    public Request(int targetFloor, Direction direction, boolean isExternal) {
        this.targetFloor = targetFloor;
        this.direction = direction;
        this.isExternal = isExternal;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isExternal() {
        return isExternal;
    }
}

