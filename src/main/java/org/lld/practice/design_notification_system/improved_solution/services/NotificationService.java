package org.lld.practice.design_notification_system.improved_solution.services;

import org.lld.practice.design_notification_system.improved_solution.channels.NotificationChannelHandler;
import org.lld.practice.design_notification_system.improved_solution.factories.ChannelHandlerFactory;
import org.lld.practice.design_notification_system.improved_solution.models.Notification;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationChannel;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationPriority;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Main notification service that orchestrates notification sending.
 * Handles queuing, priority, and retry logic.
 */
public class NotificationService {
    private final ChannelHandlerFactory channelFactory;
    private final PriorityBlockingQueue<Notification> notificationQueue;
    private static final int MAX_RETRIES = 3;
    
    public NotificationService() {
        this.channelFactory = ChannelHandlerFactory.getInstance();
        this.notificationQueue = new PriorityBlockingQueue<>(
            11, 
            (n1, n2) -> Integer.compare(n2.getPriority().getValue(), n1.getPriority().getValue())
        );
        startNotificationProcessor();
    }
    
    /**
     * Sends a notification immediately.
     * 
     * @param userId The user ID
     * @param message The message
     * @param channel The notification channel
     * @param priority The priority level
     * @return The notification ID
     */
    public String sendNotification(String userId, String message, 
                                  NotificationChannel channel, NotificationPriority priority) {
        String notificationId = UUID.randomUUID().toString();
        Notification notification = new Notification(
            notificationId, userId, message, channel, priority
        );
        
        notificationQueue.offer(notification);
        return notificationId;
    }
    
    /**
     * Schedules a notification for later delivery.
     * 
     * @param userId The user ID
     * @param message The message
     * @param channel The notification channel
     * @param priority The priority level
     * @param scheduledTime The time to send the notification
     * @return The notification ID
     */
    public String scheduleNotification(String userId, String message,
                                      NotificationChannel channel, NotificationPriority priority,
                                      LocalDateTime scheduledTime) {
        String notificationId = UUID.randomUUID().toString();
        Notification notification = new Notification(
            notificationId, userId, message, channel, priority, scheduledTime, null
        );
        
        notificationQueue.offer(notification);
        return notificationId;
    }
    
    /**
     * Processes notifications from the queue.
     */
    private void startNotificationProcessor() {
        Thread processorThread = new Thread(() -> {
            while (true) {
                try {
                    Notification notification = notificationQueue.take();
                    
                    // Check if scheduled for future
                    if (notification.getScheduledTime().isAfter(LocalDateTime.now())) {
                        // Re-queue for later
                        notificationQueue.offer(notification);
                        Thread.sleep(1000); // Wait before checking again
                        continue;
                    }
                    
                    // Process notification with retry logic
                    processNotification(notification);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        processorThread.setDaemon(true);
        processorThread.start();
    }
    
    /**
     * Processes a notification with retry logic.
     * 
     * @param notification The notification to process
     */
    private void processNotification(Notification notification) {
        NotificationChannelHandler handler = channelFactory.getHandler(notification.getChannel());
        
        if (handler == null) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage("No handler found for channel: " + notification.getChannel());
            return;
        }
        
        int retryCount = 0;
        boolean success = false;
        
        while (retryCount < MAX_RETRIES && !success) {
            success = handler.send(notification);
            
            if (!success && retryCount < MAX_RETRIES - 1) {
                retryCount++;
                try {
                    // Exponential backoff
                    Thread.sleep((long) Math.pow(2, retryCount) * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        if (!success) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage("Failed after " + MAX_RETRIES + " retries");
        }
    }
    
    /**
     * Gets the status of a notification.
     * 
     * @param notificationId The notification ID
     * @return The notification status, or null if not found
     */
    public NotificationStatus getNotificationStatus(String notificationId) {
        // In a real implementation, this would query a database
        // For demo purposes, we return null
        return null;
    }
}

