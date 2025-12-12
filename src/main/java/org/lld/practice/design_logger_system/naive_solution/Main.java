package org.lld.practice.design_logger_system.naive_solution;

/**
 * Demo of naive logger implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Logger System Demo ===\n");
        
        SimpleLogger.debug("This is a debug message");
        SimpleLogger.info("Application started");
        SimpleLogger.warn("Low memory warning");
        SimpleLogger.error("Failed to connect to database");
        SimpleLogger.fatal("System crash detected");
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- Cannot change output destination");
        System.out.println("- No filtering by log level");
        System.out.println("- Fixed format, cannot customize");
        System.out.println("- Not thread-safe");
        System.out.println("- Cannot log to file or database");
    }
}

