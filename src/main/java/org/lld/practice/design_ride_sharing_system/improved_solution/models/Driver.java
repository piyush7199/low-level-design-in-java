package org.lld.practice.design_ride_sharing_system.improved_solution.models;

public class Driver extends User {
    private final Vehicle vehicle;
    private Location currentLocation;
    private boolean available;
    private int totalRides;

    public Driver(String userId, String name, String phone, Vehicle vehicle, Location currentLocation) {
        super(userId, name, phone);
        this.vehicle = vehicle;
        this.currentLocation = currentLocation;
        this.available = true;
        this.totalRides = 0;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getTotalRides() {
        return totalRides;
    }

    public void incrementRides() {
        this.totalRides++;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", vehicle=" + vehicle.getModel() +
                ", rating=" + String.format("%.2f", rating) +
                ", available=" + available +
                '}';
    }
}

