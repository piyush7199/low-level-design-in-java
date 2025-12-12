# Design In-Memory Rate Limiter

## 1. Problem Statement and Requirements

Design an In-Memory Rate Limiter that controls the rate of requests a client can make to a system. Rate limiting is essential for protecting systems from abuse, ensuring fair usage, and maintaining service availability.

### Functional Requirements:

- **Rate Limiting**: Limit the number of requests a client can make within a time window
- **Multiple Algorithms**: Support different rate limiting strategies:
  - **Token Bucket**: Smooth rate limiting with burst capacity
  - **Sliding Window Log**: Precise rate limiting with request timestamps
  - **Sliding Window Counter**: Memory-efficient approximation
  - **Fixed Window Counter**: Simple time-window based limiting
  - **Leaky Bucket**: Smooth output rate regardless of input bursts
- **Client Identification**: Rate limit by client ID, IP address, or API key
- **Configurable Limits**: Different limits for different clients/tiers
- **Response Information**: Return remaining quota, reset time, retry-after

### Non-Functional Requirements:

- **Low Latency**: O(1) time complexity for rate limit checks
- **Thread Safety**: Handle concurrent requests from same client
- **Memory Efficiency**: Minimize memory footprint per client
- **Accuracy**: Minimize over-limiting or under-limiting
- **Extensibility**: Easy to add new rate limiting algorithms

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might implement a simple counter-based approach:

```java
class SimpleRateLimiter {
    private Map<String, Integer> requestCounts = new HashMap<>();
    private Map<String, Long> windowStart = new HashMap<>();
    private int maxRequests = 100;
    private long windowSizeMs = 60000; // 1 minute
    
    public boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        Long start = windowStart.get(clientId);
        
        if (start == null || now - start > windowSizeMs) {
            // New window
            windowStart.put(clientId, now);
            requestCounts.put(clientId, 1);
            return true;
        }
        
        int count = requestCounts.getOrDefault(clientId, 0);
        if (count < maxRequests) {
            requestCounts.put(clientId, count + 1);
            return true;
        }
        return false;
    }
}
```

### Limitations and Design Flaws:

1. **Fixed Window Problem**:
   - Client can make 100 requests at 0:59 and 100 more at 1:01
   - Results in 200 requests in 2 seconds, defeating the purpose
   - No smoothing of request rate

2. **No Burst Handling**:
   - Cannot handle legitimate traffic bursts
   - Strictly rejects any request over the limit
   - No concept of "saving up" unused quota

3. **Race Conditions**:
   - Not thread-safe for concurrent requests
   - Check-and-update is not atomic
   - Can allow more requests than intended

4. **Memory Issues**:
   - Never cleans up old client entries
   - Memory grows unbounded over time
   - No eviction of inactive clients

5. **Violation of OCP**:
   - Hard to add new rate limiting algorithms
   - Configuration is hardcoded
   - No separation of concerns

6. **Poor Client Experience**:
   - No information about remaining quota
   - No retry-after guidance
   - Binary allow/deny response

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Rate Limiting Algorithms | Different algorithms (Token Bucket, Sliding Window) |
| **Factory** | RateLimiter Creation | Create limiters based on configuration |
| **Template Method** | Base Rate Limiter | Common structure with customizable logic |

### Rate Limiting Algorithms Explained:

#### 1. Token Bucket Algorithm
```
┌─────────────────────────────────────────────────────────────┐
│                      TOKEN BUCKET                           │
├─────────────────────────────────────────────────────────────┤
│  Capacity: 10 tokens                                        │
│  Refill Rate: 2 tokens/second                              │
│                                                             │
│  ┌─────────────────────────────────────┐                   │
│  │  🪙 🪙 🪙 🪙 🪙 🪙 ⚪ ⚪ ⚪ ⚪      │ ← Current: 6 tokens │
│  └─────────────────────────────────────┘                   │
│           ↑                                                 │
│     Tokens refill                                          │
│     over time                                              │
│                                                             │
│  Request arrives:                                          │
│  - If tokens > 0: Allow, remove 1 token                    │
│  - If tokens = 0: Reject                                   │
└─────────────────────────────────────────────────────────────┘
```
- **Pros**: Smooth rate limiting, allows bursts up to bucket size
- **Cons**: Burst at start of period possible
- **Use Case**: API rate limiting with burst tolerance

