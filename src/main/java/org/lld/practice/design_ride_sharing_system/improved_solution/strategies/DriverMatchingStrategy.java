package org.lld.practice.design_ride_sharing_system.improved_solution.strategies;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Driver;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.Location;

import java.util.List;

public interface DriverMatchingStrategy {
    Driver findBestDriver(List<Driver> availableDrivers, Location pickupLocation);
}

