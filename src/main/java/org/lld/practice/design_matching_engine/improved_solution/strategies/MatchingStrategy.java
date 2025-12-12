package org.lld.practice.design_matching_engine.improved_solution.strategies;

import org.lld.practice.design_matching_engine.improved_solution.models.Driver;
import org.lld.practice.design_matching_engine.improved_solution.models.RideRequest;
import org.lld.practice.design_matching_engine.improved_solution.models.ScoredDriver;

import java.util.List;
import java.util.Optional;

/**
 * Strategy interface for driver matching algorithms.
 */
public interface MatchingStrategy {
    
    /**
     * Find the best matching driver for a ride request.
     * 
     * @param request The ride request
     * @param availableDrivers List of available drivers
     * @return Best matching driver, or empty if no suitable match
     */
    Optional<ScoredDriver> findBestMatch(RideRequest request, List<Driver> availableDrivers);
    
    /**
     * Rank all available drivers for a ride request.
     * 
     * @param request The ride request
     * @param availableDrivers List of available drivers
     * @return List of drivers sorted by match score (best first)
     */
    List<ScoredDriver> rankDrivers(RideRequest request, List<Driver> availableDrivers);
    
    /**
     * Get the name of this strategy.
     */
    String getName();
}

