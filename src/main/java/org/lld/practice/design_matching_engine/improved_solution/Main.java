package org.lld.practice.design_matching_engine.improved_solution;

import org.lld.practice.design_matching_engine.improved_solution.models.*;
import org.lld.practice.design_matching_engine.improved_solution.strategies.*;

import java.util.List;
import java.util.Optional;

/**
 * Demo application for the Matching Engine.
 * 
 * Demonstrates:
 * - Multiple matching strategies
 * - Driver management
 * - Match lifecycle (pending -> accepted -> completed)
 * - Scoring-based matching with multiple factors
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚗 RIDE/DELIVERY MATCHING ENGINE - DEMO                    ║");
        System.out.println("║     Design Patterns: Strategy, Observer                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // Create engine with default scoring strategy
        MatchingEngine engine = new MatchingEngine();
        
        // Setup drivers
        setupDrivers(engine);
        
        // Demo 1: Nearest Driver Strategy
        demoNearestDriver(engine);
        
        // Demo 2: Scoring-Based Strategy
        demoScoringBased(engine);
        
        // Demo 3: Load-Balanced Strategy
        demoLoadBalanced(engine);
        
        // Demo 4: Full Match Lifecycle
        demoMatchLifecycle(engine);
        
        // Summary
        printSummary();
    }
    
    private static void setupDrivers(MatchingEngine engine) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("SETUP: Registering Drivers");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // San Francisco downtown area
        Driver d1 = new Driver("D1", "Alice", VehicleType.CAR, 
                new Location(37.7749, -122.4194));
        d1.setRating(4.9);
        
        Driver d2 = new Driver("D2", "Bob", VehicleType.CAR, 
                new Location(37.7751, -122.4180));  // Closest to pickup
        d2.setRating(4.5);
        
        Driver d3 = new Driver("D3", "Charlie", VehicleType.SUV, 
                new Location(37.7760, -122.4200));
        d3.setRating(4.8);
        
        Driver d4 = new Driver("D4", "Diana", VehicleType.CAR, 
                new Location(37.7740, -122.4170));
        d4.setRating(4.2);
        
        Driver d5 = new Driver("D5", "Eve", VehicleType.PREMIUM, 
                new Location(37.7755, -122.4195));
        d5.setRating(5.0);
        
        // Register all drivers
        engine.registerDriver(d1);
        engine.registerDriver(d2);
        engine.registerDriver(d3);
        engine.registerDriver(d4);
        engine.registerDriver(d5);
        
        // Set drivers online
        engine.setDriverOnline("D1");
        engine.setDriverOnline("D2");
        engine.setDriverOnline("D3");
        engine.setDriverOnline("D4");
        engine.setDriverOnline("D5");
        
        engine.printStatus();
    }
    
    private static void demoNearestDriver(MatchingEngine engine) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 1: Nearest Driver Strategy");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        engine.setStrategy(new NearestDriverStrategy());
        
        Rider rider = new Rider("R1", "John", "555-0100");
        Location pickup = new Location(37.7750, -122.4185);
        Location dropoff = new Location(37.7850, -122.4094);
        
        RideRequest request = new RideRequest(rider, pickup, dropoff, VehicleType.CAR, 1);
        
        System.out.println("\n📍 Request: CAR from (37.7750, -122.4185)");
        
        // Show ranked drivers
        List<ScoredDriver> ranked = engine.getRankedDrivers(request);
        System.out.println("\n📊 Driver Rankings (by distance):");
        for (int i = 0; i < ranked.size(); i++) {
            ScoredDriver sd = ranked.get(i);
            System.out.printf("   %d. %s - %.2f km away, ETA %d min%n",
                    i + 1, sd.getDriver().getName(), sd.getDistance(), sd.getEtaMinutes());
        }
        
        Optional<Match> match = engine.findMatch(request);
        match.ifPresent(m -> {
            m.reject();  // Simulate rejection to free driver
        });
        
        System.out.println();
    }
    
    private static void demoScoringBased(MatchingEngine engine) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 2: Scoring-Based Strategy");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        engine.setStrategy(new ScoringBasedStrategy());
        
        Rider rider = new Rider("R2", "Jane", "555-0200");
        Location pickup = new Location(37.7750, -122.4185);
        Location dropoff = new Location(37.7850, -122.4094);
        
        RideRequest request = new RideRequest(rider, pickup, dropoff, VehicleType.CAR, 1);
        
        System.out.println("\n📍 Request: CAR from (37.7750, -122.4185)");
        System.out.println("   Scoring considers: Distance (40%), Rating (25%), Acceptance (15%), etc.");
        
        // Show ranked drivers with scores
        List<ScoredDriver> ranked = engine.getRankedDrivers(request);
        System.out.println("\n📊 Driver Rankings (by score):");
        for (int i = 0; i < ranked.size(); i++) {
            ScoredDriver sd = ranked.get(i);
            System.out.printf("   %d. %s - Score: %.2f (%.2f km, Rating: %.1f)%n",
                    i + 1, sd.getDriver().getName(), sd.getScore(),
                    sd.getDistance(), sd.getDriver().getRating());
        }
        
        Optional<Match> match = engine.findMatch(request);
        match.ifPresent(m -> {
            m.reject();  // Free driver for next demo
        });
        
        System.out.println();
    }
    
    private static void demoLoadBalanced(MatchingEngine engine) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 3: Load-Balanced Strategy");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        engine.setStrategy(new LoadBalancedStrategy());
        
        // Simulate some drivers having completed more rides
        Driver d1 = engine.getDriverService().getDriver("D1").orElseThrow();
        for (int i = 0; i < 5; i++) d1.completeRide();
        d1.goOnline();
        
        Driver d2 = engine.getDriverService().getDriver("D2").orElseThrow();
        for (int i = 0; i < 10; i++) d2.completeRide();
        d2.goOnline();
        
        System.out.println("\n📊 Simulated ride counts:");
        System.out.println("   Alice (D1): 5 rides completed");
        System.out.println("   Bob (D2): 10 rides completed");
        System.out.println("   Others: 0 rides");
        
        Rider rider = new Rider("R3", "Mike", "555-0300");
        Location pickup = new Location(37.7750, -122.4185);
        Location dropoff = new Location(37.7850, -122.4094);
        
        RideRequest request = new RideRequest(rider, pickup, dropoff, VehicleType.CAR, 1);
        
        System.out.println("\n   Load balancing prefers drivers with fewer rides...");
        
        List<ScoredDriver> ranked = engine.getRankedDrivers(request);
        System.out.println("\n📊 Driver Rankings (load-balanced):");
        for (int i = 0; i < ranked.size(); i++) {
            ScoredDriver sd = ranked.get(i);
            System.out.printf("   %d. %s - Score: %.2f (Rides: %d, Distance: %.2f km)%n",
                    i + 1, sd.getDriver().getName(), sd.getScore(),
                    sd.getDriver().getTotalRides(), sd.getDistance());
        }
        
        Optional<Match> match = engine.findMatch(request);
        match.ifPresent(m -> m.reject());
        
        System.out.println();
    }
    
    private static void demoMatchLifecycle(MatchingEngine engine) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 4: Complete Match Lifecycle");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        engine.setStrategy(new ScoringBasedStrategy());
        
        Rider rider = new Rider("R4", "Sarah", "555-0400");
        Location pickup = new Location(37.7750, -122.4185);
        Location dropoff = new Location(37.7850, -122.4094);
        
        RideRequest request = new RideRequest(rider, pickup, dropoff, VehicleType.CAR, 2);
        
        System.out.println("\n📍 Sarah requests a CAR for 2 passengers");
        
        // Find match
        Optional<Match> matchOpt = engine.findMatch(request);
        
        if (matchOpt.isPresent()) {
            Match match = matchOpt.get();
            
            System.out.printf("%n🎫 Match ID: %s%n", match.getMatchId());
            System.out.printf("   Status: %s%n", match.getStatus());
            
            // Driver accepts
            System.out.println("\n👍 Driver accepts the ride...");
            engine.acceptMatch(match);
            System.out.printf("   Status: %s%n", match.getStatus());
            
            // Ride in progress
            System.out.println("\n🚗 Ride in progress...");
            System.out.printf("   Driver %s picking up %s%n", 
                    match.getDriver().getName(), rider.getName());
            
            // Complete ride
            System.out.println("\n🏁 Completing ride...");
            engine.completeRide(match);
            System.out.printf("   Status: %s%n", match.getStatus());
            System.out.printf("   Driver total rides: %d%n", match.getDriver().getTotalRides());
        }
        
        engine.printStatus();
    }
    
    private static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY CONCEPTS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Strategy Pattern: Multiple matching algorithms");
        System.out.println("   - Nearest Driver: Simple distance-based");
        System.out.println("   - Scoring-Based: Multi-factor weighted scoring");
        System.out.println("   - Load-Balanced: Fair ride distribution");
        System.out.println();
        System.out.println("✅ Match Lifecycle: PENDING → ACCEPTED → COMPLETED");
        System.out.println("✅ Driver Status: OFFLINE → AVAILABLE → BUSY → AVAILABLE");
        System.out.println("✅ Location Services: Haversine distance, ETA estimation");
        System.out.println();
        System.out.println("🎯 Interview Discussion Points:");
        System.out.println("   - Spatial indexing (Quadtree, Geohash) for O(log n) search");
        System.out.println("   - Distributed matching with Redis geospatial");
        System.out.println("   - Surge pricing integration");
        System.out.println("   - Driver acceptance timeout handling");
    }
}

