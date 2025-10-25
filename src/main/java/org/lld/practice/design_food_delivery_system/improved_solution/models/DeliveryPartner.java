package org.lld.practice.design_food_delivery_system.improved_solution.models;

public class DeliveryPartner extends User {
    private Location currentLocation;
    private boolean available;
    private double rating;
    private int totalDeliveries;

    public DeliveryPartner(String userId, String name, String email, String phone, Location currentLocation) {
        super(userId, name, email, phone);
        this.currentLocation = currentLocation;
        this.available = true;
        this.rating = 5.0;
        this.totalDeliveries = 0;
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

    public double getRating() {
        return rating;
    }

    public void updateRating(double newRating) {
        this.rating = ((this.rating * totalDeliveries) + newRating) / (totalDeliveries + 1);
    }

    public void incrementDeliveries() {
        this.totalDeliveries++;
    }

    public int getTotalDeliveries() {
        return totalDeliveries;
    }
}

