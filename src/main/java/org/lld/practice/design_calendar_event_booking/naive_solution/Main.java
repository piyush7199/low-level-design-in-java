package org.lld.practice.design_calendar_event_booking.naive_solution;

import java.time.LocalDateTime;

/**
 * Demo of naive calendar implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Calendar System Demo ===\n");
        
        SimpleCalendar calendar = new SimpleCalendar();
        
        LocalDateTime now = LocalDateTime.now();
        
        calendar.addEvent("Meeting 1", now.plusHours(1), now.plusHours(2));
        calendar.addEvent("Meeting 2", now.plusHours(3), now.plusHours(4));
        
        try {
            calendar.addEvent("Conflicting Meeting", now.plusHours(1).plusMinutes(30), 
                           now.plusHours(2).plusMinutes(30));
        } catch (RuntimeException e) {
            System.out.println("Conflict detected: " + e.getMessage());
        }
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- Linear conflict checking (O(n))");
        System.out.println("- No efficient time-based queries");
        System.out.println("- Not thread-safe");
        System.out.println("- No recurring events");
        System.out.println("- No reminders");
    }
}

