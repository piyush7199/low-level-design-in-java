package org.lld.practice.design_matching_engine.improved_solution.models;

/**
 * Enum representing driver availability status.
 */
public enum DriverStatus {
    AVAILABLE,      // Ready to accept rides
    BUSY,           // Currently on a ride
    OFFLINE,        // Not accepting rides
    PENDING_MATCH   // Matched but waiting for confirmation
}