#### 2. Sliding Window Log
```
┌─────────────────────────────────────────────────────────────┐
│                   SLIDING WINDOW LOG                        │
├─────────────────────────────────────────────────────────────┤
│  Window: 1 minute, Max: 5 requests                         │
│                                                             │
│  Timeline (now = 10:01:00):                                │
│  ├──────────────────────────────────────────────────────┤  │
│  10:00:00    10:00:30    10:00:45    10:01:00              │
│     ❌          │           │           │                  │
│   expired      ✓           ✓           ✓                   │
│                                                             │
│  Timestamps in window: [10:00:30, 10:00:45, 10:00:55]      │
│  Count: 3 (under limit of 5) → ALLOW                       │
└─────────────────────────────────────────────────────────────┘
```
- **Pros**: Most accurate, no boundary issues
- **Cons**: High memory usage (stores all timestamps)
- **Use Case**: When precision is critical

#### 3. Sliding Window Counter
```
┌─────────────────────────────────────────────────────────────┐
│                 SLIDING WINDOW COUNTER                      │
├─────────────────────────────────────────────────────────────┤
│  Window: 1 minute, Max: 100 requests                       │
│                                                             │
│  Previous Window: 80 requests                              │
│  Current Window: 30 requests                               │
│  Current position: 40% into window                         │
│                                                             │
│  Weighted count = 80 * (1 - 0.4) + 30 = 48 + 30 = 78       │
│  78 < 100 → ALLOW                                          │
│                                                             │
│  ┌─────────────────┬─────────────────┐                     │
│  │   Prev: 80      │   Curr: 30      │                     │
│  │   (60% weight)  │   (40% in)      │                     │
│  └─────────────────┴─────────────────┘                     │
└─────────────────────────────────────────────────────────────┘
```
- **Pros**: Memory efficient, good approximation
- **Cons**: Not 100% accurate (weighted average)
- **Use Case**: High-traffic systems needing efficiency

#### 4. Fixed Window Counter
```
┌─────────────────────────────────────────────────────────────┐
│                  FIXED WINDOW COUNTER                       │
├─────────────────────────────────────────────────────────────┤
│  Window: 1 minute, Max: 100 requests                       │
│                                                             │
│  ┌─────────────────┬─────────────────┬─────────────────┐   │
│  │  Window 1       │  Window 2       │  Window 3       │   │
│  │  10:00-10:01    │  10:01-10:02    │  10:02-10:03    │   │
│  │  Count: 100 ✓   │  Count: 45 ✓    │  Count: 0       │   │
│  └─────────────────┴─────────────────┴─────────────────┘   │
│                                                             │
│  Simple but has boundary problem:                          │
│  99 requests at 10:00:59 + 99 at 10:01:01 = 198 in 2 sec!  │
└─────────────────────────────────────────────────────────────┘
```
- **Pros**: Simple, memory efficient
- **Cons**: Boundary spike problem
- **Use Case**: Simple rate limiting, non-critical systems

### Core Classes:

#### 1. Models Layer (`models/`)
- `RateLimitConfig` - Configuration (requests per window, window size)
- `RateLimitResult` - Result with allowed, remaining, resetTime
- `RateLimitAlgorithm` - Enum: TOKEN_BUCKET, SLIDING_WINDOW, FIXED_WINDOW

