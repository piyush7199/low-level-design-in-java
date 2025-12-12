package org.lld.practice.design_matching_engine.naive_solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive implementation of a Matching Engine.
 * 
 * This demonstrates common anti-patterns:
 * - O(n) linear search through all drivers
 * - Single matching criterion (distance only)
 * - No fairness mechanism
 * - Not thread-safe
 * - No spatial indexing
 * 
 * DO NOT use this pattern in production!
 */
public class SimpleMatchingEngine {
    
    private final List<Driver> drivers = new ArrayList<>();
    
    public void addDriver(Driver driver) {
        drivers.add(driver);
    }
    
    /**
     * Find nearest available driver.
     * 
     * Problems:
     * - O(n) iteration through ALL drivers
     * - Only considers distance
     * - Same nearby driver always matched (unfair)
     * - Not thread-safe
     */
    public Driver findDriver(double pickupLat, double pickupLon, String vehicleType) {
        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Driver driver : drivers) {
            // Check availability
            if (!driver.isAvailable()) {
                continue;
            }
            
            // Check vehicle type match
            if (!driver.getVehicleType().equals(vehicleType)) {
                continue;
            }
            
            // Calculate distance (simplified)
            double distance = calculateDistance(
                    driver.getLatitude(), driver.getLongitude(),
                    pickupLat, pickupLon);
            
            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }
        
        if (nearestDriver != null) {
            nearestDriver.setAvailable(false);  // Race condition!
            System.out.printf("✅ Matched driver %s (%.2f km away)%n", 
                    nearestDriver.getName(), minDistance);
        } else {
            System.out.println("❌ No drivers available");
        }
        
        return nearestDriver;
    }
    
    /**
     * Simple Haversine distance calculation.
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
    
    // Simple Driver class
    static class Driver {
        private final String id;
        private final String name;
        private double latitude;
        private double longitude;
        private String vehicleType;
        private boolean available;
        private double rating;
        
        public Driver(String id, String name, double lat, double lon, String vehicleType) {
            this.id = id;
            this.name = name;
            this.latitude = lat;
            this.longitude = lon;
            this.vehicleType = vehicleType;
            this.available = true;
            this.rating = 4.5;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getVehicleType() { return vehicleType; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
        public double getRating() { return rating; }
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) {
        System.out.println("=== Naive Matching Engine Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        SimpleMatchingEngine engine = new SimpleMatchingEngine();
        
        // Add drivers around downtown (San Francisco coordinates)
        engine.addDriver(new Driver("D1", "Alice", 37.7749, -122.4194, "CAR"));
        engine.addDriver(new Driver("D2", "Bob", 37.7751, -122.4180, "CAR"));    // Closest
        engine.addDriver(new Driver("D3", "Charlie", 37.7760, -122.4200, "SUV"));
        engine.addDriver(new Driver("D4", "Diana", 37.7740, -122.4170, "CAR"));
        engine.addDriver(new Driver("D5", "Eve", 37.7755, -122.4195, "CAR"));    // Has 5.0 rating
        
        double pickupLat = 37.7750;
        double pickupLon = -122.4185;
        
        System.out.println("Pickup location: (37.7750, -122.4185)");
        System.out.println("Looking for a CAR...\n");
        
        // Request 1
        System.out.println("Request 1:");
        Driver driver1 = engine.findDriver(pickupLat, pickupLon, "CAR");
        
        // Request 2 - Same location, Bob is now busy
        System.out.println("\nRequest 2 (same location, Bob now busy):");
        Driver driver2 = engine.findDriver(pickupLat, pickupLon, "CAR");
        
        // Request 3 - Looking for SUV
        System.out.println("\nRequest 3 (looking for SUV):");
        Driver driver3 = engine.findDriver(pickupLat, pickupLon, "SUV");
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. O(n) search through all drivers every time");
        System.out.println("2. Only distance matters - ignored Eve's 5.0 rating!");
        System.out.println("3. No fairness - closest driver always wins");
        System.out.println("4. Race condition in setAvailable() - not thread-safe");
        System.out.println("5. No match confirmation flow (accept/reject)");
        System.out.println("6. No scoring based on driver quality metrics");
    }
}

