package org.lld.practice.design_pub_sub_system.improved_solution.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a message in the pub/sub system.
 */
public class Message {
    
    private final String messageId;
    private final String key;          // Partition key (optional)
    private final String value;        // Message payload
    private final Instant timestamp;
    private final String topic;
    
    private int partition;             // Assigned partition
    private long offset;               // Position in partition

    public Message(String key, String value, String topic) {
        this.messageId = UUID.randomUUID().toString().substring(0, 8);
        this.key = key;
        this.value = Objects.requireNonNull(value, "Message value cannot be null");
        this.topic = Objects.requireNonNull(topic, "Topic cannot be null");
        this.timestamp = Instant.now();
        this.partition = -1;  // Not yet assigned
        this.offset = -1;     // Not yet assigned
    }

    /**
     * Create a message without a key (round-robin partitioning).
     */
    public static Message of(String value, String topic) {
        return new Message(null, value, topic);
    }

    /**
     * Create a message with a key (key-based partitioning).
     */
    public static Message of(String key, String value, String topic) {
        return new Message(key, value, topic);
    }

    // ========== Internal Setters ==========

    void setPartition(int partition) {
        this.partition = partition;
    }

    void setOffset(long offset) {
        this.offset = offset;
    }

    // ========== Getters ==========

    public String getMessageId() {
        return messageId;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getTopic() {
        return topic;
    }

    public int getPartition() {
        return partition;
    }

    public long getOffset() {
        return offset;
    }

    public boolean hasKey() {
        return key != null && !key.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Message{id='%s', key='%s', value='%s', partition=%d, offset=%d}",
                messageId, key != null ? key : "null", 
                value.length() > 30 ? value.substring(0, 30) + "..." : value,
                partition, offset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}

