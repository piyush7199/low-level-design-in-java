package org.lld.practice.design_chat_application.improved_solution.models;

import java.time.LocalDateTime;

/**
 * Represents a chat message.
 */
public class Message {
    private final String messageId;
    private final String fromUserId;
    private final String toUserId;
    private final String chatId; // For group chats
    private final MessageType type;
    private final String content;
    private final LocalDateTime timestamp;
    private MessageStatus status;
    
    public Message(String messageId, String fromUserId, String toUserId, 
                  String chatId, MessageType type, String content) {
        this.messageId = messageId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.chatId = chatId;
        this.type = type;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.status = MessageStatus.SENT;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public String getFromUserId() {
        return fromUserId;
    }
    
    public String getToUserId() {
        return toUserId;
    }
    
    public String getChatId() {
        return chatId;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public MessageStatus getStatus() {
        return status;
    }
    
    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}

