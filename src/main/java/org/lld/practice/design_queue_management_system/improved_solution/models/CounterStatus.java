package org.lld.practice.design_queue_management_system.improved_solution.models;

/**
 * Enum representing the status of a service counter.
 */
public enum CounterStatus {
    AVAILABLE,    // Counter is free and ready to serve
    BUSY,         // Counter is currently serving a customer
    CLOSED        // Counter is not operational
}

