package org.lld.practice.design_matching_engine.improved_solution.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a match between a driver and a ride request.
 */
public class Match {
    
    private final String matchId;
    private final RideRequest request;
    private final Driver driver;
    private final double score;
    private final double distanceToPickup;
    private final int etaMinutes;
    private final Instant matchedAt;
    
    private MatchStatus status;
    private Instant respondedAt;
    private Instant completedAt;

    public Match(RideRequest request, Driver driver, double score) {
        this.matchId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.request = Objects.requireNonNull(request);
        this.driver = Objects.requireNonNull(driver);
        this.score = score;
        this.distanceToPickup = driver.getCurrentLocation().distanceTo(request.getPickupLocation());
        this.etaMinutes = driver.getCurrentLocation().estimateETAMinutes(request.getPickupLocation());
        this.matchedAt = Instant.now();
        this.status = MatchStatus.PENDING;
    }

    // ========== Status Transitions ==========

    public void accept() {
        this.status = MatchStatus.ACCEPTED;
        this.respondedAt = Instant.now();
        driver.startRide();
        driver.recordAcceptance(true);
    }

    public void reject() {
        this.status = MatchStatus.REJECTED;
        this.respondedAt = Instant.now();
        driver.goOnline();  // Make driver available again
        driver.recordAcceptance(false);
    }

    public void timeout() {
        this.status = MatchStatus.TIMEOUT;
        this.respondedAt = Instant.now();
        driver.goOnline();
        driver.recordAcceptance(false);
    }

    public void cancel() {
        this.status = MatchStatus.CANCELLED;
        if (driver.getStatus() == DriverStatus.BUSY) {
            driver.goOnline();
        }
    }

    public void complete() {
        this.status = MatchStatus.COMPLETED;
        this.completedAt = Instant.now();
        driver.completeRide();
    }

    // ========== Getters ==========

    public String getMatchId() {
        return matchId;
    }

    public RideRequest getRequest() {
        return request;
    }

    public Driver getDriver() {
        return driver;
    }

    public double getScore() {
        return score;
    }

    public double getDistanceToPickup() {
        return distanceToPickup;
    }

    public int getEtaMinutes() {
        return etaMinutes;
    }

    public Instant getMatchedAt() {
        return matchedAt;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    @Override
    public String toString() {
        return String.format("Match{id='%s', driver='%s', rider='%s', score=%.2f, ETA=%dmin, status=%s}",
                matchId, driver.getName(), request.getRider().getName(), 
                score, etaMinutes, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(matchId, match.matchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId);
    }
}

