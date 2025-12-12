# Design A Chat Application

## 1. Problem Statement and Requirements

Our goal is to design a real-time chat application that supports one-on-one messaging, group chats, message delivery status, and online presence.

### Functional Requirements:

- **User Management**: User registration, authentication, and profile management
- **One-on-One Chat**: Send and receive messages between two users
- **Group Chat**: Create groups, add/remove members, send messages to groups
- **Message Types**: Support text, images, files, and emojis
- **Message Status**: Track message status (SENT, DELIVERED, READ)
- **Online Presence**: Show user online/offline status
- **Message History**: Store and retrieve chat history
- **Typing Indicators**: Show when a user is typing
- **Notifications**: Notify users of new messages when offline

### Non-Functional Requirements:

- **Real-time Communication**: Low latency message delivery
- **Scalability**: Support millions of concurrent users
- **Reliability**: Messages should not be lost
- **Security**: End-to-end encryption support
- **Performance**: Fast message delivery and retrieval

---

## 2. Naive Solution: The "Starting Point"

A beginner might create a simple class that stores messages in memory.

### The Thought Process:

"I'll create a ChatService class that stores all messages in a list and broadcasts them to users."

```java
class ChatService {
    private List<Message> messages = new ArrayList<>();
    
    public void sendMessage(String from, String to, String text) {
        Message msg = new Message(from, to, text);
        messages.add(msg);
        System.out.println(from + " to " + to + ": " + text);
    }
}
```

### Limitations and Design Flaws:

1. **No Real-time Updates**: Messages stored but not pushed to recipients
2. **No Group Support**: Only one-on-one messaging
3. **No Message Status**: Cannot track delivery or read status
4. **No Persistence**: Messages lost on restart
5. **No User Management**: No authentication or user management
6. **Scalability Issues**: Single server, no distribution
7. **No Presence**: Cannot track online/offline status

---

## 3. Improved Solution: The "Mentor's Guidance"

Separate concerns: message storage, real-time delivery, user management, and presence tracking.

### Design Patterns Used:

1. **Observer Pattern**: For real-time message delivery and notifications
2. **Strategy Pattern**: For different message types and delivery strategies
3. **Factory Pattern**: For creating different types of messages
4. **State Pattern**: For user presence (online/offline/away)
5. **Repository Pattern**: For message and user storage abstraction

### Core Classes and Interactions:

1. **ChatService** (Orchestrator):
   - Main entry point for chat operations
   - Manages message routing and delivery
   - Coordinates with message handlers and observers

2. **Message** (Model):
   - Represents a chat message with metadata
   - Supports different message types

3. **MessageHandler** (Strategy Interface):
   - Handles different message types
   - Implementations: TextMessageHandler, ImageMessageHandler, FileMessageHandler

4. **ChatObserver** (Observer Interface):
   - Notifies about new messages
   - Implementations: UserNotificationObserver, PresenceObserver

5. **UserPresence** (State):
   - Tracks user online/offline/away status
   - Updates in real-time

6. **MessageRepository** (Repository):
   - Abstracts message storage
   - Handles persistence and retrieval

### Key Design Benefits:

- **Real-time Updates**: Observer pattern enables instant message delivery
- **Extensibility**: Easy to add new message types or features
- **Scalability**: Repository pattern allows distributed storage
- **Maintainability**: Clear separation of concerns
- **Testability**: Each component can be tested independently

---

## 4. Final Design Overview

The improved solution uses Observer, Strategy, and State patterns to create a scalable, real-time chat application with support for multiple message types, group chats, and presence tracking.

