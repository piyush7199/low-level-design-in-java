# Design Cache Management System with Finite Memory

## 1. Problem Statement and Requirements

Design a Cache Management System that efficiently stores key-value pairs in memory with finite capacity. The system should support multiple eviction policies (LRU, LFU, FIFO) and optional TTL (Time-To-Live) expiry for cached entries.

### Functional Requirements:

- **Basic Operations**: Support `get(key)`, `put(key, value)`, `remove(key)`, `clear()` operations
- **Finite Capacity**: Cache should have a configurable maximum capacity
- **Eviction Policies**: Support multiple eviction strategies:
  - **LRU (Least Recently Used)**: Evict the least recently accessed item
  - **LFU (Least Frequently Used)**: Evict the least frequently accessed item
  - **FIFO (First In First Out)**: Evict the oldest inserted item
- **TTL Expiry**: Optionally support time-based expiration for cache entries
- **Statistics**: Track cache hits, misses, evictions for monitoring
- **Thread Safety**: Support concurrent access from multiple threads

### Non-Functional Requirements:

- **O(1) Time Complexity**: Get and Put operations should be O(1) for LRU cache
- **Memory Efficiency**: Minimize memory overhead per cached entry
- **Extensibility**: Easy to add new eviction policies
- **Observability**: Provide metrics for cache performance monitoring

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might implement a simple cache using a HashMap with a size check:

```java
class SimpleCache<K, V> {
    private Map<K, V> cache = new HashMap<>();
    private int maxSize;
    
    public V get(K key) {
        return cache.get(key);
    }
    
    public void put(K key, V value) {
        if (cache.size() >= maxSize && !cache.containsKey(key)) {
            // Remove "some" entry - but which one?
            K firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }
        cache.put(key, value);
    }
}
```

### Limitations and Design Flaws:

1. **No Real Eviction Policy**:
   - HashMap doesn't maintain insertion order (pre-Java 8)
   - No tracking of access frequency or recency
   - Random eviction is inefficient

2. **Poor Time Complexity**:
   - Finding the least recently/frequently used item requires O(n) scan
   - No efficient way to track access patterns

3. **No TTL Support**:
   - Entries never expire
   - Stale data remains in cache indefinitely

4. **Violation of OCP**:
   - Adding a new eviction policy requires modifying the cache class
   - No separation between cache storage and eviction logic

5. **Not Thread-Safe**:
   - Concurrent access leads to race conditions
   - No synchronization mechanism

6. **No Observability**:
   - No tracking of cache hits/misses
   - Cannot monitor cache effectiveness

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Eviction Policies | Different algorithms (LRU, LFU, FIFO) for eviction |
| **Factory** | Cache Creation | Create different cache types based on configuration |
| **Decorator** | TTL Support | Add TTL functionality on top of base cache |
| **Template Method** | Base Cache | Common cache operations with customizable eviction |
| **Observer** | Statistics | Track and report cache metrics |

### Key Data Structures:

#### LRU Cache - O(1) Get/Put
```
┌─────────────────────────────────────────────────────┐
│                    HashMap                          │
│  key1 → Node1    key2 → Node2    key3 → Node3      │
└─────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│              Doubly Linked List                     │
│  HEAD ←→ [MRU] ←→ [Node2] ←→ [LRU] ←→ TAIL        │
│          Most                   Least               │
│          Recent                 Recent              │
└─────────────────────────────────────────────────────┘
```

#### LFU Cache - O(1) Get/Put
```
┌─────────────────────────────────────────────────────┐
│                    HashMap (key → Node)             │
└─────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────┐
│           Frequency Map (freq → LinkedHashSet)      │
│  1 → [key3, key5]                                   │
│  2 → [key1, key4]                                   │
│  5 → [key2]        ← minFrequency = 1               │
└─────────────────────────────────────────────────────┘
```

### Core Classes and Their Interactions:

#### 1. Models Layer (`models/`)
- `CacheEntry<K, V>` - Wrapper holding value, metadata (access time, frequency, TTL)
- `CacheConfig` - Configuration (capacity, TTL, eviction policy)
- `CacheStats` - Statistics (hits, misses, evictions)
- `EvictionPolicy` - Enum: LRU, LFU, FIFO

#### 2. Strategy Pattern (`strategies/`)
- `EvictionStrategy<K>` - Interface for eviction algorithms
- `LRUEvictionStrategy<K>` - Doubly linked list + HashMap
- `LFUEvictionStrategy<K>` - Frequency map approach
- `FIFOEvictionStrategy<K>` - Queue-based eviction

