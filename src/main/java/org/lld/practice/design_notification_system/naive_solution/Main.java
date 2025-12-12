package org.lld.practice.design_notification_system.naive_solution;

/**
 * Demo of naive notification system implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Notification System Demo ===\n");
        
        SimpleNotificationService service = new SimpleNotificationService();
        
        service.sendNotification("user123", "Welcome to our platform!", "EMAIL");
        service.sendNotification("user456", "Your order has been shipped", "SMS");
        service.sendNotification("user789", "You have a new message", "PUSH");
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- Cannot add new channels without modifying code");
        System.out.println("- No priority handling");
        System.out.println("- No retry mechanism for failed notifications");
        System.out.println("- No template support");
        System.out.println("- No scheduling capability");
    }
}

