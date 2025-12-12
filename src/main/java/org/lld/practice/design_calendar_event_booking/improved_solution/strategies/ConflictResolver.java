package org.lld.practice.design_calendar_event_booking.improved_solution.strategies;

import org.lld.practice.design_calendar_event_booking.improved_solution.models.Event;

/**
 * Strategy interface for resolving scheduling conflicts.
 * Different implementations can reject, auto-resolve, or suggest alternatives.
 */
public interface ConflictResolver {
    /**
     * Resolves a conflict between a new event and existing events.
     * 
     * @param newEvent The new event to schedule
     * @param conflictingEvents List of conflicting events
     * @return true if conflict is resolved, false otherwise
     */
    boolean resolveConflict(Event newEvent, java.util.List<Event> conflictingEvents);
}

