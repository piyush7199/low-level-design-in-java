package org.lld.practice.design_ride_sharing_system.naive_solution;

public class Main {
    public static void main(String[] args) {
        RideApp app = new RideApp();
        
        // Register users
        app.registerRider(new Rider("R1", "Alice"));
        app.registerDriver(new Driver("D1", "Bob", new Location(37.7749, -122.4194)));
        app.registerDriver(new Driver("D2", "Charlie", new Location(37.7849, -122.4294)));
        
        // Request ride
        Ride ride = app.requestRide("R1", 
            new Location(37.7750, -122.4195), 
            new Location(37.7950, -122.4395),
            "ECONOMY");
        
        // Complete ride
        if (ride != null) {
            app.completeRide(ride.getId());
        }
    }
}

