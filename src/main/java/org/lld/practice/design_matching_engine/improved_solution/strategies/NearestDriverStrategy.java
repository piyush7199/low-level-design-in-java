package org.lld.practice.design_matching_engine.improved_solution.strategies;

import org.lld.practice.design_matching_engine.improved_solution.models.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Nearest driver matching strategy.
 * Simply matches with the closest available driver.
 * 
 * Pros: Simple, fast, minimal ETA
 * Cons: Unfair to drivers who have been waiting, ignores quality metrics
 */
public class NearestDriverStrategy implements MatchingStrategy {
    
    private static final double MAX_DISTANCE_KM = 10.0;

    @Override
    public Optional<ScoredDriver> findBestMatch(RideRequest request, List<Driver> availableDrivers) {
        List<ScoredDriver> ranked = rankDrivers(request, availableDrivers);
        return ranked.isEmpty() ? Optional.empty() : Optional.of(ranked.get(0));
    }

    @Override
    public List<ScoredDriver> rankDrivers(RideRequest request, List<Driver> availableDrivers) {
        Location pickup = request.getPickupLocation();
        VehicleType requestedType = request.getPreferredVehicleType();
        
        return availableDrivers.stream()
                .filter(driver -> driver.getVehicleType() == requestedType)
                .filter(driver -> driver.getVehicleType().getCapacity() >= request.getPassengerCount())
                .map(driver -> {
                    double distance = driver.getCurrentLocation().distanceTo(pickup);
                    int eta = driver.getCurrentLocation().estimateETAMinutes(pickup);
                    // Score is inverse of distance (closer = higher score)
                    double score = distance > 0 ? 1.0 / distance : 1.0;
                    return new ScoredDriver(driver, score, distance, eta);
                })
                .filter(sd -> sd.getDistance() <= MAX_DISTANCE_KM)
                .sorted(Comparator.comparingDouble(ScoredDriver::getDistance))
                .toList();
    }

    @Override
    public String getName() {
        return "Nearest Driver";
    }
}

