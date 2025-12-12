package org.lld.practice.design_calendar_event_booking.improved_solution.strategies;

import org.lld.practice.design_calendar_event_booking.improved_solution.models.Event;

import java.util.List;

/**
 * Conflict resolver that rejects events with conflicts.
 */
public class RejectConflictResolver implements ConflictResolver {
    
    @Override
    public boolean resolveConflict(Event newEvent, List<Event> conflictingEvents) {
        // Reject if there are any conflicts
        return conflictingEvents.isEmpty();
    }
}

