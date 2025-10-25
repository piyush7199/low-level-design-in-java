package org.lld.practice.design_ride_sharing_system.improved_solution.models;

public class Vehicle {
    private final String licensePlate;
    private final String model;
    private final String color;
    private final VehicleType vehicleType;

    public Vehicle(String licensePlate, String model, String color, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.color = color;
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public String toString() {
        return color + " " + model + " (" + licensePlate + ")";
    }
}

