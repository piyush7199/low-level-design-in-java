package org.lld.practice.design_calendar_event_booking.improved_solution;

import org.lld.practice.design_calendar_event_booking.improved_solution.models.Event;
import org.lld.practice.design_calendar_event_booking.improved_solution.repositories.EventRepository;
import org.lld.practice.design_calendar_event_booking.improved_solution.repositories.InMemoryEventRepository;
import org.lld.practice.design_calendar_event_booking.improved_solution.services.CalendarService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Demo of improved calendar/event booking system.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Improved Calendar/Event Booking System Demo ===\n");
        
        EventRepository repository = new InMemoryEventRepository();
        CalendarService calendarService = new CalendarService(repository);
        
        LocalDateTime now = LocalDateTime.now();
        
        System.out.println("1. Creating events:");
        Event meeting1 = calendarService.createEvent(
                "Team Meeting",
                "Weekly team sync",
                now.plusHours(1),
                now.plusHours(2),
                "alice"
        );
        System.out.println("Event created: " + meeting1.getTitle() + " (ID: " + meeting1.getEventId() + ")");
        
        Event meeting2 = calendarService.createEvent(
                "Client Call",
                "Discuss project requirements",
                now.plusHours(3),
                now.plusHours(4),
                "alice"
        );
        System.out.println("Event created: " + meeting2.getTitle() + " (ID: " + meeting2.getEventId() + ")");
        
        System.out.println("\n2. Checking availability:");
        boolean available = calendarService.isAvailable("alice", 
                now.plusHours(5), now.plusHours(6));
        System.out.println("Time slot 5-6 hours from now is available: " + available);
        
        System.out.println("\n3. Attempting to create conflicting event:");
        try {
            calendarService.createEvent(
                    "Conflicting Meeting",
                    "This should fail",
                    now.plusHours(1).plusMinutes(30),
                    now.plusHours(2).plusMinutes(30),
                    "alice"
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Conflict detected: " + e.getMessage());
        }
        
        System.out.println("\n4. Getting events for user:");
        List<Event> events = calendarService.getEvents("alice", now, now.plusDays(1));
        System.out.println("Found " + events.size() + " events:");
        events.forEach(e -> System.out.println("  - " + e.getTitle() + " (" + 
                e.getStartTime() + " to " + e.getEndTime() + ")"));
        
        System.out.println("\n5. Adding participant to event:");
        calendarService.addParticipant(meeting1.getEventId(), "bob");
        System.out.println("Participant added to " + meeting1.getTitle());
        
        System.out.println("\n6. Cancelling an event:");
        calendarService.cancelEvent(meeting2.getEventId());
        System.out.println("Event cancelled: " + meeting2.getTitle());
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Efficient conflict detection");
        System.out.println("✓ Strategy pattern for conflict resolution");
        System.out.println("✓ Support for recurring events");
        System.out.println("✓ Participant management");
        System.out.println("✓ Time-based queries");
        System.out.println("✓ Repository pattern for storage abstraction");
        System.out.println("✓ Easy to extend with reminders and notifications");
    }
}

