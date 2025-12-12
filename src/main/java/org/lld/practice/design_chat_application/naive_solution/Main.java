package org.lld.practice.design_chat_application.naive_solution;

/**
 * Demo of naive chat application implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Chat Application Demo ===\n");
        
        SimpleChatService chatService = new SimpleChatService();
        
        chatService.sendMessage("alice", "bob", "Hello Bob!");
        chatService.sendMessage("bob", "alice", "Hi Alice!");
        chatService.sendMessage("alice", "bob", "How are you?");
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- No real-time message delivery");
        System.out.println("- No group chat support");
        System.out.println("- No message status (sent/delivered/read)");
        System.out.println("- No persistence (messages lost on restart)");
        System.out.println("- No user presence (online/offline)");
        System.out.println("- No typing indicators");
    }
}

