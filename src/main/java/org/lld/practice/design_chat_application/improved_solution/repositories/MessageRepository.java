package org.lld.practice.design_chat_application.improved_solution.repositories;

import org.lld.practice.design_chat_application.improved_solution.models.Message;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for message storage operations.
 */
public interface MessageRepository {
    /**
     * Saves a message.
     * 
     * @param message The message to save
     */
    void save(Message message);
    
    /**
     * Finds a message by ID.
     * 
     * @param messageId The message ID
     * @return Optional containing the message if found
     */
    Optional<Message> findById(String messageId);
    
    /**
     * Gets all messages for a chat.
     * 
     * @param chatId The chat ID
     * @return List of messages
     */
    List<Message> findByChatId(String chatId);
    
    /**
     * Gets messages for a chat after a specific timestamp.
     * 
     * @param chatId The chat ID
     * @param afterTimestamp Timestamp to filter messages after
     * @return List of messages
     */
    List<Message> findByChatIdAfter(String chatId, long afterTimestamp);
}

