package org.lld.practice.design_notification_system.improved_solution.factories;

import org.lld.practice.design_notification_system.improved_solution.channels.EmailChannelHandler;
import org.lld.practice.design_notification_system.improved_solution.channels.NotificationChannelHandler;
import org.lld.practice.design_notification_system.improved_solution.channels.PushChannelHandler;
import org.lld.practice.design_notification_system.improved_solution.channels.SMSChannelHandler;
import org.lld.practice.design_notification_system.improved_solution.models.NotificationChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating notification channel handlers.
 * Singleton pattern ensures single instance of handlers.
 */
public class ChannelHandlerFactory {
    private static ChannelHandlerFactory instance;
    private final Map<NotificationChannel, NotificationChannelHandler> handlers;
    
    private ChannelHandlerFactory() {
        handlers = new HashMap<>();
        // Initialize default handlers
        handlers.put(NotificationChannel.EMAIL, new EmailChannelHandler());
        handlers.put(NotificationChannel.SMS, new SMSChannelHandler());
        handlers.put(NotificationChannel.PUSH, new PushChannelHandler());
    }
    
    public static synchronized ChannelHandlerFactory getInstance() {
        if (instance == null) {
            instance = new ChannelHandlerFactory();
        }
        return instance;
    }
    
    /**
     * Gets the handler for a specific channel.
     * 
     * @param channel The notification channel
     * @return The channel handler
     */
    public NotificationChannelHandler getHandler(NotificationChannel channel) {
        return handlers.get(channel);
    }
    
    /**
     * Registers a custom handler for a channel.
     * 
     * @param channel The notification channel
     * @param handler The handler to register
     */
    public void registerHandler(NotificationChannel channel, NotificationChannelHandler handler) {
        handlers.put(channel, handler);
    }
}

