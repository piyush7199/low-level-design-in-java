package org.lld.practice.design_calendar_event_booking.improved_solution.models;

/**
 * Represents a recurrence pattern for recurring events.
 */
public class RecurrencePattern {
    private final RecurrenceType type;
    private final int interval; // Every N days/weeks/months
    private final int count; // Number of occurrences (null means infinite)
    
    public RecurrencePattern(RecurrenceType type, int interval) {
        this(type, interval, -1); // -1 means infinite
    }
    
    public RecurrencePattern(RecurrenceType type, int interval, int count) {
        this.type = type;
        this.interval = interval;
        this.count = count;
    }
    
    public RecurrenceType getType() {
        return type;
    }
    
    public int getInterval() {
        return interval;
    }
    
    public int getCount() {
        return count;
    }
    
    public boolean isInfinite() {
        return count == -1;
    }
}

