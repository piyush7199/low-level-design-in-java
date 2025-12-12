package org.lld.practice.design_pub_sub_system.improved_solution.consumer;

import org.lld.practice.design_pub_sub_system.improved_solution.broker.Broker;
import org.lld.practice.design_pub_sub_system.improved_solution.models.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Consumer for reading messages from topics.
 */
public class Consumer {
    
    private final String consumerId;
    private final String groupId;
    private final Broker broker;
    
    // Current partition assignments
    private volatile Set<Integer> assignedPartitions;
    
    // Local offset tracking (not yet committed)
    private final Map<Integer, Long> currentOffsets;

    public Consumer(String consumerId, String groupId, Broker broker) {
        this.consumerId = Objects.requireNonNull(consumerId);
        this.groupId = Objects.requireNonNull(groupId);
        this.broker = Objects.requireNonNull(broker);
        this.assignedPartitions = new HashSet<>();
        this.currentOffsets = new ConcurrentHashMap<>();
    }

    /**
     * Subscribe to a topic.
     */
    public void subscribe(String topic) {
        broker.subscribe(this, topic, groupId);
        initializeOffsets(topic);
    }

    /**
     * Initialize offsets from committed positions.
     */
    private void initializeOffsets(String topic) {
        for (int partition : assignedPartitions) {
            long committedOffset = broker.getCommittedOffset(groupId, partition);
            currentOffsets.put(partition, committedOffset);
        }
    }

    /**
     * Poll for new messages.
     * 
     * @param maxMessages Maximum messages to fetch
     * @return List of messages from assigned partitions
     */
    public List<Message> poll(int maxMessages) {
        List<Message> messages = new ArrayList<>();
        
        for (int partition : assignedPartitions) {
            long offset = currentOffsets.getOrDefault(partition, 0L);
            List<Message> partitionMessages = broker.poll(this, partition, offset, maxMessages);
            
            if (!partitionMessages.isEmpty()) {
                messages.addAll(partitionMessages);
                // Update local offset
                long newOffset = partitionMessages.get(partitionMessages.size() - 1).getOffset() + 1;
                currentOffsets.put(partition, newOffset);
            }
        }
        
        return messages;
    }

    /**
     * Commit current offsets (manual commit).
     */
    public void commit() {
        for (Map.Entry<Integer, Long> entry : currentOffsets.entrySet()) {
            broker.commitOffset(groupId, entry.getKey(), entry.getValue());
        }
    }

    /**
     * Update partition assignments (called during rebalance).
     */
    public void updateAssignment(Set<Integer> partitions) {
        this.assignedPartitions = new HashSet<>(partitions);
        
        // Initialize offsets for newly assigned partitions
        for (int partition : partitions) {
            if (!currentOffsets.containsKey(partition)) {
                long committedOffset = broker.getCommittedOffset(groupId, partition);
                currentOffsets.put(partition, committedOffset);
            }
        }
    }

    /**
     * Get current offset for a partition.
     */
    public long getCurrentOffset(int partition) {
        return currentOffsets.getOrDefault(partition, 0L);
    }

    /**
     * Get assigned partitions.
     */
    public Set<Integer> getAssignedPartitions() {
        return Collections.unmodifiableSet(assignedPartitions);
    }

    /**
     * Unsubscribe and leave the group.
     */
    public void close() {
        broker.unsubscribe(this, groupId);
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public String toString() {
        return String.format("Consumer{id='%s', group='%s', partitions=%s}",
                consumerId, groupId, assignedPartitions);
    }
}

