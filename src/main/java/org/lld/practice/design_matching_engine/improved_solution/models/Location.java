package org.lld.practice.design_matching_engine.improved_solution.models;

/**
 * Represents a geographic location with latitude and longitude.
 * Provides distance calculation using Haversine formula.
 */
public class Location {
    
    private static final double EARTH_RADIUS_KM = 6371.0;
    
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Calculate distance to another location using Haversine formula.
     * 
     * @param other The other location
     * @return Distance in kilometers
     */
    public double distanceTo(Location other) {
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(this.latitude)) * 
                   Math.cos(Math.toRadians(other.latitude)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    /**
     * Estimate ETA in minutes based on distance.
     * Assumes average speed of 30 km/h in city traffic.
     */
    public int estimateETAMinutes(Location other) {
        double distance = distanceTo(other);
        double averageSpeedKmPerHour = 30.0;
        return (int) Math.ceil((distance / averageSpeedKmPerHour) * 60);
    }

    @Override
    public String toString() {
        return String.format("(%.4f, %.4f)", latitude, longitude);
    }
}

