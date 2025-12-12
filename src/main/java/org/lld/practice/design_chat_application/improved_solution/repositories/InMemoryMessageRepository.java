package org.lld.practice.design_chat_application.improved_solution.repositories;

import org.lld.practice.design_chat_application.improved_solution.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of message repository.
 * In production, this would be replaced with a database implementation.
 */
public class InMemoryMessageRepository implements MessageRepository {
    private final Map<String, Message> messageStore = new ConcurrentHashMap<>();
    private final Map<String, List<Message>> chatMessages = new ConcurrentHashMap<>();
    
    @Override
    public void save(Message message) {
        messageStore.put(message.getMessageId(), message);
        chatMessages.computeIfAbsent(message.getChatId(), k -> new ArrayList<>()).add(message);
    }
    
    @Override
    public Optional<Message> findById(String messageId) {
        return Optional.ofNullable(messageStore.get(messageId));
    }
    
    @Override
    public List<Message> findByChatId(String chatId) {
        return new ArrayList<>(chatMessages.getOrDefault(chatId, new ArrayList<>()));
    }
    
    @Override
    public List<Message> findByChatIdAfter(String chatId, long afterTimestamp) {
        List<Message> messages = findByChatId(chatId);
        return messages.stream()
                .filter(msg -> msg.getTimestamp().toEpochSecond(java.time.ZoneOffset.UTC) > afterTimestamp)
                .toList();
    }
}

