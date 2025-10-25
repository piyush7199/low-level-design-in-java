package org.lld.practice.design_ride_sharing_system.improved_solution.strategies;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Driver;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.Location;

import java.util.List;

public class NearestDriverStrategy implements DriverMatchingStrategy {
    
    @Override
    public Driver findBestDriver(List<Driver> availableDrivers, Location pickupLocation) {
        if (availableDrivers.isEmpty()) {
            return null;
        }

        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            double distance = driver.getCurrentLocation().distanceTo(pickupLocation);
            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }

        return nearestDriver;
    }
}

