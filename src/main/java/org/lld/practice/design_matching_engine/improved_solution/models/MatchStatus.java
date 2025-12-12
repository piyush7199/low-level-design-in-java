package org.lld.practice.design_matching_engine.improved_solution.models;

/**
 * Enum representing the status of a match.
 */
public enum MatchStatus {
    PENDING,        // Match proposed, waiting for driver response
    ACCEPTED,       // Driver accepted the match
    REJECTED,       // Driver rejected the match
    TIMEOUT,        // Driver didn't respond in time
    CANCELLED,      // Rider cancelled the request
    COMPLETED       // Ride completed successfully
}

