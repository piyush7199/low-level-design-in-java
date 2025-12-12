package org.lld.practice.design_calendar_event_booking.improved_solution.repositories;

import org.lld.practice.design_calendar_event_booking.improved_solution.models.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for event storage operations.
 */
public interface EventRepository {
    /**
     * Saves an event.
     * 
     * @param event The event to save
     */
    void save(Event event);
    
    /**
     * Finds an event by ID.
     * 
     * @param eventId The event ID
     * @return Optional containing the event if found
     */
    Optional<Event> findById(String eventId);
    
    /**
     * Finds events for a user in a time range.
     * 
     * @param userId The user ID
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return List of events
     */
    List<Event> findByUserAndTimeRange(String userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Finds conflicting events for a time range.
     * 
     * @param userId The user ID
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return List of conflicting events
     */
    List<Event> findConflictingEvents(String userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Deletes an event.
     * 
     * @param eventId The event ID
     */
    void delete(String eventId);
}

