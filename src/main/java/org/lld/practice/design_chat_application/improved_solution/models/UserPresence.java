package org.lld.practice.design_chat_application.improved_solution.models;

import java.time.LocalDateTime;

/**
 * Represents user presence status.
 */
public class UserPresence {
    private final String userId;
    private PresenceStatus status;
    private LocalDateTime lastSeen;
    
    public UserPresence(String userId) {
        this.userId = userId;
        this.status = PresenceStatus.OFFLINE;
        this.lastSeen = LocalDateTime.now();
    }
    
    public String getUserId() {
        return userId;
    }
    
    public PresenceStatus getStatus() {
        return status;
    }
    
    public void setStatus(PresenceStatus status) {
        this.status = status;
        if (status == PresenceStatus.OFFLINE) {
            this.lastSeen = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getLastSeen() {
        return lastSeen;
    }
}

