package org.lld.practice.design_pub_sub_system.improved_solution;

import org.lld.practice.design_pub_sub_system.improved_solution.broker.Broker;
import org.lld.practice.design_pub_sub_system.improved_solution.consumer.Consumer;
import org.lld.practice.design_pub_sub_system.improved_solution.models.Message;
import org.lld.practice.design_pub_sub_system.improved_solution.producer.Producer;
import org.lld.practice.design_pub_sub_system.improved_solution.strategies.RoundRobinPartitionStrategy;

import java.util.List;

/**
 * Demo application for the Pub/Sub Messaging System (Mini-Kafka).
 * 
 * Demonstrates:
 * - Topic creation with partitions
 * - Key-based partitioning
 * - Consumer groups with rebalancing
 * - Offset management
 * - Message ordering within partitions
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     📡 PUB/SUB MESSAGING SYSTEM (MINI-KAFKA) - DEMO            ║");
        System.out.println("║     Features: Partitions, Consumer Groups, Offsets            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        Broker broker = new Broker();
        
        // Demo 1: Topic creation
        demoTopicCreation(broker);
        
        // Demo 2: Key-based partitioning
        demoKeyBasedPartitioning(broker);
        
        // Demo 3: Consumer groups
        demoConsumerGroups(broker);
        
        // Demo 4: Message ordering
        demoMessageOrdering(broker);
        
        // Demo 5: Offset management
        demoOffsetManagement(broker);
        
        // Summary
        broker.printStatus();
        printSummary();
    }
    
    private static void demoTopicCreation(Broker broker) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 1: Topic Creation");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Create topics with different partition counts
        broker.createTopic("orders", 3);
        broker.createTopic("user-events", 2);
        broker.createTopic("logs", 4);
        
        System.out.println();
    }
    
    private static void demoKeyBasedPartitioning(Broker broker) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 2: Key-Based Partitioning");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Producer producer = new Producer(broker);
        
        System.out.println("Publishing orders with keys (same key → same partition):\n");
        
        // Orders for user-123 should go to same partition
        producer.send("orders", "user-123", "Order-001: iPhone");
        producer.send("orders", "user-456", "Order-002: MacBook");
        producer.send("orders", "user-123", "Order-003: AirPods");  // Same partition as Order-001
        producer.send("orders", "user-789", "Order-004: iPad");
        producer.send("orders", "user-123", "Order-005: Watch");    // Same partition as Order-001
        
        System.out.println("\n✅ Notice: Messages with key 'user-123' go to the same partition");
        System.out.println("   This guarantees ordering for a specific user's orders!\n");
    }
    
    private static void demoConsumerGroups(Broker broker) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 3: Consumer Groups and Partition Assignment");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Create consumers in the same group
        System.out.println("Creating 2 consumers in group 'order-processors':\n");
        
        Consumer consumer1 = new Consumer("C1", "order-processors", broker);
        consumer1.subscribe("orders");
        System.out.printf("   %s assigned partitions: %s%n", 
                consumer1.getConsumerId(), consumer1.getAssignedPartitions());
        
        Consumer consumer2 = new Consumer("C2", "order-processors", broker);
        consumer2.subscribe("orders");
        System.out.printf("   %s assigned partitions: %s%n", 
                consumer2.getConsumerId(), consumer2.getAssignedPartitions());
        
        System.out.println("\n📊 Partition distribution:");
        System.out.println("   - 3 partitions split between 2 consumers");
        System.out.println("   - Each consumer processes only its assigned partitions");
        System.out.println("   - Load is balanced across consumers\n");
        
        // Demonstrate rebalance when consumer leaves
        System.out.println("Simulating Consumer C2 leaving the group...\n");
        consumer2.close();
        
        System.out.printf("   %s now assigned: %s (took over C2's partitions)%n%n", 
                consumer1.getConsumerId(), consumer1.getAssignedPartitions());
    }
    
    private static void demoMessageOrdering(Broker broker) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 4: Message Ordering Within Partitions");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Producer producer = new Producer(broker, new RoundRobinPartitionStrategy());
        
        // Publish to user-events with sequential messages
        System.out.println("Publishing sequential events:\n");
        
        producer.send("user-events", "event-1", "Login");
        producer.send("user-events", "event-1", "View Product");
        producer.send("user-events", "event-1", "Add to Cart");
        producer.send("user-events", "event-1", "Checkout");
        producer.send("user-events", "event-1", "Payment");
        
        // Create a consumer to read
        Consumer consumer = new Consumer("EventConsumer", "event-readers", broker);
        consumer.subscribe("user-events");
        
        System.out.println("\nConsuming messages (should be in order within each partition):\n");
        
        List<Message> messages = consumer.poll(10);
        for (Message msg : messages) {
            System.out.printf("   📥 P%d offset=%d: %s%n", 
                    msg.getPartition(), msg.getOffset(), msg.getValue());
        }
        
        System.out.println("\n✅ Messages within a partition maintain order\n");
    }
    
    private static void demoOffsetManagement(Broker broker) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 5: Offset Management (Resume from where you left off)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        Producer producer = new Producer(broker);
        
        // Publish some logs
        System.out.println("Publishing log messages:\n");
        for (int i = 1; i <= 6; i++) {
            producer.send("logs", "server-" + (i % 2 + 1), "Log entry " + i);
        }
        
        // Consumer reads and commits
        System.out.println("\n--- First Consumer Session ---");
        Consumer consumer1 = new Consumer("LogReader", "log-group", broker);
        consumer1.subscribe("logs");
        
        List<Message> batch1 = consumer1.poll(3);
        System.out.printf("Read %d messages:%n", batch1.size());
        for (Message msg : batch1) {
            System.out.printf("   📥 P%d offset=%d: %s%n", 
                    msg.getPartition(), msg.getOffset(), msg.getValue());
        }
        
        // Commit offsets
        consumer1.commit();
        System.out.println("✅ Offsets committed\n");
        
        // Simulate consumer restart
        System.out.println("--- Simulating Consumer Restart ---");
        consumer1.close();
        
        Consumer consumer2 = new Consumer("LogReader-New", "log-group", broker);
        consumer2.subscribe("logs");
        
        List<Message> batch2 = consumer2.poll(10);
        System.out.printf("New consumer reads %d messages (resuming from committed offset):%n", batch2.size());
        for (Message msg : batch2) {
            System.out.printf("   📥 P%d offset=%d: %s%n", 
                    msg.getPartition(), msg.getOffset(), msg.getValue());
        }
        
        System.out.println("\n✅ Consumer resumed from where the previous one left off!\n");
    }
    
    private static void printSummary() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY CONCEPTS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Topics and Partitions: Parallel processing units");
        System.out.println("✅ Key-based Partitioning: Same key → same partition → ordering");
        System.out.println("✅ Consumer Groups: Load balancing with automatic rebalancing");
        System.out.println("✅ Offset Management: Track position, resume on failure");
        System.out.println("✅ Message Ordering: Guaranteed within a partition");
        System.out.println();
        System.out.println("🎯 Interview Discussion Points:");
        System.out.println("   - Exactly-once semantics with idempotent producers");
        System.out.println("   - Leader-follower replication for fault tolerance");
        System.out.println("   - Log compaction for key-value topics");
        System.out.println("   - Consumer lag monitoring and backpressure");
        System.out.println("   - Distributed coordination with ZooKeeper/Raft");
    }
}

