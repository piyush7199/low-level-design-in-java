package org.lld.practice.design_pub_sub_system.naive_solution;

import java.util.*;
import java.util.function.Consumer;

/**
 * Naive implementation of a Pub/Sub system.
 * 
 * This demonstrates common anti-patterns:
 * - No partitioning (single queue bottleneck)
 * - Broadcast to all consumers (no load balancing)
 * - No offset management (can't replay/resume)
 * - Synchronous delivery (blocks publisher)
 * - No message persistence
 * 
 * DO NOT use this pattern in production!
 */
public class SimplePubSub {
    
    private final Map<String, List<Consumer<String>>> subscribers = new HashMap<>();
    private final Map<String, Queue<String>> messageQueues = new HashMap<>();
    
    /**
     * Publish a message to a topic.
     * 
     * Problems:
     * - Synchronous delivery blocks the publisher
     * - All subscribers get all messages (no load balancing)
     * - No partitioning for parallelism
     * - No offset tracking
     */
    public void publish(String topic, String message) {
        // Store message (no persistence, memory only)
        messageQueues.computeIfAbsent(topic, k -> new LinkedList<>()).add(message);
        
        // Broadcast to ALL subscribers - inefficient!
        List<Consumer<String>> topicSubscribers = subscribers.getOrDefault(topic, List.of());
        for (Consumer<String> subscriber : topicSubscribers) {
            try {
                subscriber.accept(message);  // Synchronous - blocks!
            } catch (Exception e) {
                System.err.println("Subscriber failed: " + e.getMessage());
                // No retry, no dead letter queue
            }
        }
        
        System.out.printf("📤 Published to '%s': %s (sent to %d subscribers)%n", 
                topic, message, topicSubscribers.size());
    }
    
    /**
     * Subscribe to a topic.
     * 
     * Problems:
     * - No consumer groups
     * - Cannot resume from where we left off
     * - All subscribers get all messages
     */
    public void subscribe(String topic, Consumer<String> subscriber) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(subscriber);
        System.out.printf("✅ Subscribed to topic '%s'%n", topic);
    }
    
    /**
     * Get message count for a topic.
     */
    public int getMessageCount(String topic) {
        return messageQueues.getOrDefault(topic, new LinkedList<>()).size();
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) {
        System.out.println("=== Naive Pub/Sub Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        SimplePubSub pubsub = new SimplePubSub();
        
        // Create subscribers
        Consumer<String> subscriber1 = msg -> 
            System.out.println("   📥 Subscriber1 received: " + msg);
        Consumer<String> subscriber2 = msg -> 
            System.out.println("   📥 Subscriber2 received: " + msg);
        Consumer<String> subscriber3 = msg -> 
            System.out.println("   📥 Subscriber3 received: " + msg);
        
        // Subscribe to topic
        pubsub.subscribe("orders", subscriber1);
        pubsub.subscribe("orders", subscriber2);
        pubsub.subscribe("orders", subscriber3);
        
        System.out.println();
        
        // Publish messages
        pubsub.publish("orders", "Order-001: iPhone");
        System.out.println();
        pubsub.publish("orders", "Order-002: MacBook");
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. ALL 3 subscribers received BOTH messages");
        System.out.println("   → In Kafka, consumer group would split the load");
        System.out.println("2. No partitioning - single queue = bottleneck");
        System.out.println("3. No offset management - can't resume on failure");
        System.out.println("4. Synchronous delivery - slow subscriber blocks publisher");
        System.out.println("5. Memory only - messages lost on restart");
        System.out.println("6. No message ordering guarantees");
        
        System.out.println("\n📊 What Kafka does differently:");
        System.out.println("   - Partitions for parallelism");
        System.out.println("   - Consumer groups for load balancing");
        System.out.println("   - Offset tracking for replay/resume");
        System.out.println("   - Persistent storage");
        System.out.println("   - Async delivery with batching");
    }
}

