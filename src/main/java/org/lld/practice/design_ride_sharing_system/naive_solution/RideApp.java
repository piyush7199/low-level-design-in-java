package org.lld.practice.design_ride_sharing_system.naive_solution;

import java.util.*;

public class RideApp {
    private Map<String, Rider> riders = new HashMap<>();
    private Map<String, Driver> drivers = new HashMap<>();
    private List<Ride> rides = new ArrayList<>();
    
    public void registerRider(Rider rider) {
        riders.put(rider.getId(), rider);
        System.out.println("Rider registered: " + rider.getName());
    }
    
    public void registerDriver(Driver driver) {
        drivers.put(driver.getId(), driver);
        System.out.println("Driver registered: " + driver.getName());
    }
    
    public Ride requestRide(String riderId, Location pickup, Location dropoff, String rideType) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            System.out.println("Rider not found");
            return null;
        }
        
        Driver driver = findNearestDriver(pickup);
        if (driver == null) {
            System.out.println("No drivers available");
            return null;
        }
        
        double fare = calculateFare(pickup, dropoff, rideType);
        Ride ride = new Ride("RIDE" + rides.size(), rider, driver, pickup, dropoff, fare);
        rides.add(ride);
        
        driver.setAvailable(false);
        System.out.println("Ride requested: " + ride.getId() + ", Fare: $" + fare);
        return ride;
    }
    
    public void completeRide(String rideId) {
        for (Ride ride : rides) {
            if (ride.getId().equals(rideId)) {
                ride.setStatus("COMPLETED");
                ride.getDriver().setAvailable(true);
                System.out.println("Ride completed: " + rideId);
                return;
            }
        }
    }
    
    private Driver findNearestDriver(Location location) {
        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Driver driver : drivers.values()) {
            if (driver.isAvailable()) {
                double distance = calculateDistance(location, driver.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = driver;
                }
            }
        }
        return nearest;
    }
    
    private double calculateFare(Location pickup, Location dropoff, String rideType) {
        double distance = calculateDistance(pickup, dropoff);
        double baseFare = 5.0;
        double perKm = 2.0;
        
        if ("PREMIUM".equals(rideType)) {
            perKm = 3.5;
        }
        
        return baseFare + (distance * perKm);
    }
    
    private double calculateDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(a.getLat() - b.getLat(), 2) + Math.pow(a.getLng() - b.getLng(), 2)) * 100;
    }
}

class Rider {
    private String id;
    private String name;
    
    public Rider(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
}

class Driver {
    private String id;
    private String name;
    private Location location;
    private boolean available;
    
    public Driver(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.available = true;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public Location getLocation() { return location; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

class Ride {
    private String id;
    private Rider rider;
    private Driver driver;
    private Location pickup;
    private Location dropoff;
    private double fare;
    private String status;
    
    public Ride(String id, Rider rider, Driver driver, Location pickup, Location dropoff, double fare) {
        this.id = id;
        this.rider = rider;
        this.driver = driver;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.fare = fare;
        this.status = "REQUESTED";
    }
    
    public String getId() { return id; }
    public Driver getDriver() { return driver; }
    public void setStatus(String status) { this.status = status; }
}

class Location {
    private double lat;
    private double lng;
    
    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    
    public double getLat() { return lat; }
    public double getLng() { return lng; }
}

