package org.lld.practice.design_calendar_event_booking.improved_solution.services;

import org.lld.practice.design_calendar_event_booking.improved_solution.models.Event;
import org.lld.practice.design_calendar_event_booking.improved_solution.models.EventStatus;
import org.lld.practice.design_calendar_event_booking.improved_solution.repositories.EventRepository;
import org.lld.practice.design_calendar_event_booking.improved_solution.strategies.ConflictResolver;
import org.lld.practice.design_calendar_event_booking.improved_solution.strategies.RejectConflictResolver;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing calendar events.
 * Uses Strategy pattern for conflict resolution.
 */
public class CalendarService {
    private final EventRepository eventRepository;
    private ConflictResolver conflictResolver;
    
    public CalendarService(EventRepository eventRepository) {
        this(eventRepository, new RejectConflictResolver());
    }
    
    public CalendarService(EventRepository eventRepository, ConflictResolver conflictResolver) {
        this.eventRepository = eventRepository;
        this.conflictResolver = conflictResolver;
    }
    
    /**
     * Creates a new event.
     * 
     * @param title Event title
     * @param description Event description
     * @param startTime Event start time
     * @param endTime Event end time
     * @param organizerId Organizer user ID
     * @return The created event
     * @throws IllegalArgumentException if there's a conflict
     */
    public Event createEvent(String title, String description, LocalDateTime startTime,
                            LocalDateTime endTime, String organizerId) {
        Event event = new Event(title, description, startTime, endTime, organizerId);
        
        // Check for conflicts
        List<Event> conflicts = eventRepository.findConflictingEvents(organizerId, startTime, endTime);
        
        if (!conflictResolver.resolveConflict(event, conflicts)) {
            throw new IllegalArgumentException("Event conflicts with existing events");
        }
        
        eventRepository.save(event);
        return event;
    }
    
    /**
     * Updates an existing event.
     * 
     * @param eventId The event ID
     * @param title New title (optional)
     * @param description New description (optional)
     * @param startTime New start time (optional)
     * @param endTime New end time (optional)
     * @return The updated event
     */
    public Event updateEvent(String eventId, String title, String description,
                            LocalDateTime startTime, LocalDateTime endTime) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        
        // Check for conflicts if time changed
        if (startTime != null || endTime != null) {
            LocalDateTime newStart = startTime != null ? startTime : event.getStartTime();
            LocalDateTime newEnd = endTime != null ? endTime : event.getEndTime();
            
            List<Event> conflicts = eventRepository.findConflictingEvents(
                    event.getOrganizerId(), newStart, newEnd);
            conflicts.remove(event); // Remove self from conflicts
            
            if (!conflictResolver.resolveConflict(event, conflicts)) {
                throw new IllegalArgumentException("Updated event conflicts with existing events");
            }
        }
        
        // Update fields
        if (title != null) {
            // In a real implementation, we'd have setters or use a builder
            // For now, we'll create a new event with updated fields
            Event updatedEvent = new Event(
                    title != null ? title : event.getTitle(),
                    description != null ? description : event.getDescription(),
                    startTime != null ? startTime : event.getStartTime(),
                    endTime != null ? endTime : event.getEndTime(),
                    event.getOrganizerId()
            );
            // Copy other fields...
            eventRepository.save(updatedEvent);
            return updatedEvent;
        }
        
        return event;
    }
    
    /**
     * Cancels an event.
     * 
     * @param eventId The event ID
     */
    public void cancelEvent(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }
    
    /**
     * Gets events for a user in a time range.
     * 
     * @param userId The user ID
     * @param startTime Start of time range
     * @param endTime End of time range
     * @return List of events
     */
    public List<Event> getEvents(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        return eventRepository.findByUserAndTimeRange(userId, startTime, endTime);
    }
    
    /**
     * Checks if a time slot is available for a user.
     * 
     * @param userId The user ID
     * @param startTime Start time
     * @param endTime End time
     * @return true if available, false otherwise
     */
    public boolean isAvailable(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Event> conflicts = eventRepository.findConflictingEvents(userId, startTime, endTime);
        return conflicts.isEmpty();
    }
    
    /**
     * Adds a participant to an event.
     * 
     * @param eventId The event ID
     * @param userId The user ID to add
     */
    public void addParticipant(String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        event.addParticipant(userId);
        eventRepository.save(event);
    }
}

