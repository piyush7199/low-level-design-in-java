package org.lld.practice.design_chat_application.improved_solution.models;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a chat (can be one-on-one or group).
 */
public class Chat {
    private final String chatId;
    private final ChatType type;
    private final Set<String> participants;
    private final String createdBy;
    
    public Chat(String chatId, ChatType type, String createdBy) {
        this.chatId = chatId;
        this.type = type;
        this.createdBy = createdBy;
        this.participants = new HashSet<>();
        this.participants.add(createdBy);
    }
    
    public String getChatId() {
        return chatId;
    }
    
    public ChatType getType() {
        return type;
    }
    
    public Set<String> getParticipants() {
        return new HashSet<>(participants);
    }
    
    public void addParticipant(String userId) {
        participants.add(userId);
    }
    
    public void removeParticipant(String userId) {
        participants.remove(userId);
    }
    
    public boolean hasParticipant(String userId) {
        return participants.contains(userId);
    }
}

