package org.lld.practice.design_chat_application.improved_solution.services;

import org.lld.practice.design_chat_application.improved_solution.models.Chat;
import org.lld.practice.design_chat_application.improved_solution.models.ChatType;
import org.lld.practice.design_chat_application.improved_solution.models.Message;
import org.lld.practice.design_chat_application.improved_solution.models.MessageStatus;
import org.lld.practice.design_chat_application.improved_solution.models.MessageType;
import org.lld.practice.design_chat_application.improved_solution.models.PresenceStatus;
import org.lld.practice.design_chat_application.improved_solution.models.UserPresence;
import org.lld.practice.design_chat_application.improved_solution.observers.ChatObserver;
import org.lld.practice.design_chat_application.improved_solution.repositories.MessageRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Main chat service that orchestrates chat operations.
 * Uses Observer pattern for real-time updates.
 */
public class ChatService {
    private final MessageRepository messageRepository;
    private final List<ChatObserver> observers;
    private final Map<String, Chat> chats;
    private final Map<String, UserPresence> userPresences;
    
    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.observers = new CopyOnWriteArrayList<>();
        this.chats = new ConcurrentHashMap<>();
        this.userPresences = new ConcurrentHashMap<>();
    }
    
    /**
     * Sends a message in a chat.
     * 
     * @param fromUserId The sender's user ID
     * @param chatId The chat ID
     * @param content The message content
     * @param type The message type
     * @return The created message
     */
    public Message sendMessage(String fromUserId, String chatId, String content, MessageType type) {
        Chat chat = chats.get(chatId);
        if (chat == null || !chat.hasParticipant(fromUserId)) {
            throw new IllegalArgumentException("Chat not found or user not a participant");
        }
        
        String messageId = UUID.randomUUID().toString();
        String toUserId = getOtherParticipant(chat, fromUserId);
        
        Message message = new Message(messageId, fromUserId, toUserId, chatId, type, content);
        message.setStatus(MessageStatus.SENT);
        
        messageRepository.save(message);
        
        // Notify observers (Observer pattern)
        notifyMessageReceived(message);
        
        return message;
    }
    
    /**
     * Creates a one-on-one chat between two users.
     * 
     * @param user1Id First user ID
     * @param user2Id Second user ID
     * @return The created chat
     */
    public Chat createOneOnOneChat(String user1Id, String user2Id) {
        String chatId = generateChatId(user1Id, user2Id);
        
        if (chats.containsKey(chatId)) {
            return chats.get(chatId);
        }
        
        Chat chat = new Chat(chatId, ChatType.ONE_ON_ONE, user1Id);
        chat.addParticipant(user2Id);
        chats.put(chatId, chat);
        
        return chat;
    }
    
    /**
     * Creates a group chat.
     * 
     * @param createdBy The user creating the group
     * @param participantIds List of participant user IDs
     * @return The created chat
     */
    public Chat createGroupChat(String createdBy, List<String> participantIds) {
        String chatId = UUID.randomUUID().toString();
        Chat chat = new Chat(chatId, ChatType.GROUP, createdBy);
        
        for (String participantId : participantIds) {
            chat.addParticipant(participantId);
        }
        
        chats.put(chatId, chat);
        return chat;
    }
    
    /**
     * Marks a message as read.
     * 
     * @param messageId The message ID
     * @param userId The user who read the message
     */
    public void markMessageAsRead(String messageId, String userId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            if (message.getToUserId().equals(userId)) {
                message.setStatus(MessageStatus.READ);
                messageRepository.save(message);
            }
        });
    }
    
    /**
     * Updates user presence status.
     * 
     * @param userId The user ID
     * @param status The new presence status
     */
    public void updatePresence(String userId, PresenceStatus status) {
        UserPresence presence = userPresences.computeIfAbsent(userId, UserPresence::new);
        boolean wasOnline = presence.getStatus() == PresenceStatus.ONLINE;
        presence.setStatus(status);
        boolean isOnline = status == PresenceStatus.ONLINE;
        
        if (wasOnline != isOnline) {
            notifyPresenceChanged(userId, isOnline);
        }
    }
    
    /**
     * Adds an observer to receive chat events.
     * 
     * @param observer The observer to add
     */
    public void addObserver(ChatObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Removes an observer.
     * 
     * @param observer The observer to remove
     */
    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Gets chat history.
     * 
     * @param chatId The chat ID
     * @return List of messages
     */
    public List<Message> getChatHistory(String chatId) {
        return messageRepository.findByChatId(chatId);
    }
    
    private void notifyMessageReceived(Message message) {
        for (ChatObserver observer : observers) {
            observer.onMessageReceived(message);
        }
    }
    
    private void notifyPresenceChanged(String userId, boolean isOnline) {
        for (ChatObserver observer : observers) {
            observer.onPresenceChanged(userId, isOnline);
        }
    }
    
    private String getOtherParticipant(Chat chat, String userId) {
        if (chat.getType() == ChatType.ONE_ON_ONE) {
            return chat.getParticipants().stream()
                    .filter(id -> !id.equals(userId))
                    .findFirst()
                    .orElse(null);
        }
        return null; // For group chats, return null or handle differently
    }
    
    private String generateChatId(String user1Id, String user2Id) {
        // Generate consistent chat ID for one-on-one chats
        return user1Id.compareTo(user2Id) < 0 
                ? user1Id + "_" + user2Id 
                : user2Id + "_" + user1Id;
    }
}

