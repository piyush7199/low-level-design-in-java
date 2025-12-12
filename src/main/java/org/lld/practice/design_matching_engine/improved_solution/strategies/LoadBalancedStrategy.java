package org.lld.practice.design_matching_engine.improved_solution.strategies;

import org.lld.practice.design_matching_engine.improved_solution.models.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Load-balanced matching strategy.
 * Prioritizes drivers who have completed fewer rides to ensure fair distribution.
 * 
 * Combines distance with ride count to balance load across drivers.
 */
public class LoadBalancedStrategy implements MatchingStrategy {
    
    private static final double MAX_DISTANCE_KM = 10.0;
    private static final double WEIGHT_DISTANCE = 0.5;
    private static final double WEIGHT_RIDE_COUNT = 0.5;

    @Override
    public Optional<ScoredDriver> findBestMatch(RideRequest request, List<Driver> availableDrivers) {
        List<ScoredDriver> ranked = rankDrivers(request, availableDrivers);
        return ranked.isEmpty() ? Optional.empty() : Optional.of(ranked.get(0));
    }

    @Override
    public List<ScoredDriver> rankDrivers(RideRequest request, List<Driver> availableDrivers) {
        Location pickup = request.getPickupLocation();
        VehicleType requestedType = request.getPreferredVehicleType();
        
        // Find max rides for normalization
        int maxRides = availableDrivers.stream()
                .mapToInt(Driver::getTotalRides)
                .max()
                .orElse(1);
        
        return availableDrivers.stream()
                .filter(driver -> driver.getVehicleType() == requestedType || 
                                  canUpgrade(driver.getVehicleType(), requestedType))
                .filter(driver -> driver.getVehicleType().getCapacity() >= request.getPassengerCount())
                .map(driver -> {
                    double distance = driver.getCurrentLocation().distanceTo(pickup);
                    int eta = driver.getCurrentLocation().estimateETAMinutes(pickup);
                    
                    // Distance score (closer = higher)
                    double distanceScore = distance > 0 ? 
                            1.0 - Math.min(1.0, distance / MAX_DISTANCE_KM) : 1.0;
                    
                    // Ride count score (fewer rides = higher, for fairness)
                    double rideCountScore = maxRides > 0 ? 
                            1.0 - ((double) driver.getTotalRides() / maxRides) : 1.0;
                    
                    double score = WEIGHT_DISTANCE * distanceScore + 
                                   WEIGHT_RIDE_COUNT * rideCountScore;
                    
                    return new ScoredDriver(driver, score, distance, eta);
                })
                .filter(sd -> sd.getDistance() <= MAX_DISTANCE_KM)
                .sorted()
                .toList();
    }

    /**
     * Check if driver's vehicle can upgrade to requested type.
     * E.g., SUV can serve CAR requests.
     */
    private boolean canUpgrade(VehicleType driverVehicle, VehicleType requestedType) {
        return driverVehicle.getCapacity() >= requestedType.getCapacity() &&
               driverVehicle.getPriceMultiplier() >= requestedType.getPriceMultiplier();
    }

    @Override
    public String getName() {
        return "Load-Balanced";
    }
}

