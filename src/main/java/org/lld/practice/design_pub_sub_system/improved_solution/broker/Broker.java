package org.lld.practice.design_pub_sub_system.improved_solution.broker;

import org.lld.practice.design_pub_sub_system.improved_solution.consumer.Consumer;
import org.lld.practice.design_pub_sub_system.improved_solution.models.*;
import org.lld.practice.design_pub_sub_system.improved_solution.strategies.PartitionStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central message broker (like Kafka broker).
 * Manages topics, partitions, and consumer groups.
 */
public class Broker {
    
    private final Map<String, Topic> topics;
    private final Map<String, ConsumerGroup> consumerGroups;  // groupId -> ConsumerGroup

    public Broker() {
        this.topics = new ConcurrentHashMap<>();
        this.consumerGroups = new ConcurrentHashMap<>();
    }

    // ========== Topic Management ==========

    /**
     * Create a new topic.
     */
    public Topic createTopic(String name, int numPartitions) {
        if (topics.containsKey(name)) {
            throw new IllegalArgumentException("Topic already exists: " + name);
        }
        
        Topic topic = new Topic(name, numPartitions);
        topics.put(name, topic);
        System.out.printf("📁 Created topic '%s' with %d partitions%n", name, numPartitions);
        return topic;
    }

    /**
     * Get a topic by name.
     */
    public Optional<Topic> getTopic(String name) {
        return Optional.ofNullable(topics.get(name));
    }

    /**
     * Get topic or throw exception.
     */
    public Topic getTopicOrThrow(String name) {
        return getTopic(name)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found: " + name));
    }

    /**
     * List all topics.
     */
    public Collection<Topic> listTopics() {
        return Collections.unmodifiableCollection(topics.values());
    }

    // ========== Publishing ==========

    /**
     * Publish a message to a topic.
     */
    public void publish(Message message, PartitionStrategy strategy) {
        Topic topic = getTopicOrThrow(message.getTopic());
        
        // Select partition
        int partitionId = strategy.selectPartition(message, topic.getNumPartitions());
        Partition partition = topic.getPartition(partitionId);
        
        // Append message to partition
        long offset = partition.append(message);
        
        System.out.printf("📤 Published to %s[P%d] offset=%d: key='%s' value='%s'%n",
                message.getTopic(), partitionId, offset,
                message.getKey() != null ? message.getKey() : "null",
                truncate(message.getValue(), 30));
    }

    // ========== Consumer Management ==========

    /**
     * Subscribe a consumer to a topic.
     */
    public void subscribe(Consumer consumer, String topicName, String groupId) {
        Topic topic = getTopicOrThrow(topicName);
        
        String groupKey = groupId + ":" + topicName;
        ConsumerGroup group = consumerGroups.computeIfAbsent(groupKey,
                k -> new ConsumerGroup(groupId, topicName));
        
        group.addConsumer(consumer, topic.getNumPartitions());
    }

    /**
     * Unsubscribe a consumer.
     */
    public void unsubscribe(Consumer consumer, String groupId) {
        // Find all groups this consumer belongs to
        for (ConsumerGroup group : consumerGroups.values()) {
            if (group.getGroupId().equals(groupId)) {
                Topic topic = getTopicOrThrow(group.getTopicName());
                group.removeConsumer(consumer.getConsumerId(), topic.getNumPartitions());
            }
        }
    }

    /**
     * Poll messages for a consumer from a specific partition.
     */
    public List<Message> poll(Consumer consumer, int partitionId, long fromOffset, int limit) {
        // Find the topic for this consumer's group
        String groupKey = consumer.getGroupId() + ":" + findTopicForConsumer(consumer);
        ConsumerGroup group = consumerGroups.get(groupKey);
        
        if (group == null) {
            return Collections.emptyList();
        }
        
        Topic topic = getTopicOrThrow(group.getTopicName());
        Partition partition = topic.getPartition(partitionId);
        
        return partition.read(fromOffset, limit);
    }

    /**
     * Find topic for a consumer (simplified - assumes one topic per group).
     */
    private String findTopicForConsumer(Consumer consumer) {
        for (ConsumerGroup group : consumerGroups.values()) {
            if (group.getGroupId().equals(consumer.getGroupId())) {
                return group.getTopicName();
            }
        }
        return null;
    }

    // ========== Offset Management ==========

    /**
     * Commit offset for a consumer group.
     */
    public void commitOffset(String groupId, int partitionId, long offset) {
        for (ConsumerGroup group : consumerGroups.values()) {
            if (group.getGroupId().equals(groupId)) {
                group.commitOffset(partitionId, offset);
            }
        }
    }

    /**
     * Get committed offset for a consumer group.
     */
    public long getCommittedOffset(String groupId, int partitionId) {
        for (ConsumerGroup group : consumerGroups.values()) {
            if (group.getGroupId().equals(groupId)) {
                return group.getCommittedOffset(partitionId);
            }
        }
        return 0L;
    }

    // ========== Statistics ==========

    /**
     * Get consumer group info.
     */
    public Optional<ConsumerGroup> getConsumerGroup(String groupId, String topicName) {
        String groupKey = groupId + ":" + topicName;
        return Optional.ofNullable(consumerGroups.get(groupKey));
    }

    /**
     * Print broker status.
     */
    public void printStatus() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║              📡 BROKER STATUS                       ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Topics: %-42d ║%n", topics.size());
        System.out.printf("║ Consumer Groups: %-33d ║%n", consumerGroups.size());
        System.out.println("╠════════════════════════════════════════════════════╣");
        
        for (Topic topic : topics.values()) {
            System.out.printf("║ Topic: %-42s ║%n", topic.getName());
            System.out.printf("║   Partitions: %-36d ║%n", topic.getNumPartitions());
            System.out.printf("║   Messages: %-38d ║%n", topic.getTotalMessageCount());
        }
        
        System.out.println("╚════════════════════════════════════════════════════╝\n");
    }

    private String truncate(String s, int maxLen) {
        return s.length() <= maxLen ? s : s.substring(0, maxLen) + "...";
    }
}

