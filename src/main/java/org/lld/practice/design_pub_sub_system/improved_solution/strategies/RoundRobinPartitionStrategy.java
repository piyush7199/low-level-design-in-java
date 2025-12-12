package org.lld.practice.design_pub_sub_system.improved_solution.strategies;

import org.lld.practice.design_pub_sub_system.improved_solution.models.Message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round-robin partition strategy.
 * Distributes messages evenly across partitions regardless of key.
 */
public class RoundRobinPartitionStrategy implements PartitionStrategy {
    
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public int selectPartition(Message message, int numPartitions) {
        return counter.getAndIncrement() % numPartitions;
    }

    @Override
    public String getName() {
        return "Round-Robin";
    }
}

