package org.lld.practice.design_calendar_event_booking.improved_solution.repositories;

import org.lld.practice.design_calendar_event_booking.improved_solution.models.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of event repository.
 * In production, this would use a database with proper indexing.
 */
public class InMemoryEventRepository implements EventRepository {
    private final Map<String, Event> eventStore = new ConcurrentHashMap<>();
    private final Map<String, List<String>> userEvents = new ConcurrentHashMap<>();
    
    @Override
    public void save(Event event) {
        eventStore.put(event.getEventId(), event);
        
        // Index by participants
        for (String participantId : event.getParticipantIds()) {
            userEvents.computeIfAbsent(participantId, k -> new ArrayList<>()).add(event.getEventId());
        }
    }
    
    @Override
    public Optional<Event> findById(String eventId) {
        return Optional.ofNullable(eventStore.get(eventId));
    }
    
    @Override
    public List<Event> findByUserAndTimeRange(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        List<String> eventIds = userEvents.getOrDefault(userId, new ArrayList<>());
        return eventIds.stream()
                .map(eventStore::get)
                .filter(event -> event != null && 
                        event.getStartTime().isBefore(endTime) && 
                        event.getEndTime().isAfter(startTime))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Event> findConflictingEvents(String userId, LocalDateTime startTime, LocalDateTime endTime) {
        List<String> eventIds = userEvents.getOrDefault(userId, new ArrayList<>());
        return eventIds.stream()
                .map(eventStore::get)
                .filter(event -> event != null && event.overlapsWith(createTemporaryEvent(startTime, endTime)))
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(String eventId) {
        Event event = eventStore.remove(eventId);
        if (event != null) {
            // Remove from user indexes
            for (String participantId : event.getParticipantIds()) {
                List<String> events = userEvents.get(participantId);
                if (events != null) {
                    events.remove(eventId);
                }
            }
        }
    }
    
    private Event createTemporaryEvent(LocalDateTime startTime, LocalDateTime endTime) {
        return new Event("", "", startTime, endTime, "");
    }
}

