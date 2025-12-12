package org.lld.practice.design_notification_system.naive_solution;

/**
 * Naive implementation of a notification system.
 * 
 * This demonstrates common pitfalls:
 * - Hardcoded channel logic with if-else
 * - No support for priorities
 * - No retry mechanism
 * - No template support
 * - Violates Open/Closed Principle
 */
public class SimpleNotificationService {
    
    public void sendNotification(String userId, String message, String channel) {
        if (channel.equals("EMAIL")) {
            System.out.println("Sending email to " + userId + ": " + message);
            // Simulate email sending
        } else if (channel.equals("SMS")) {
            System.out.println("Sending SMS to " + userId + ": " + message);
            // Simulate SMS sending
        } else if (channel.equals("PUSH")) {
            System.out.println("Sending push notification to " + userId + ": " + message);
            // Simulate push notification
        } else {
            System.out.println("Unknown channel: " + channel);
        }
    }
    
    public void sendBulkNotification(String[] userIds, String message, String channel) {
        for (String userId : userIds) {
            sendNotification(userId, message, channel);
        }
    }
}

