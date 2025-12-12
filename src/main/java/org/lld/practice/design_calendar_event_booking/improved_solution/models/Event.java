package org.lld.practice.design_calendar_event_booking.improved_solution.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a calendar event.
 */
public class Event {
    private final String eventId;
    private final String title;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String organizerId;
    private final Set<String> participantIds;
    private EventStatus status;
    private RecurrencePattern recurrencePattern;
    
    public Event(String title, String description, LocalDateTime startTime, 
                LocalDateTime endTime, String organizerId) {
        this.eventId = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.organizerId = organizerId;
        this.participantIds = new HashSet<>();
        this.participantIds.add(organizerId);
        this.status = EventStatus.SCHEDULED;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public String getOrganizerId() {
        return organizerId;
    }
    
    public Set<String> getParticipantIds() {
        return new HashSet<>(participantIds);
    }
    
    public void addParticipant(String userId) {
        participantIds.add(userId);
    }
    
    public void removeParticipant(String userId) {
        participantIds.remove(userId);
    }
    
    public EventStatus getStatus() {
        return status;
    }
    
    public void setStatus(EventStatus status) {
        this.status = status;
    }
    
    public RecurrencePattern getRecurrencePattern() {
        return recurrencePattern;
    }
    
    public void setRecurrencePattern(RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
    
    public boolean isRecurring() {
        return recurrencePattern != null;
    }
    
    /**
     * Checks if this event overlaps with another event.
     * 
     * @param other The other event
     * @return true if events overlap
     */
    public boolean overlapsWith(Event other) {
        return this.startTime.isBefore(other.endTime) && 
               other.startTime.isBefore(this.endTime);
    }
}

