package org.lld.practice.design_pub_sub_system.improved_solution.producer;

import org.lld.practice.design_pub_sub_system.improved_solution.broker.Broker;
import org.lld.practice.design_pub_sub_system.improved_solution.models.Message;
import org.lld.practice.design_pub_sub_system.improved_solution.strategies.KeyHashPartitionStrategy;
import org.lld.practice.design_pub_sub_system.improved_solution.strategies.PartitionStrategy;

import java.util.Objects;
import java.util.UUID;

/**
 * Producer for publishing messages to topics.
 */
public class Producer {
    
    private final String producerId;
    private final Broker broker;
    private final PartitionStrategy partitionStrategy;

    public Producer(Broker broker) {
        this(broker, new KeyHashPartitionStrategy());
    }

    public Producer(Broker broker, PartitionStrategy partitionStrategy) {
        this.producerId = "P-" + UUID.randomUUID().toString().substring(0, 4);
        this.broker = Objects.requireNonNull(broker);
        this.partitionStrategy = Objects.requireNonNull(partitionStrategy);
    }

    /**
     * Send a message without a key.
     */
    public void send(String topic, String value) {
        send(topic, null, value);
    }

    /**
     * Send a message with a key.
     */
    public void send(String topic, String key, String value) {
        Message message = Message.of(key, value, topic);
        broker.publish(message, partitionStrategy);
    }

    /**
     * Send a pre-constructed message.
     */
    public void send(Message message) {
        broker.publish(message, partitionStrategy);
    }

    public String getProducerId() {
        return producerId;
    }

    @Override
    public String toString() {
        return String.format("Producer{id='%s', strategy='%s'}",
                producerId, partitionStrategy.getName());
    }
}

