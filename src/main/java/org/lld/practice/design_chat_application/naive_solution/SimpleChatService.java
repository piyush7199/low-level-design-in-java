package org.lld.practice.design_chat_application.naive_solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive implementation of a chat application.
 * 
 * This demonstrates common pitfalls:
 * - No real-time updates
 * - No group chat support
 * - No message status tracking
 * - No persistence
 * - No user management
 */
public class SimpleChatService {
    private final List<Message> messages = new ArrayList<>();
    
    public void sendMessage(String from, String to, String text) {
        Message msg = new Message(from, to, text);
        messages.add(msg);
        System.out.println(from + " -> " + to + ": " + text);
    }
    
    public List<Message> getMessages(String userId) {
        List<Message> userMessages = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getFrom().equals(userId) || msg.getTo().equals(userId)) {
                userMessages.add(msg);
            }
        }
        return userMessages;
    }
    
    static class Message {
        private final String from;
        private final String to;
        private final String text;
        private final long timestamp;
        
        public Message(String from, String to, String text) {
            this.from = from;
            this.to = to;
            this.text = text;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getFrom() {
            return from;
        }
        
        public String getTo() {
            return to;
        }
        
        public String getText() {
            return text;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
}

