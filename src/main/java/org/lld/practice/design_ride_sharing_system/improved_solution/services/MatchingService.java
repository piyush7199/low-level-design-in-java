package org.lld.practice.design_ride_sharing_system.improved_solution.services;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Driver;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.Location;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.RideType;
import org.lld.practice.design_ride_sharing_system.improved_solution.strategies.DriverMatchingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingService {
    private final Map<String, Driver> drivers;
    private final DriverMatchingStrategy matchingStrategy;

    public MatchingService(DriverMatchingStrategy matchingStrategy) {
        this.drivers = new HashMap<>();
        this.matchingStrategy = matchingStrategy;
    }

    public void addDriver(Driver driver) {
        drivers.put(driver.getUserId(), driver);
    }

    public Driver findDriver(Location pickupLocation, RideType rideType) {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers.values()) {
            if (driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }

        if (availableDrivers.isEmpty()) {
            return null;
        }

        Driver selectedDriver = matchingStrategy.findBestDriver(availableDrivers, pickupLocation);
        
        if (selectedDriver != null) {
            System.out.println("[MatchingService] Matched driver: " + selectedDriver.getName() + 
                             " (Distance: " + String.format("%.2f", 
                             selectedDriver.getCurrentLocation().distanceTo(pickupLocation)) + " km)");
        }
        
        return selectedDriver;
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> available = new ArrayList<>();
        for (Driver driver : drivers.values()) {
            if (driver.isAvailable()) {
                available.add(driver);
            }
        }
        return available;
    }
}

