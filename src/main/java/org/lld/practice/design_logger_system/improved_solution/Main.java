package org.lld.practice.design_logger_system.improved_solution;

import org.lld.practice.design_logger_system.improved_solution.appenders.ConsoleAppender;
import org.lld.practice.design_logger_system.improved_solution.appenders.FileAppender;
import org.lld.practice.design_logger_system.improved_solution.formatters.JSONFormatter;
import org.lld.practice.design_logger_system.improved_solution.formatters.PlainTextFormatter;
import org.lld.practice.design_logger_system.improved_solution.models.LogLevel;

/**
 * Demo of improved logger system with multiple appenders and formatters.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Improved Logger System Demo ===\n");
        
        // Create log manager with global minimum level
        LogManager logManager = new LogManager(LogLevel.DEBUG);
        
        // Add console appender with plain text formatter
        ConsoleAppender consoleAppender = new ConsoleAppender(LogLevel.INFO, new PlainTextFormatter());
        logManager.addAppender(consoleAppender);
        
        // Add file appender with JSON formatter
        FileAppender fileAppender = new FileAppender("app.log", LogLevel.DEBUG, new JSONFormatter());
        logManager.addAppender(fileAppender);
        
        // Create logger for different sources
        Logger userServiceLogger = Logger.getLogger("UserService", logManager);
        Logger paymentServiceLogger = Logger.getLogger("PaymentService", logManager);
        
        System.out.println("1. Logging with different levels:");
        userServiceLogger.debug("User authentication started");
        userServiceLogger.info("User logged in successfully");
        userServiceLogger.warn("Failed login attempt detected");
        userServiceLogger.error("Database connection failed");
        
        System.out.println("\n2. Logging from different sources:");
        paymentServiceLogger.info("Payment processing started");
        paymentServiceLogger.error("Payment gateway timeout", new RuntimeException("Connection timeout"));
        
        System.out.println("\n3. Changing global log level to WARN:");
        logManager.setGlobalMinimumLevel(LogLevel.WARN);
        userServiceLogger.debug("This debug message won't be logged");
        userServiceLogger.info("This info message won't be logged");
        userServiceLogger.warn("This warning will be logged");
        userServiceLogger.error("This error will be logged");
        
        // Cleanup
        logManager.shutdown();
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Multiple output destinations (Console, File)");
        System.out.println("✓ Different formatters (Plain Text, JSON)");
        System.out.println("✓ Configurable log levels");
        System.out.println("✓ Thread-safe operations");
        System.out.println("✓ Easy to extend with new appenders/formatters");
    }
}

