package org.lld.practice.design_ride_sharing_system.improved_solution.services;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.*;

import java.util.*;
import java.util.stream.Collectors;

public class RideService {
    private final Map<String, Ride> rides;
    private final MatchingService matchingService;
    private final PaymentService paymentService;
    private int rideCounter = 1;

    public RideService(MatchingService matchingService, PaymentService paymentService) {
        this.rides = new HashMap<>();
        this.matchingService = matchingService;
        this.paymentService = paymentService;
    }

    public Ride requestRide(Rider rider, Location pickup, Location dropoff, RideType rideType) {
        Driver driver = matchingService.findDriver(pickup, rideType);
        
        if (driver == null) {
            System.out.println("[RideService] No available drivers found");
            return null;
        }

        String rideId = "RIDE" + String.format("%05d", rideCounter++);
        Ride ride = new Ride(rideId, rider, driver, pickup, dropoff, rideType);
        
        rides.put(rideId, ride);
        driver.setAvailable(false);
        rider.addRideToHistory(rideId);
        
        System.out.println("[RideService] Ride requested: " + rideId);
        System.out.println("Rider: " + rider.getName());
        System.out.println("Driver: " + driver.getName() + " (" + driver.getVehicle() + ")");
        System.out.println("Distance: " + String.format("%.2f", ride.getDistance()) + " km");
        
        // Auto-accept the ride
        ride.acceptRide();
        
        return ride;
    }

    public void startRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            System.out.println("[RideService] Ride not found: " + rideId);
            return;
        }
        ride.startRide();
    }

    public void completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            System.out.println("[RideService] Ride not found: " + rideId);
            return;
        }

        ride.completeRide();
        
        // Calculate and process fare
        double fare = paymentService.calculateFare(ride);
        ride.setFare(fare);
        
        boolean paymentSuccess = paymentService.processPayment(ride);
        if (paymentSuccess) {
            System.out.println("[RideService] Payment processed: $" + String.format("%.2f", fare));
        }

        // Make driver available again
        ride.getDriver().setAvailable(true);
        ride.getDriver().incrementRides();
        
        System.out.println("[RideService] Ride completed successfully");
    }

    public void cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            System.out.println("[RideService] Ride not found: " + rideId);
            return;
        }

        ride.cancelRide();
        ride.getDriver().setAvailable(true);
        System.out.println("[RideService] Ride cancelled");
    }

    public Ride getRide(String rideId) {
        return rides.get(rideId);
    }

    public List<Ride> getRiderHistory(String riderId) {
        return rides.values().stream()
                .filter(ride -> ride.getRider().getUserId().equals(riderId))
                .collect(Collectors.toList());
    }

    public List<Ride> getAllRides() {
        return new ArrayList<>(rides.values());
    }
}

