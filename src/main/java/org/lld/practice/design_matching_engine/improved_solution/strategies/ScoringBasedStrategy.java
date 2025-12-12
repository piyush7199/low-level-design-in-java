package org.lld.practice.design_matching_engine.improved_solution.strategies;

import org.lld.practice.design_matching_engine.improved_solution.models.*;

import java.util.List;
import java.util.Optional;

/**
 * Scoring-based matching strategy.
 * Uses weighted multi-factor scoring to find the best match.
 * 
 * Factors considered:
 * - Distance to pickup (40%)
 * - Driver rating (25%)
 * - Acceptance rate (15%)
 * - Idle time / fairness (10%)
 * - Vehicle match bonus (10%)
 */
public class ScoringBasedStrategy implements MatchingStrategy {
    
    private static final double MAX_DISTANCE_KM = 10.0;
    private static final long MAX_IDLE_TIME_SECONDS = 1800; // 30 minutes
    
    // Scoring weights
    private static final double WEIGHT_DISTANCE = 0.40;
    private static final double WEIGHT_RATING = 0.25;
    private static final double WEIGHT_ACCEPTANCE = 0.15;
    private static final double WEIGHT_IDLE_TIME = 0.10;
    private static final double WEIGHT_VEHICLE_MATCH = 0.10;

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
                .filter(driver -> canServeRequest(driver, request))
                .map(driver -> scoreDriver(driver, request, pickup, requestedType))
                .filter(sd -> sd.getDistance() <= MAX_DISTANCE_KM)
                .sorted()  // Uses ScoredDriver's compareTo (higher score first)
                .toList();
    }

    private boolean canServeRequest(Driver driver, RideRequest request) {
        // Check vehicle capacity
        return driver.getVehicleType().getCapacity() >= request.getPassengerCount();
    }

    private ScoredDriver scoreDriver(Driver driver, RideRequest request, 
                                     Location pickup, VehicleType requestedType) {
        double distance = driver.getCurrentLocation().distanceTo(pickup);
        int eta = driver.getCurrentLocation().estimateETAMinutes(pickup);
        
        // Calculate individual factor scores (normalized to 0-1)
        double distanceScore = normalizeDistance(distance);
        double ratingScore = driver.getRating() / 5.0;
        double acceptanceScore = driver.getAcceptanceRate() / 100.0;
        double idleTimeScore = normalizeIdleTime(driver.getIdleTimeSeconds());
        double vehicleMatchScore = (driver.getVehicleType() == requestedType) ? 1.0 : 0.5;
        
        // Calculate weighted total score
        double totalScore = 
                WEIGHT_DISTANCE * distanceScore +
                WEIGHT_RATING * ratingScore +
                WEIGHT_ACCEPTANCE * acceptanceScore +
                WEIGHT_IDLE_TIME * idleTimeScore +
                WEIGHT_VEHICLE_MATCH * vehicleMatchScore;
        
        return new ScoredDriver(driver, totalScore, distance, eta);
    }

    private double normalizeDistance(double distance) {
        // Closer = higher score
        if (distance >= MAX_DISTANCE_KM) return 0.0;
        return 1.0 - (distance / MAX_DISTANCE_KM);
    }

    private double normalizeIdleTime(long idleSeconds) {
        // Longer wait = higher score (fairness)
        return Math.min(1.0, (double) idleSeconds / MAX_IDLE_TIME_SECONDS);
    }

    @Override
    public String getName() {
        return "Scoring-Based";
    }
}

