package org.lld.practice.design_ride_sharing_system.improved_solution;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.*;

public class Main {
    public static void main(String[] args) {
        RideSharingSystem system = new RideSharingSystem();

        // Setup: Register users
        System.out.println("=== Setting Up Ride-Sharing System ===\n");
        
        Rider rider1 = new Rider("R001", "Alice Smith", "1234567890");
        Rider rider2 = new Rider("R002", "Bob Johnson", "9876543210");
        system.registerRider(rider1);
        system.registerRider(rider2);

        Vehicle vehicle1 = new Vehicle("ABC123", "Toyota Camry", "White", VehicleType.SEDAN);
        Vehicle vehicle2 = new Vehicle("XYZ789", "Tesla Model S", "Black", VehicleType.LUXURY);
        Vehicle vehicle3 = new Vehicle("DEF456", "Honda CR-V", "Silver", VehicleType.SUV);

        Driver driver1 = new Driver("D001", "John Driver", "5555555555", vehicle1, 
                                   new Location(37.7749, -122.4194));
        Driver driver2 = new Driver("D002", "Sarah Pro", "4444444444", vehicle2, 
                                   new Location(37.7849, -122.4094));
        Driver driver3 = new Driver("D003", "Mike Wheeler", "3333333333", vehicle3, 
                                   new Location(37.7649, -122.4294));
        
        system.registerDriver(driver1);
        system.registerDriver(driver2);
        system.registerDriver(driver3);

        // Scenario 1: Request and complete an economy ride
        System.out.println("\n=== Scenario 1: Economy Ride ===\n");
        Ride ride1 = system.requestRide("R001", 
            new Location(37.7750, -122.4195),
            new Location(37.7950, -122.4395),
            RideType.ECONOMY);

        if (ride1 != null) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.startRide(ride1.getRideId());
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.completeRide(ride1.getRideId());
            
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            system.rateRide(ride1.getRideId(), 4.5, 5.0);
        }

        // Scenario 2: Request a premium ride
        System.out.println("\n=== Scenario 2: Premium Ride ===\n");
        Ride ride2 = system.requestRide("R002",
            new Location(37.7850, -122.4100),
            new Location(37.8050, -122.4300),
            RideType.PREMIUM);

        if (ride2 != null) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.startRide(ride2.getRideId());
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.completeRide(ride2.getRideId());
            
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            system.rateRide(ride2.getRideId(), 5.0, 4.8);
        }

        // Scenario 3: Cancel a ride
        System.out.println("\n=== Scenario 3: Ride Cancellation ===\n");
        Ride ride3 = system.requestRide("R001",
            new Location(37.7650, -122.4300),
            new Location(37.7850, -122.4500),
            RideType.ECONOMY);

        if (ride3 != null) {
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            system.cancelRide(ride3.getRideId());
        }

        // Display ride history
        System.out.println("\n=== Ride History for Alice ===");
        System.out.println("Total rides: " + system.getRiderHistory("R001").size());

        System.out.println("\n=== System Complete ===");
    }
}

