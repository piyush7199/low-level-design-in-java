package org.lld.practice.design_matching_engine.improved_solution.services;

import org.lld.practice.design_matching_engine.improved_solution.models.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing drivers.
 */
public class DriverService {
    
    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();

    public void registerDriver(Driver driver) {
        drivers.put(driver.getDriverId(), driver);
    }

    public Optional<Driver> getDriver(String driverId) {
        return Optional.ofNullable(drivers.get(driverId));
    }

    public void updateDriverLocation(String driverId, Location location) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.updateLocation(location);
        }
    }

    public void setDriverOnline(String driverId) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.goOnline();
            System.out.printf("🟢 Driver %s is now ONLINE%n", driver.getName());
        }
    }

    public void setDriverOffline(String driverId) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.goOffline();
            System.out.printf("🔴 Driver %s is now OFFLINE%n", driver.getName());
        }
    }

    /**
     * Get all available drivers.
     */
    public List<Driver> getAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .toList();
    }

    /**
     * Get available drivers of a specific vehicle type.
     */
    public List<Driver> getAvailableDrivers(VehicleType vehicleType) {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .filter(d -> d.getVehicleType() == vehicleType)
                .toList();
    }

    /**
     * Get count of available drivers.
     */
    public int getAvailableDriverCount() {
        return (int) drivers.values().stream()
                .filter(Driver::isAvailable)
                .count();
    }

    /**
     * Get all registered drivers.
     */
    public Collection<Driver> getAllDrivers() {
        return Collections.unmodifiableCollection(drivers.values());
    }
}

