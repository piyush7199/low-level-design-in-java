package org.lld.practice.design_chat_application.improved_solution.observers;

import org.lld.practice.design_chat_application.improved_solution.models.Message;

/**
 * Observer interface for chat events.
 * Observer pattern: Notifies observers about new messages and events.
 */
public interface ChatObserver {
    /**
     * Called when a new message is received.
     * 
     * @param message The new message
     */
    void onMessageReceived(Message message);
    
    /**
     * Called when a user's presence status changes.
     * 
     * @param userId The user ID
     * @param isOnline True if user is online, false otherwise
     */
    void onPresenceChanged(String userId, boolean isOnline);
    
    /**
     * Called when a user starts typing.
     * 
     * @param userId The user ID who is typing
     * @param chatId The chat ID
     */
    void onTypingStarted(String userId, String chatId);
    
    /**
     * Called when a user stops typing.
     * 
     * @param userId The user ID who stopped typing
     * @param chatId The chat ID
     */
    void onTypingStopped(String userId, String chatId);
}

