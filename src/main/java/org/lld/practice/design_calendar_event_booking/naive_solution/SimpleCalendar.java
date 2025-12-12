package org.lld.practice.design_calendar_event_booking.naive_solution;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Naive implementation of a calendar system.
 * 
 * This demonstrates common pitfalls:
 * - Linear conflict checking (O(n))
 * - No efficient time-based queries
 * - Not thread-safe
 * - No recurring events support
 */
public class SimpleCalendar {
    private final List<Event> events = new ArrayList<>();
    
    public void addEvent(String title, LocalDateTime start, LocalDateTime end) {
        Event newEvent = new Event(title, start, end);
        
        // Check for conflicts - O(n) operation
        for (Event event : events) {
            if (isOverlapping(event, newEvent)) {
                throw new RuntimeException("Event conflicts with existing event: " + event.getTitle());
            }
        }
        
        events.add(newEvent);
        System.out.println("Event added: " + title);
    }
    
    public List<Event> getEvents(LocalDateTime start, LocalDateTime end) {
        List<Event> result = new ArrayList<>();
        // Linear scan - O(n)
        for (Event event : events) {
            if (isOverlapping(event, new Event("", start, end))) {
                result.add(event);
            }
        }
        return result;
    }
    
    private boolean isOverlapping(Event e1, Event e2) {
        return e1.getStart().isBefore(e2.getEnd()) && 
               e2.getStart().isBefore(e1.getEnd());
    }
    
    static class Event {
        private final String title;
        private final LocalDateTime start;
        private final LocalDateTime end;
        
        public Event(String title, LocalDateTime start, LocalDateTime end) {
            this.title = title;
            this.start = start;
            this.end = end;
        }
        
        public String getTitle() {
            return title;
        }
        
        public LocalDateTime getStart() {
            return start;
        }
        
        public LocalDateTime getEnd() {
            return end;
        }
    }
}

