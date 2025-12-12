package org.lld.practice.design_notification_system.improved_solution.models;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents a notification with all its metadata.
 */
public class Notification {
    private final String notificationId;
    private final String userId;
    private final String message;
    private final NotificationChannel channel;
    private final NotificationPriority priority;
    private final LocalDateTime scheduledTime;
    private final Map<String, String> templateVariables;
    private NotificationStatus status;
    private LocalDateTime sentAt;
    private String errorMessage;
    
    public Notification(String notificationId, String userId, String message, 
                       NotificationChannel channel, NotificationPriority priority) {
        this(notificationId, userId, message, channel, priority, LocalDateTime.now(), null);
    }
    
    public Notification(String notificationId, String userId, String message,
                       NotificationChannel channel, NotificationPriority priority,
                       LocalDateTime scheduledTime, Map<String, String> templateVariables) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.channel = channel;
        this.priority = priority;
        this.scheduledTime = scheduledTime;
        this.templateVariables = templateVariables;
        this.status = NotificationStatus.PENDING;
    }
    
    public String getNotificationId() {
        return notificationId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public NotificationChannel getChannel() {
        return channel;
    }
    
    public NotificationPriority getPriority() {
        return priority;
    }
    
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
    
    public Map<String, String> getTemplateVariables() {
        return templateVariables;
    }
    
    public NotificationStatus getStatus() {
        return status;
    }
    
    public void setStatus(NotificationStatus status) {
        this.status = status;
        if (status == NotificationStatus.SENT || status == NotificationStatus.DELIVERED) {
            this.sentAt = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

