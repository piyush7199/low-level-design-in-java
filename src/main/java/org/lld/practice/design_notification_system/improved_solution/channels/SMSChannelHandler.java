package org.lld.practice.design_notification_system.improved_solution.channels;

import org.lld.practice.design_notification_system.improved_solution.models.Notification;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationChannel;

/**
 * SMS channel handler for sending SMS notifications.
 */
public class SMSChannelHandler implements NotificationChannelHandler {
    
    @Override
    public boolean send(Notification notification) {
        // Simulate SMS sending
        System.out.println("[SMS] Sending SMS to " + notification.getUserId());
        System.out.println("Message: " + notification.getMessage());
        
        // Simulate success/failure (85% success rate)
        boolean success = Math.random() > 0.15;
        if (success) {
            notification.setStatus(org.lld.practice.design_notification_system.improved_solution.models.NotificationStatus.SENT);
        } else {
            notification.setStatus(org.lld.practice.design_notification_system.improved_solution.models.NotificationStatus.FAILED);
            notification.setErrorMessage("SMS gateway timeout");
        }
        
        return success;
    }
    
    @Override
    public NotificationChannel getChannelType() {
        return NotificationChannel.SMS;
    }
}

