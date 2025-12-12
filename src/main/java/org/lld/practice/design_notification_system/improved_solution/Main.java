package org.lld.practice.design_notification_system.improved_solution;

import org.lld.practice.design_notification_system.improved_solution.models.NotificationChannel;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationPriority;
import org.lld.practice.design_notification_system.improved_solution.services.NotificationService;

import java.time.LocalDateTime;

/**
 * Demo of improved notification system with multiple channels and priorities.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Improved Notification System Demo ===\n");
        
        NotificationService service = new NotificationService();
        
        System.out.println("1. Sending notifications with different priorities:");
        service.sendNotification("user123", "Welcome to our platform!", 
                               NotificationChannel.EMAIL, NotificationPriority.NORMAL);
        service.sendNotification("user456", "Your payment is due!", 
                               NotificationChannel.SMS, NotificationPriority.HIGH);
        service.sendNotification("user789", "System maintenance in 1 hour", 
                               NotificationChannel.PUSH, NotificationPriority.URGENT);
        
        Thread.sleep(2000); // Wait for processing
        
        System.out.println("\n2. Scheduling a notification:");
        LocalDateTime futureTime = LocalDateTime.now().plusSeconds(2);
        service.scheduleNotification("user999", "Scheduled notification", 
                                   NotificationChannel.EMAIL, NotificationPriority.NORMAL, futureTime);
        
        Thread.sleep(3000); // Wait for scheduled notification
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Multiple notification channels (Email, SMS, Push)");
        System.out.println("✓ Priority-based queue processing");
        System.out.println("✓ Retry logic with exponential backoff");
        System.out.println("✓ Scheduled notifications");
        System.out.println("✓ Easy to add new channels (Strategy pattern)");
        System.out.println("✓ Factory pattern for channel handler creation");
    }
}

