package org.lld.practice.design_notification_system.improved_solution.channels;

import org.lld.practice.design_notification_system.improved_solution.models.Notification;

/**
 * Strategy interface for notification channel handlers.
 * Different implementations handle different notification channels.
 */
public interface NotificationChannelHandler {
    /**
     * Sends a notification through this channel.
     * 
     * @param notification The notification to send
     * @return true if sent successfully, false otherwise
     */
    boolean send(Notification notification);
    
    /**
     * Gets the channel type this handler supports.
     * 
     * @return The channel type
     */
    org.lld.practice.design_notification_system.improved_solution.models.NotificationChannel getChannelType();
}

