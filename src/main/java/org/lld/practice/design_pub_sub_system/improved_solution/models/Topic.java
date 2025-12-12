package org.lld.practice.design_pub_sub_system.improved_solution.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a topic containing multiple partitions.
 */
public class Topic {
    
    private final String name;
    private final List<Partition> partitions;
    private final Instant createdAt;

    public Topic(String name, int numPartitions) {
        if (numPartitions < 1) {
            throw new IllegalArgumentException("Topic must have at least 1 partition");
        }
        
        this.name = Objects.requireNonNull(name, "Topic name cannot be null");
        this.partitions = new ArrayList<>();
        this.createdAt = Instant.now();
        
        for (int i = 0; i < numPartitions; i++) {
            partitions.add(new Partition(i, name));
        }
    }

    /**
     * Get a partition by ID.
     */
    public Partition getPartition(int partitionId) {
        if (partitionId < 0 || partitionId >= partitions.size()) {
            throw new IllegalArgumentException("Invalid partition ID: " + partitionId);
        }
        return partitions.get(partitionId);
    }

    /**
     * Get all partitions.
     */
    public List<Partition> getPartitions() {
        return Collections.unmodifiableList(partitions);
    }

    /**
     * Get the number of partitions.
     */
    public int getNumPartitions() {
        return partitions.size();
    }

    /**
     * Get total message count across all partitions.
     */
    public int getTotalMessageCount() {
        return partitions.stream()
                .mapToInt(Partition::getMessageCount)
                .sum();
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("Topic{name='%s', partitions=%d, totalMessages=%d}",
                name, partitions.size(), getTotalMessageCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

