package org.lld.practice.design_logger_system.naive_solution;

/**
 * Naive implementation of a logger system.
 * 
 * This demonstrates common pitfalls:
 * - Hardcoded output destination
 * - No flexibility for different log levels
 * - No support for multiple appenders
 * - No formatting options
 * - Not thread-safe
 */
public class SimpleLogger {
    
    public static void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }
    
    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }
    
    public static void warn(String message) {
        System.out.println("[WARN] " + message);
    }
    
    public static void error(String message) {
        System.err.println("[ERROR] " + message);
    }
    
    public static void fatal(String message) {
        System.err.println("[FATAL] " + message);
    }
}

