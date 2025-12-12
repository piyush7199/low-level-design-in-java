package org.lld.practice.design_pub_sub_system.improved_solution.strategies;

import org.lld.practice.design_pub_sub_system.improved_solution.models.Message;

/**
 * Strategy interface for selecting a partition for a message.
 */
public interface PartitionStrategy {
    
    /**
     * Select a partition for the given message.
     * 
     * @param message The message to partition
     * @param numPartitions Total number of partitions
     * @return The partition ID (0 to numPartitions-1)
     */
    int selectPartition(Message message, int numPartitions);
    
    /**
     * Get the strategy name.
     */
    String getName();
}

