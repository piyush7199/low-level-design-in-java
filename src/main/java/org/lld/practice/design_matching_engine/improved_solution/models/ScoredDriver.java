package org.lld.practice.design_matching_engine.improved_solution.models;

/**
 * Represents a driver with their computed match score.
 * Used for ranking drivers in matching algorithms.
 */
public class ScoredDriver implements Comparable<ScoredDriver> {
    
    private final Driver driver;
    private final double score;
    private final double distance;
    private final int etaMinutes;

    public ScoredDriver(Driver driver, double score, double distance, int etaMinutes) {
        this.driver = driver;
        this.score = score;
        this.distance = distance;
        this.etaMinutes = etaMinutes;
    }

    public Driver getDriver() {
        return driver;
    }

    public double getScore() {
        return score;
    }

    public double getDistance() {
        return distance;
    }

    public int getEtaMinutes() {
        return etaMinutes;
    }

    @Override
    public int compareTo(ScoredDriver other) {
        // Higher score is better
        return Double.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return String.format("ScoredDriver{driver='%s', score=%.2f, distance=%.2fkm, ETA=%dmin}",
                driver.getName(), score, distance, etaMinutes);
    }
}

