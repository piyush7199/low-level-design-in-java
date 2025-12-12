package org.lld.practice.design_pub_sub_system.improved_solution.strategies;

import org.lld.practice.design_pub_sub_system.improved_solution.models.Message;

/**
 * Partition strategy based on key hash.
 * Messages with the same key always go to the same partition.
 * Falls back to round-robin for messages without keys.
 */
public class KeyHashPartitionStrategy implements PartitionStrategy {
    
    private int roundRobinCounter = 0;

    @Override
    public int selectPartition(Message message, int numPartitions) {
        if (message.hasKey()) {
            // Hash the key to get a consistent partition
            int hash = Math.abs(message.getKey().hashCode());
            return hash % numPartitions;
        } else {
            // Round-robin for keyless messages
            return roundRobinCounter++ % numPartitions;
        }
    }

    @Override
    public String getName() {
        return "Key-Hash";
    }
}

