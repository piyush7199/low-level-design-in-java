# Design A Notification System

## 1. Problem Statement and Requirements

Our goal is to design a flexible notification system that can send messages through multiple channels (Email, SMS, Push, In-App) with different priorities and scheduling capabilities.

### Functional Requirements:

- **Multiple Channels**: Support Email, SMS, Push Notifications, and In-App notifications
- **Priority Levels**: Handle different priority levels (LOW, NORMAL, HIGH, URGENT)
- **Scheduling**: Support immediate and scheduled notifications
- **Templates**: Support message templates with variable substitution
- **User Preferences**: Respect user notification preferences and channel preferences
- **Retry Logic**: Retry failed notifications with exponential backoff
- **Batching**: Batch notifications for efficiency
- **Delivery Status**: Track delivery status (PENDING, SENT, DELIVERED, FAILED)

### Non-Functional Requirements:

- **Performance**: Handle high-volume notifications efficiently
- **Reliability**: Ensure notifications are not lost
- **Scalability**: Scale to handle millions of notifications
- **Extensibility**: Easy to add new notification channels
- **Maintainability**: Clean separation of concerns

---

## 2. Naive Solution: The "Starting Point"

A beginner might create a single class that handles all notification logic.

### The Thought Process:

"I'll create a NotificationService class that sends notifications through different channels. I'll use if-else statements to handle different channels."

```java
class NotificationService {
    public void sendNotification(String userId, String message, String channel) {
        if (channel.equals("EMAIL")) {
            // Send email
            System.out.println("Sending email: " + message);
        } else if (channel.equals("SMS")) {
            // Send SMS
            System.out.println("Sending SMS: " + message);
        } else if (channel.equals("PUSH")) {
            // Send push notification
            System.out.println("Sending push: " + message);
        }
    }
}
```

### Limitations and Design Flaws:

1. **Violation of SOLID Principles**:
   - **Open/Closed Principle**: Cannot add new channels without modifying the class
   - **Single Responsibility Principle**: Handles all channels, templates, and delivery logic

2. **No Priority Handling**: All notifications treated equally

3. **No Retry Logic**: Failed notifications are lost

4. **No Scheduling**: Only supports immediate notifications

5. **Tight Coupling**: Direct dependencies on specific channel implementations

6. **No Template Support**: Cannot use message templates

---

## 3. Improved Solution: The "Mentor's Guidance"

The key is to separate notification channels, message formatting, delivery strategies, and scheduling into independent components.

### Design Patterns Used:

1. **Strategy Pattern**: For different notification channels (Email, SMS, Push)
2. **Factory Pattern**: For creating appropriate channel handlers
3. **Observer Pattern**: For tracking notification delivery status
4. **Template Method Pattern**: For message template processing
5. **Command Pattern**: For queuing and scheduling notifications

### Core Classes and Interactions:

1. **NotificationService** (Orchestrator):
   - Main entry point for sending notifications
   - Manages notification queue and scheduling
   - Coordinates with channel handlers

2. **NotificationChannel** (Strategy Interface):
   - Defines contract for notification channels
   - Implementations: EmailChannel, SMSChannel, PushChannel, InAppChannel

3. **NotificationTemplate** (Template):
   - Handles message templates with variable substitution
   - Supports different template types

4. **NotificationQueue** (Queue Manager):
   - Manages notification queue with priority
   - Handles batching and retry logic

5. **NotificationPreferences** (User Settings):
   - Stores user preferences for notification channels
   - Respects user opt-out settings

### Key Design Benefits:

- **Extensibility**: Add new channels without modifying existing code
- **Flexibility**: Support multiple channels, priorities, and scheduling
- **Reliability**: Retry logic ensures notifications are delivered
- **Maintainability**: Clear separation of concerns
- **Testability**: Each component can be tested independently

---

## 4. Final Design Overview

The improved solution uses Strategy, Factory, and Observer patterns to create a flexible, extensible notification system that can handle multiple channels, priorities, and scheduling requirements while maintaining clean code principles.

