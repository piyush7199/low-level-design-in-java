package org.lld.practice.design_pub_sub_system.improved_solution.models;

import org.lld.practice.design_pub_sub_system.improved_solution.consumer.Consumer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a consumer group.
 * Consumers in the same group share the work of consuming from a topic's partitions.
 */
public class ConsumerGroup {
    
    private final String groupId;
    private final String topicName;
    private final Map<String, Consumer> consumers;                    // consumerId -> Consumer
    private final Map<String, Set<Integer>> consumerPartitions;       // consumerId -> assigned partitions
    private final Map<Integer, Long> committedOffsets;                // partitionId -> committed offset

    public ConsumerGroup(String groupId, String topicName) {
        this.groupId = Objects.requireNonNull(groupId);
        this.topicName = Objects.requireNonNull(topicName);
        this.consumers = new ConcurrentHashMap<>();
        this.consumerPartitions = new ConcurrentHashMap<>();
        this.committedOffsets = new ConcurrentHashMap<>();
    }

    /**
     * Add a consumer to the group and trigger rebalance.
     */
    public synchronized void addConsumer(Consumer consumer, int totalPartitions) {
        consumers.put(consumer.getConsumerId(), consumer);
        rebalance(totalPartitions);
        System.out.printf("👥 Consumer %s joined group '%s'%n", 
                consumer.getConsumerId(), groupId);
    }

    /**
     * Remove a consumer from the group and trigger rebalance.
     */
    public synchronized void removeConsumer(String consumerId, int totalPartitions) {
        consumers.remove(consumerId);
        consumerPartitions.remove(consumerId);
        if (!consumers.isEmpty()) {
            rebalance(totalPartitions);
        }
        System.out.printf("👥 Consumer %s left group '%s'%n", consumerId, groupId);
    }

    /**
     * Rebalance partitions among consumers.
     * Uses round-robin assignment strategy.
     */
    private void rebalance(int totalPartitions) {
        // Clear current assignments
        consumerPartitions.clear();
        
        if (consumers.isEmpty()) return;
        
        List<String> consumerIds = new ArrayList<>(consumers.keySet());
        
        // Round-robin assignment
        for (int partition = 0; partition < totalPartitions; partition++) {
            String consumerId = consumerIds.get(partition % consumerIds.size());
            consumerPartitions.computeIfAbsent(consumerId, k -> new HashSet<>())
                    .add(partition);
        }
        
        // Update consumers with their assignments
        for (Map.Entry<String, Consumer> entry : consumers.entrySet()) {
            Set<Integer> partitions = consumerPartitions.getOrDefault(
                    entry.getKey(), Collections.emptySet());
            entry.getValue().updateAssignment(partitions);
        }
        
        System.out.printf("🔄 Rebalanced group '%s': %s%n", groupId, consumerPartitions);
    }

    /**
     * Get partitions assigned to a consumer.
     */
    public Set<Integer> getAssignedPartitions(String consumerId) {
        return consumerPartitions.getOrDefault(consumerId, Collections.emptySet());
    }

    /**
     * Commit offset for a partition.
     */
    public void commitOffset(int partitionId, long offset) {
        committedOffsets.put(partitionId, offset);
    }

    /**
     * Get committed offset for a partition.
     */
    public long getCommittedOffset(int partitionId) {
        return committedOffsets.getOrDefault(partitionId, 0L);
    }

    /**
     * Get all committed offsets.
     */
    public Map<Integer, Long> getCommittedOffsets() {
        return Collections.unmodifiableMap(committedOffsets);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getConsumerCount() {
        return consumers.size();
    }

    public Collection<Consumer> getConsumers() {
        return Collections.unmodifiableCollection(consumers.values());
    }

    @Override
    public String toString() {
        return String.format("ConsumerGroup{id='%s', topic='%s', consumers=%d}",
                groupId, topicName, consumers.size());
    }
}

