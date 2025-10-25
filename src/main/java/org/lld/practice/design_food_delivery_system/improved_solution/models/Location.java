package org.lld.practice.design_food_delivery_system.improved_solution.models;

public class Location {
    private final double latitude;
    private final double longitude;
    private final String address;

    public Location(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public double distanceTo(Location other) {
        // Simplified distance calculation (Euclidean)
        // In production, use Haversine formula
        double latDiff = this.latitude - other.latitude;
        double lonDiff = this.longitude - other.longitude;
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff) * 111; // rough km conversion
    }

    @Override
    public String toString() {
        return address;
    }
}

