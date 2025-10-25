package org.lld.practice.design_ride_sharing_system.improved_solution;

import org.lld.practice.design_ride_sharing_system.improved_solution.models.*;
import org.lld.practice.design_ride_sharing_system.improved_solution.services.*;
import org.lld.practice.design_ride_sharing_system.improved_solution.strategies.*;

import java.util.List;

/**
 * Facade for the ride-sharing system
 */
public class RideSharingSystem {
    private final UserService userService;
    private final RideService rideService;
    private final MatchingService matchingService;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    public RideSharingSystem() {
        this.userService = new UserService();
        this.matchingService = new MatchingService(new NearestDriverStrategy());
        this.paymentService = new PaymentService(new DistanceBasedPricing());
        this.ratingService = new RatingService();
        this.rideService = new RideService(matchingService, paymentService);
    }

    public void registerRider(Rider rider) {
        userService.registerRider(rider);
    }

    public void registerDriver(Driver driver) {
        userService.registerDriver(driver);
        matchingService.addDriver(driver);
    }

    public Ride requestRide(String riderId, Location pickup, Location dropoff, RideType rideType) {
        Rider rider = userService.getRider(riderId);
        if (rider == null) {
            System.out.println("[System] Rider not found: " + riderId);
            return null;
        }
        return rideService.requestRide(rider, pickup, dropoff, rideType);
    }

    public void startRide(String rideId) {
        rideService.startRide(rideId);
    }

    public void completeRide(String rideId) {
        rideService.completeRide(rideId);
    }

    public void cancelRide(String rideId) {
        rideService.cancelRide(rideId);
    }

    public void rateRide(String rideId, double riderRating, double driverRating) {
        Ride ride = rideService.getRide(rideId);
        if (ride != null) {
            ratingService.rateDriver(ride.getDriver(), driverRating);
            ratingService.rateRider(ride.getRider(), riderRating);
        }
    }

    public Ride getRide(String rideId) {
        return rideService.getRide(rideId);
    }

    public List<Ride> getRiderHistory(String riderId) {
        return rideService.getRiderHistory(riderId);
    }

    public RideService getRideService() {
        return rideService;
    }
}

