package org.lld.practice.design_chat_application.improved_solution.observers;

import org.lld.practice.design_chat_application.improved_solution.models.Message;

/**
 * Observer that handles notifications for new messages.
 */
public class NotificationObserver implements ChatObserver {
    
    @Override
    public void onMessageReceived(Message message) {
        // In a real implementation, this would send push notifications
        System.out.println("[NOTIFICATION] New message from " + message.getFromUserId() + 
                         " in chat " + message.getChatId());
    }
    
    @Override
    public void onPresenceChanged(String userId, boolean isOnline) {
        // Handle presence change notifications if needed
    }
    
    @Override
    public void onTypingStarted(String userId, String chatId) {
        // Handle typing indicator
        System.out.println("[TYPING] " + userId + " is typing in chat " + chatId);
    }
    
    @Override
    public void onTypingStopped(String userId, String chatId) {
        // Handle typing stopped
    }
}

