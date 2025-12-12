package org.lld.practice.design_pub_sub_system.improved_solution.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a partition within a topic.
 * A partition is an ordered, immutable sequence of messages (log).
 */
public class Partition {
    
    private final int partitionId;
    private final String topicName;
    private final List<Message> messages;
    private final ReadWriteLock lock;
    
    private long nextOffset;

    public Partition(int partitionId, String topicName) {
        this.partitionId = partitionId;
        this.topicName = topicName;
        this.messages = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
        this.nextOffset = 0;
    }

    /**
     * Append a message to the partition.
     * @return The offset assigned to the message
     */
    public long append(Message message) {
        lock.writeLock().lock();
        try {
            long offset = nextOffset++;
            message.setPartition(partitionId);
            message.setOffset(offset);
            messages.add(message);
            return offset;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Read messages starting from the given offset.
     * @param fromOffset Starting offset (inclusive)
     * @param limit Maximum number of messages to return
     * @return List of messages
     */
    public List<Message> read(long fromOffset, int limit) {
        lock.readLock().lock();
        try {
            if (fromOffset < 0 || fromOffset >= messages.size()) {
                return Collections.emptyList();
            }
            
            int endIndex = (int) Math.min(fromOffset + limit, messages.size());
            return new ArrayList<>(messages.subList((int) fromOffset, endIndex));
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Get the next offset that will be assigned.
     */
    public long getLatestOffset() {
        lock.readLock().lock();
        try {
            return nextOffset;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Get the earliest available offset.
     */
    public long getEarliestOffset() {
        return 0;  // We keep all messages in this simple implementation
    }

    /**
     * Get the message count.
     */
    public int getMessageCount() {
        lock.readLock().lock();
        try {
            return messages.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getPartitionId() {
        return partitionId;
    }

    public String getTopicName() {
        return topicName;
    }

    @Override
    public String toString() {
        return String.format("Partition{topic='%s', id=%d, messages=%d, latestOffset=%d}",
                topicName, partitionId, getMessageCount(), getLatestOffset());
    }
}

