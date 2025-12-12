# Design Pub/Sub Messaging System (Mini-Kafka)

## 1. Problem Statement and Requirements

Design a Publish-Subscribe messaging system similar to Apache Kafka. The system should allow producers to publish messages to topics and consumers to subscribe and consume messages reliably.

### Functional Requirements:

- **Topics**: Create and manage topics for message categorization
- **Partitions**: Divide topics into partitions for parallelism
- **Producers**: Publish messages to topics with optional partition key
- **Consumers**: Subscribe to topics and consume messages
- **Consumer Groups**: Group consumers for load balancing and fault tolerance
- **Offset Management**: Track consumed message positions per consumer
- **Message Ordering**: Guarantee ordering within a partition
- **Message Retention**: Retain messages for a configurable period

### Non-Functional Requirements:

- **High Throughput**: Handle thousands of messages per second
- **Durability**: Messages persist until retention period expires
- **Scalability**: Add partitions for horizontal scaling
- **Fault Tolerance**: Consumer failures handled gracefully
- **At-least-once Delivery**: Ensure messages are not lost

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might use a simple queue and broadcast to all subscribers:

```java
class SimplePubSub {
    private Map<String, List<Consumer>> subscribers = new HashMap<>();
    private Map<String, Queue<String>> queues = new HashMap<>();
    
    public void publish(String topic, String message) {
        queues.computeIfAbsent(topic, k -> new LinkedList<>()).add(message);
        // Broadcast to all subscribers
        for (Consumer consumer : subscribers.getOrDefault(topic, List.of())) {
            consumer.receive(message);
        }
    }
    
    public void subscribe(String topic, Consumer consumer) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(consumer);
    }
}
```

### Limitations and Design Flaws:

1. **No Partitioning**:
   - Single queue per topic = bottleneck
   - No parallelism for consumers
   - Cannot scale horizontally

2. **No Consumer Groups**:
   - All consumers get all messages (broadcast)
   - No load balancing
   - Duplicate processing

3. **No Offset Management**:
   - Cannot replay messages
   - Cannot resume from failure point
   - Messages lost on consumer failure

4. **No Message Ordering Guarantees**:
   - Concurrent publishing can scramble order
   - No partition-level ordering

5. **Memory-Only Storage**:
   - Messages lost on system restart
   - No persistence or durability

6. **Tight Coupling**:
   - Synchronous delivery blocks publisher
   - Consumer failures affect publishing

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Observer** | Pub/Sub notifications | Decouple producers from consumers |
| **Strategy** | Partition assignment | Different partitioning strategies |
| **Factory** | Producer/Consumer creation | Encapsulate configuration |
| **Singleton** | Broker | Single coordination point |

