package org.lld.practice.design_notification_system.improved_solution.channels;

import org.lld.practice.design_notification_system.improved_solution.models.Notification;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationChannel;

/**
 * Push notification channel handler.
 */
public class PushChannelHandler implements NotificationChannelHandler {
    
    @Override
    public boolean send(Notification notification) {
        // Simulate push notification
        System.out.println("[PUSH] Sending push notification to device of user " + notification.getUserId());
        System.out.println("Title: Notification");
        System.out.println("Body: " + notification.getMessage());
        
        // Simulate success/failure (95% success rate)
        boolean success = Math.random() > 0.05;
        if (success) {
            notification.setStatus(org.lld.practice.design_notification_system.improved_solution.models.NotificationStatus.SENT);
        } else {
            notification.setStatus(org.lld.practice.design_notification_system.improved_solution.models.NotificationStatus.FAILED);
            notification.setErrorMessage("Device not reachable");
        }
        
        return success;
    }
    
    @Override
    public NotificationChannel getChannelType() {
        return NotificationChannel.PUSH;
    }
}

