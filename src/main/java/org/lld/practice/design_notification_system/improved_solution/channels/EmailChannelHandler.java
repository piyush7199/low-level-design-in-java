package org.lld.practice.design_notification_system.improved_solution.channels;

import org.lld.practice.design_notification_system.improved_solution.models.Notification;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationChannel;

/**
 * Email channel handler for sending email notifications.
 */
public class EmailChannelHandler implements NotificationChannelHandler {
    
    @Override
    public boolean send(Notification notification) {
        // Simulate email sending
        System.out.println("[EMAIL] Sending email to " + notification.getUserId() + 
                         " at " + notification.getUserId() + "@example.com");
        System.out.println("Subject: Notification");
        System.out.println("Body: " + notification.getMessage());
        
        // Simulate success/failure (90% success rate)
        boolean success = Math.random() > 0.1;
        if (success) {
            notification.setStatus(org.lld.practice.design_notification_system.improved_solution.models.NotificationStatus.SENT);
        } else {
            notification.setStatus(org.lld.practice.design_notification_system.improved_solution.models.NotificationStatus.FAILED);
            notification.setErrorMessage("Email service unavailable");
        }
        
        return success;
    }
    
    @Override
    public NotificationChannel getChannelType() {
        return NotificationChannel.EMAIL;
    }
}