#### 2. Strategy Pattern (`strategies/`)
- `RateLimitStrategy` - Interface for rate limiting algorithms
- `TokenBucketStrategy` - Token bucket implementation
- `SlidingWindowLogStrategy` - Timestamp-based sliding window
- `SlidingWindowCounterStrategy` - Counter-based approximation
- `FixedWindowStrategy` - Simple fixed window counter

#### 3. Rate Limiter (`limiter/`)
- `RateLimiter` - Interface for rate limiting
- `InMemoryRateLimiter` - Main implementation with strategy
- `ThreadSafeRateLimiter` - Decorator for thread safety

### Class Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                    <<interface>>                            │
│                     RateLimiter                             │
├─────────────────────────────────────────────────────────────┤
│ + tryAcquire(clientId: String): RateLimitResult            │
│ + tryAcquire(clientId: String, permits: int): Result       │
│ + getRemainingQuota(clientId: String): int                  │
│ + getResetTime(clientId: String): Instant                   │
└─────────────────────────────────────────────────────────────┘
                           △
                           │
          ┌────────────────┼────────────────┐
          │                                 │
┌─────────────────────┐         ┌─────────────────────┐
│ InMemoryRateLimiter │         │ ThreadSafeRateLimiter│
├─────────────────────┤         ├─────────────────────┤
│ - clientBuckets: Map│         │ - delegate: Limiter │
│ - strategy: Strategy│         │ - locks: Map<Lock>  │
└─────────────────────┘         └─────────────────────┘
         │
         │ uses
         ▼
┌─────────────────────────────────────────────────────────────┐
│                    <<interface>>                            │
│                   RateLimitStrategy                         │
├─────────────────────────────────────────────────────────────┤
│ + tryAcquire(bucket: Bucket, permits: int): boolean        │
│ + getRemaining(bucket: Bucket): int                         │
│ + getResetTime(bucket: Bucket): Instant                     │
└─────────────────────────────────────────────────────────────┘
                           △
          ┌────────────────┼────────────────┐
          │                │                │
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│ TokenBucket      │ │ SlidingWindow    │ │ FixedWindow      │
│ Strategy         │ │ Strategy         │ │ Strategy         │
└──────────────────┘ └──────────────────┘ └──────────────────┘
```

---

## 4. Final Design Overview

### Token Bucket Implementation Details:

```java
class TokenBucket {
    private double tokens;           // Current token count
    private long lastRefillTime;     // Last time tokens were added
    private final int capacity;      // Max tokens (burst size)
    private final double refillRate; // Tokens per second
    
    synchronized boolean tryAcquire(int permits) {
        refill();  // Add tokens based on elapsed time
        if (tokens >= permits) {
            tokens -= permits;
            return true;
        }
        return false;
    }
    
    private void refill() {
        long now = System.nanoTime();
        double elapsed = (now - lastRefillTime) / 1e9;  // seconds
        tokens = Math.min(capacity, tokens + elapsed * refillRate);
        lastRefillTime = now;
    }
}
```

### Interview Discussion Points:

1. **Token Bucket vs Leaky Bucket?**
   - Token Bucket: Controls input rate, allows bursts
   - Leaky Bucket: Controls output rate, smooths traffic
   - Token Bucket more common for API rate limiting

2. **Distributed Rate Limiting?**
   - Single node: In-memory works fine
   - Distributed: Need Redis/centralized store
   - Options: Redis INCR with EXPIRE, or Lua scripts

3. **Handling Clock Skew?**
   - Use monotonic clocks (System.nanoTime)
   - For distributed: consensus on time or logical clocks

4. **Rate Limit Headers?**
   - `X-RateLimit-Limit`: Max requests allowed
   - `X-RateLimit-Remaining`: Requests left in window
   - `X-RateLimit-Reset`: Unix timestamp when limit resets
   - `Retry-After`: Seconds to wait before retrying

5. **Graceful Degradation?**
   - Soft limits with warnings before hard limits
   - Priority queues for important requests
   - Circuit breaker pattern integration

