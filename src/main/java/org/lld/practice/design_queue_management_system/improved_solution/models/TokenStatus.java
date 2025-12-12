package org.lld.practice.design_queue_management_system.improved_solution.models;

/**
 * Enum representing the lifecycle status of a token.
 */
public enum TokenStatus {
    WAITING,      // Token is in queue waiting to be called
    SERVING,      // Token is currently being served at a counter
    COMPLETED,    // Service completed successfully
    CANCELLED,    // Customer cancelled the token
    NO_SHOW       // Customer didn't appear when called
}

