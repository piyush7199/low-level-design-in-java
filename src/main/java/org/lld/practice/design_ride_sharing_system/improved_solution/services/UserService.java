package org.lld.practice.design_ride_sharing_system.improved_solution.services;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.Driver;
import org.lld.practice.design_ride_sharing_system.improved_solution.models.Rider;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, Rider> riders;
    private final Map<String, Driver> drivers;

    public UserService() {
        this.riders = new HashMap<>();
        this.drivers = new HashMap<>();
    }

    public void registerRider(Rider rider) {
        riders.put(rider.getUserId(), rider);
        System.out.println("[UserService] Rider registered: " + rider.getName());
    }

    public void registerDriver(Driver driver) {
        drivers.put(driver.getUserId(), driver);
        System.out.println("[UserService] Driver registered: " + driver.getName() + 
                         " with " + driver.getVehicle().getModel());
    }

    public Rider getRider(String riderId) {
        return riders.get(riderId);
    }

    public Driver getDriver(String driverId) {
        return drivers.get(driverId);
    }
}