#### 3. Cache Implementations (`cache/`)
- `Cache<K, V>` - Interface defining cache operations
- `BaseCache<K, V>` - Abstract base with common logic
- `InMemoryCache<K, V>` - Main cache implementation
- `TTLCache<K, V>` - Decorator adding TTL support

#### 4. Factory Pattern (`factory/`)
- `CacheFactory` - Creates cache instances based on config

### Class Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                    <<interface>>                            │
│                       Cache<K,V>                            │
├─────────────────────────────────────────────────────────────┤
│ + get(key: K): Optional<V>                                  │
│ + put(key: K, value: V): void                              │
│ + remove(key: K): Optional<V>                              │
│ + clear(): void                                            │
│ + size(): int                                              │
│ + getStats(): CacheStats                                   │
└─────────────────────────────────────────────────────────────┘
                           △
                           │
          ┌────────────────┼────────────────┐
          │                │                │
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│  InMemoryCache  │ │    TTLCache     │ │ ThreadSafeCache │
│    <K, V>       │ │    <K, V>       │ │    <K, V>       │
├─────────────────┤ ├─────────────────┤ ├─────────────────┤
│ - storage: Map  │ │ - delegate:Cache│ │ - delegate:Cache│
│ - strategy:     │ │ - scheduler     │ │ - lock: RWLock  │
│   EvictionStrat │ └─────────────────┘ └─────────────────┘
└─────────────────┘
         │
         │ uses
         ▼
┌─────────────────────────────────────────────────────────────┐
│                <<interface>>                                │
│              EvictionStrategy<K>                            │
├─────────────────────────────────────────────────────────────┤
│ + recordAccess(key: K): void                               │
│ + recordInsertion(key: K): void                            │
│ + getEvictionCandidate(): Optional<K>                      │
│ + remove(key: K): void                                     │
└─────────────────────────────────────────────────────────────┘
                           △
          ┌────────────────┼────────────────┐
          │                │                │
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│LRUEviction   │  │LFUEviction   │  │FIFOEviction  │
│Strategy      │  │Strategy      │  │Strategy      │
└──────────────┘  └──────────────┘  └──────────────┘
```

---

## 4. Final Design Overview

### LRU Cache Implementation Details:

The LRU cache achieves O(1) time complexity using:
1. **HashMap** - For O(1) key lookup
2. **Doubly Linked List** - For O(1) removal and insertion at head/tail

When an item is accessed:
1. Look up in HashMap - O(1)
2. Move node to head of list (most recently used) - O(1)

When capacity is exceeded:
1. Remove tail node (least recently used) - O(1)
2. Remove from HashMap - O(1)

### LFU Cache Implementation Details:

The LFU cache achieves O(1) using:
1. **HashMap<Key, Node>** - For O(1) key lookup
2. **HashMap<Frequency, LinkedHashSet<Key>>** - Groups keys by access frequency
3. **minFrequency** - Tracks the minimum frequency for O(1) eviction

When an item is accessed:
1. Look up in key map - O(1)
2. Remove from current frequency set, add to frequency+1 set - O(1)
3. Update minFrequency if needed - O(1)

### TTL Expiry Implementation:

Two approaches:
1. **Lazy Expiry**: Check TTL on get(), remove if expired
2. **Active Expiry**: Background thread periodically cleans expired entries

We implement both for flexibility.

### Interview Discussion Points:

1. **Why Doubly Linked List for LRU?**
   - Need O(1) removal from middle of list
   - Singly linked list requires O(n) to find previous node

2. **LRU vs LFU Trade-offs?**
   - LRU: Better for temporal locality (recent = likely to be used again)
   - LFU: Better for frequency patterns (frequently used items stay cached)
   - LFU can suffer from "cache pollution" with old popular items

3. **How to handle TTL efficiently?**
   - Lazy: Simple but stale entries consume memory
   - Active: Background cleanup but adds complexity
   - Hybrid: Lazy check + periodic cleanup

4. **Thread Safety Options?**
   - Synchronized methods: Simple but poor concurrency
   - ReadWriteLock: Better read concurrency
   - ConcurrentHashMap + atomic operations: Best performance
   - Striped locks: Balance between concurrency and memory

5. **Memory Overhead?**
   - LRU: ~48 bytes per entry (node object + pointers)
   - LFU: ~64 bytes per entry (frequency tracking)
   - Consider memory vs performance trade-offs

