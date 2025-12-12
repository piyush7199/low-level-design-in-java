package org.lld.practice.design_matching_engine.improved_solution;

import org.lld.practice.design_matching_engine.improved_solution.models.*;
import org.lld.practice.design_matching_engine.improved_solution.services.DriverService;
import org.lld.practice.design_matching_engine.improved_solution.strategies.MatchingStrategy;
import org.lld.practice.design_matching_engine.improved_solution.strategies.ScoringBasedStrategy;

import java.util.List;
import java.util.Optional;

/**
 * Main Matching Engine for ride/delivery matching.
 * 
 * Uses Strategy pattern for pluggable matching algorithms.
 */
public class MatchingEngine {
    
    private final DriverService driverService;
    private MatchingStrategy strategy;

    public MatchingEngine() {
        this.driverService = new DriverService();
        this.strategy = new ScoringBasedStrategy();  // Default strategy
    }

    public MatchingEngine(MatchingStrategy strategy) {
        this.driverService = new DriverService();
        this.strategy = strategy;
    }

    /**
     * Set the matching strategy.
     */
    public void setStrategy(MatchingStrategy strategy) {
        this.strategy = strategy;
        System.out.printf("📊 Matching strategy set to: %s%n", strategy.getName());
    }

    /**
     * Get current strategy name.
     */
    public String getStrategyName() {
        return strategy.getName();
    }

    // ========== Driver Management ==========

    public void registerDriver(Driver driver) {
        driverService.registerDriver(driver);
    }

    public void setDriverOnline(String driverId) {
        driverService.setDriverOnline(driverId);
    }

    public void setDriverOffline(String driverId) {
        driverService.setDriverOffline(driverId);
    }

    public void updateDriverLocation(String driverId, Location location) {
        driverService.updateDriverLocation(driverId, location);
    }

    public DriverService getDriverService() {
        return driverService;
    }

    // ========== Matching ==========

    /**
     * Find a match for a ride request.
     */
    public Optional<Match> findMatch(RideRequest request) {
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        
        if (availableDrivers.isEmpty()) {
            System.out.println("❌ No drivers available");
            return Optional.empty();
        }
        
        System.out.printf("%n🔍 Finding match for request %s...%n", request.getRequestId());
        System.out.printf("   Pickup: %s, Vehicle: %s%n", 
                request.getPickupLocation(), request.getPreferredVehicleType());
        System.out.printf("   Available drivers: %d%n", availableDrivers.size());
        
        Optional<ScoredDriver> bestMatch = strategy.findBestMatch(request, availableDrivers);
        
        if (bestMatch.isEmpty()) {
            System.out.println("❌ No suitable driver found");
            return Optional.empty();
        }
        
        ScoredDriver scored = bestMatch.get();
        Driver driver = scored.getDriver();
        
        // Create match and update driver status
        Match match = new Match(request, driver, scored.getScore());
        driver.setPendingMatch();
        
        System.out.printf("✅ Match found!%n");
        System.out.printf("   Driver: %s (Rating: %.1f, Score: %.2f)%n", 
                driver.getName(), driver.getRating(), scored.getScore());
        System.out.printf("   Distance: %.2f km, ETA: %d min%n", 
                scored.getDistance(), scored.getEtaMinutes());
        
        return Optional.of(match);
    }

    /**
     * Get ranked list of drivers for a request.
     */
    public List<ScoredDriver> getRankedDrivers(RideRequest request) {
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        return strategy.rankDrivers(request, availableDrivers);
    }

    /**
     * Handle driver accepting a match.
     */
    public void acceptMatch(Match match) {
        match.accept();
        System.out.printf("✅ Driver %s accepted match %s%n", 
                match.getDriver().getName(), match.getMatchId());
    }

    /**
     * Handle driver rejecting a match.
     */
    public void rejectMatch(Match match) {
        match.reject();
        System.out.printf("❌ Driver %s rejected match %s%n", 
                match.getDriver().getName(), match.getMatchId());
    }

    /**
     * Complete a ride.
     */
    public void completeRide(Match match) {
        match.complete();
        System.out.printf("🏁 Ride %s completed%n", match.getMatchId());
    }

    // ========== Statistics ==========

    public void printStatus() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        🚗 MATCHING ENGINE STATUS        ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ Strategy: %-28s ║%n", strategy.getName());
        System.out.printf("║ Total Drivers: %-24d ║%n", driverService.getAllDrivers().size());
        System.out.printf("║ Available Drivers: %-20d ║%n", driverService.getAvailableDriverCount());
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}

