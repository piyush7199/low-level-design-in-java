# Design A Calendar/Event Booking System

## 1. Problem Statement and Requirements

Our goal is to design a calendar system that allows users to create events, book time slots, check availability, and handle conflicts.

### Functional Requirements:

- **Event Creation**: Create events with title, description, time, duration, and participants
- **Availability Checking**: Check if a time slot is available for booking
- **Conflict Detection**: Detect and handle scheduling conflicts
- **Recurring Events**: Support recurring events (daily, weekly, monthly)
- **Event Invitations**: Send invitations and track RSVP status
- **Calendar Views**: View events by day, week, or month
- **Event Updates**: Update or cancel existing events
- **Reminders**: Set reminders for upcoming events
- **Time Zone Support**: Handle different time zones

### Non-Functional Requirements:

- **Performance**: Fast availability checks and conflict detection
- **Scalability**: Support millions of users and events
- **Concurrency**: Handle concurrent bookings without conflicts
- **Reliability**: Ensure event data consistency

---

## 2. Naive Solution: The "Starting Point"

A beginner might use a simple list of events and check conflicts linearly.

### The Thought Process:

"I'll store all events in a list and check for conflicts by iterating through all events."

```java
class Calendar {
    private List<Event> events = new ArrayList<>();
    
    public void addEvent(Event event) {
        // Check for conflicts
        for (Event e : events) {
            if (isOverlapping(e, event)) {
                throw new ConflictException("Event conflicts with existing event");
            }
        }
        events.add(event);
    }
    
    private boolean isOverlapping(Event e1, Event e2) {
        return e1.getStartTime().isBefore(e2.getEndTime()) &&
               e2.getStartTime().isBefore(e1.getEndTime());
    }
}
```

### Limitations and Design Flaws:

1. **Performance Issues**: Linear conflict checking is O(n) for each event
2. **No Efficient Queries**: Finding events in a time range requires full scan
3. **No Recurring Events**: Cannot handle recurring events
4. **Concurrency Issues**: Race conditions when multiple users book simultaneously
5. **No Time Zone Support**: Assumes single time zone
6. **No Reminders**: Cannot set reminders

---

## 3. Improved Solution: The "Mentor's Guidance"

Use interval trees or sorted data structures for efficient conflict detection and time-based queries.

### Design Patterns Used:

1. **Strategy Pattern**: For different conflict resolution strategies
2. **Factory Pattern**: For creating different types of events
3. **Observer Pattern**: For reminders and notifications
4. **State Pattern**: For event status (scheduled, cancelled, completed)
5. **Repository Pattern**: For event storage abstraction

### Core Classes and Interactions:

1. **CalendarService** (Orchestrator):
   - Main entry point for calendar operations
   - Manages event creation, updates, and conflict detection

2. **Event** (Model):
   - Represents a calendar event with all metadata
   - Supports recurring patterns

3. **ConflictResolver** (Strategy Interface):
   - Defines conflict resolution strategies
   - Implementations: RejectConflictResolver, AutoResolveConflictResolver

4. **EventRepository** (Repository):
   - Abstracts event storage
   - Handles efficient time-based queries

5. **ReminderService** (Observer):
   - Handles event reminders
   - Notifies users before events

### Key Design Benefits:

- **Performance**: Efficient conflict detection using interval trees
- **Flexibility**: Strategy pattern for different conflict resolution approaches
- **Extensibility**: Easy to add new event types or features
- **Reliability**: Proper concurrency handling

---

## 4. Final Design Overview

The improved solution uses Strategy pattern for conflict resolution, efficient data structures for time-based queries, and Observer pattern for reminders to create a scalable calendar system.