### Kafka-like Architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                      MINI-KAFKA ARCHITECTURE                │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────┐       ┌─────────────────────────────────┐    │
│  │ Producer │──────>│            BROKER               │    │
│  └──────────┘       │  ┌─────────────────────────┐    │    │
│  ┌──────────┐       │  │      Topic: orders      │    │    │
│  │ Producer │──────>│  │  ┌─────┬─────┬─────┐   │    │    │
│  └──────────┘       │  │  │ P0  │ P1  │ P2  │   │    │    │
│                     │  │  │ msg │ msg │ msg │   │    │    │
│                     │  │  │ msg │ msg │ msg │   │    │    │
│                     │  │  │ msg │     │ msg │   │    │    │
│                     │  │  └─────┴─────┴─────┘   │    │    │
│                     │  └─────────────────────────┘    │    │
│                     └───────────────┬─────────────────┘    │
│                                     │                       │
│                     ┌───────────────┴───────────────┐      │
│                     ▼               ▼               ▼      │
│               ┌──────────┐   ┌──────────┐   ┌──────────┐  │
│               │Consumer 1│   │Consumer 2│   │Consumer 3│  │
│               │ (P0, P1) │   │   (P2)   │   │  (idle)  │  │
│               └──────────┘   └──────────┘   └──────────┘  │
│               └────────── Consumer Group ──────────┘       │
└─────────────────────────────────────────────────────────────┘
```

### Partitioning Strategy:

```
┌─────────────────────────────────────────────────────────────┐
│                    PARTITIONING                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Message with key:                                          │
│  partition = hash(key) % numPartitions                      │
│                                                             │
│  Example (3 partitions):                                    │
│  ┌────────────────────────────────────────────────────┐    │
│  │ Key: "user-123"  → hash → 12345 % 3 = 0 → P0      │    │
│  │ Key: "user-456"  → hash → 67890 % 3 = 0 → P0      │    │
│  │ Key: "order-789" → hash → 11111 % 3 = 2 → P2      │    │
│  └────────────────────────────────────────────────────┘    │
│                                                             │
│  ✓ Same key always goes to same partition                  │
│  ✓ Guarantees ordering for same key                        │
│  ✓ Enables parallel processing across partitions           │
└─────────────────────────────────────────────────────────────┘
```

### Consumer Group Rebalancing:

```
┌─────────────────────────────────────────────────────────────┐
│                CONSUMER GROUP REBALANCING                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Before (2 consumers, 4 partitions):                        │
│  ┌─────────┐  ┌─────────┐                                  │
│  │ C1: P0,P1│  │ C2: P2,P3│                                │
│  └─────────┘  └─────────┘                                  │
│                                                             │
│  Consumer C2 fails... Rebalance!                           │
│                                                             │
│  After (1 consumer, 4 partitions):                          │
│  ┌─────────────────────┐                                   │
│  │ C1: P0, P1, P2, P3  │  ← C1 takes over all             │
│  └─────────────────────┘                                   │
│                                                             │
│  New consumer C3 joins... Rebalance!                       │
│                                                             │
│  After (2 consumers, 4 partitions):                         │
│  ┌─────────┐  ┌─────────┐                                  │
│  │ C1: P0,P1│  │ C3: P2,P3│                                │
│  └─────────┘  └─────────┘                                  │
└─────────────────────────────────────────────────────────────┘
```

### Core Classes:

#### 1. Models (`models/`)
- `Message` - Message with key, value, timestamp, offset
- `Topic` - Topic containing partitions
- `Partition` - Ordered log of messages
- `ConsumerGroup` - Group of consumers with partition assignment
- `Offset` - Consumer's position in a partition

#### 2. Core Components
- `Broker` - Central coordinator (Singleton)
- `Producer` - Publishes messages
- `Consumer` - Subscribes and consumes messages

#### 3. Strategies (`strategies/`)
- `PartitionStrategy` - Interface for partitioning
- `KeyHashPartitionStrategy` - Partition by key hash
- `RoundRobinPartitionStrategy` - Round-robin distribution

### Class Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                         Broker                              │
│                       (Singleton)                           │
├─────────────────────────────────────────────────────────────┤
│ - topics: Map<String, Topic>                                │
│ - consumerGroups: Map<String, ConsumerGroup>               │
├─────────────────────────────────────────────────────────────┤
│ + createTopic(name, partitions): Topic                     │
│ + publish(topic, message): void                            │
│ + subscribe(group, topic, consumer): void                  │
│ + poll(consumer, timeout): List<Message>                   │
│ + commit(consumer, offsets): void                          │
└─────────────────────────────────────────────────────────────┘
                              │
                uses          │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│     Topic       │  │   Producer      │  │   Consumer      │
├─────────────────┤  ├─────────────────┤  ├─────────────────┤
│ - name: String  │  │ - broker        │  │ - consumerId    │
│ - partitions[]  │  │ - strategy      │  │ - groupId       │
│ - numPartitions │  ├─────────────────┤  │ - offsets       │
├─────────────────┤  │ + send(topic,   │  ├─────────────────┤
│ + getPartition()│  │   key, value)   │  │ + subscribe()   │
│ + publish()     │  └─────────────────┘  │ + poll()        │
└─────────────────┘                        │ + commit()      │
          │                                └─────────────────┘
          │ contains
          ▼
┌─────────────────────────────────────────────────────────────┐
│                       Partition                             │
├─────────────────────────────────────────────────────────────┤
│ - partitionId: int                                          │
│ - messages: List<Message>                                   │
│ - currentOffset: long                                       │
├─────────────────────────────────────────────────────────────┤
│ + append(message): long    // Returns offset               │
│ + read(offset, limit): List<Message>                       │
│ + getLatestOffset(): long                                  │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Final Design Overview

### Message Flow:

```
┌──────────┐    ┌──────────┐    ┌─────────────┐    ┌──────────┐
│ Producer │───>│  Broker  │───>│  Partition  │───>│ Consumer │
│          │    │          │    │  (append)   │    │  (poll)  │
└──────────┘    └──────────┘    └─────────────┘    └──────────┘
     │                                                   │
     │           ┌───────────────────────────────┐      │
     │           │     Partition Selection       │      │
     └──────────>│  if key: hash(key) % N        │      │
                 │  else: round-robin            │      │
                 └───────────────────────────────┘      │
                                                        │
                 ┌───────────────────────────────┐      │
                 │      Offset Tracking          │<─────┘
                 │  Consumer stores: {P0: 5}     │
                 │  "I've consumed up to msg 5"  │
                 └───────────────────────────────┘
```

### Offset Management:

```
┌─────────────────────────────────────────────────────────────┐
│                    OFFSET MANAGEMENT                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Partition P0:                                              │
│  ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐                │
│  │ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │ 8 │ 9 │  ← offsets    │
│  └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘                │
│                    ▲               ▲                        │
│                    │               │                        │
│           Consumer Offset    Latest Offset                  │
│           (committed: 4)     (produced: 9)                  │
│                                                             │
│  LAG = Latest - Committed = 9 - 4 = 5 messages behind      │
│                                                             │
│  On restart, consumer resumes from committed offset (4)    │
└─────────────────────────────────────────────────────────────┘
```

### Interview Discussion Points:

1. **How to handle consumer failures?**
   - Heartbeat mechanism to detect failures
   - Automatic partition reassignment
   - Uncommitted messages re-delivered to other consumers

2. **How to ensure exactly-once semantics?**
   - Idempotent producers (deduplication)
   - Transactional consumers (atomic commit)
   - Consumer-side deduplication with message IDs

3. **How to scale beyond single broker?**
   - Partition distribution across brokers
   - Leader-follower replication
   - ZooKeeper/Raft for coordination

4. **How to handle slow consumers?**
   - Backpressure mechanisms
   - Consumer lag monitoring
   - Dead letter queues for poison messages

5. **Message retention vs. compaction?**
   - Time-based retention (delete after N days)
   - Log compaction (keep latest per key)

