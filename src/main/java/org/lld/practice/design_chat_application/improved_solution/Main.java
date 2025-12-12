package org.lld.practice.design_chat_application.improved_solution;

import org.lld.practice.design_chat_application.improved_solution.models.Chat;
import org.lld.practice.design_chat_application.improved_solution.models.Message;
import org.lld.practice.design_chat_application.improved_solution.models.MessageStatus;
import org.lld.practice.design_chat_application.improved_solution.models.MessageType;
import org.lld.practice.design_chat_application.improved_solution.models.PresenceStatus;
import org.lld.practice.design_chat_application.improved_solution.observers.NotificationObserver;
import org.lld.practice.design_chat_application.improved_solution.repositories.InMemoryMessageRepository;
import org.lld.practice.design_chat_application.improved_solution.repositories.MessageRepository;
import org.lld.practice.design_chat_application.improved_solution.services.ChatService;

import java.util.Arrays;
import java.util.List;

/**
 * Demo of improved chat application with real-time updates and group support.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Improved Chat Application Demo ===\n");
        
        MessageRepository repository = new InMemoryMessageRepository();
        ChatService chatService = new ChatService(repository);
        
        // Add notification observer
        chatService.addObserver(new NotificationObserver());
        
        System.out.println("1. Creating one-on-one chat:");
        Chat oneOnOneChat = chatService.createOneOnOneChat("alice", "bob");
        System.out.println("Chat created: " + oneOnOneChat.getChatId());
        
        System.out.println("\n2. Sending messages:");
        Message msg1 = chatService.sendMessage("alice", oneOnOneChat.getChatId(), 
                                               "Hello Bob!", MessageType.TEXT);
        System.out.println("Message sent: " + msg1.getContent() + " (Status: " + msg1.getStatus() + ")");
        
        Message msg2 = chatService.sendMessage("bob", oneOnOneChat.getChatId(), 
                                              "Hi Alice!", MessageType.TEXT);
        System.out.println("Message sent: " + msg2.getContent() + " (Status: " + msg2.getStatus() + ")");
        
        System.out.println("\n3. Marking message as read:");
        chatService.markMessageAsRead(msg1.getMessageId(), "bob");
        System.out.println("Message status updated to: " + MessageStatus.READ);
        
        System.out.println("\n4. Creating group chat:");
        Chat groupChat = chatService.createGroupChat("alice", 
                                                     Arrays.asList("bob", "charlie", "diana"));
        System.out.println("Group chat created: " + groupChat.getChatId());
        System.out.println("Participants: " + groupChat.getParticipants());
        
        System.out.println("\n5. Sending message to group:");
        Message groupMsg = chatService.sendMessage("alice", groupChat.getChatId(), 
                                                   "Hello everyone!", MessageType.TEXT);
        System.out.println("Group message sent: " + groupMsg.getContent());
        
        System.out.println("\n6. Updating user presence:");
        chatService.updatePresence("alice", PresenceStatus.ONLINE);
        chatService.updatePresence("bob", PresenceStatus.ONLINE);
        System.out.println("Presence updated");
        
        System.out.println("\n7. Getting chat history:");
        List<Message> history = chatService.getChatHistory(oneOnOneChat.getChatId());
        System.out.println("Chat history has " + history.size() + " messages");
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Real-time message delivery (Observer pattern)");
        System.out.println("✓ Support for one-on-one and group chats");
        System.out.println("✓ Message status tracking (sent/delivered/read)");
        System.out.println("✓ User presence management");
        System.out.println("✓ Typing indicators support");
        System.out.println("✓ Message persistence (Repository pattern)");
        System.out.println("✓ Easy to extend with new features");
    }
}

