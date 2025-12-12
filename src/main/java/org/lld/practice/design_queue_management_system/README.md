# Design Online Queue Management System (Token System)

## 1. Problem Statement and Requirements

Design a Token-based Queue Management System commonly used in banks, hospitals, government offices, or service centers where customers take tokens and wait for their turn to be served.

### Functional Requirements:

- **Token Generation**: Generate unique tokens for customers with optional priority levels (Regular, Priority, VIP, Senior Citizen)
- **Multiple Counters/Service Points**: Support multiple service counters, each capable of serving specific token types
- **Queue Management**: FIFO processing with priority queue support
- **Status Tracking**: Track token status through its lifecycle (Waiting → Being Served → Completed/Cancelled/No-Show)
- **Counter Assignment**: Assign tokens to available counters based on service type and availability
- **Notifications**: Notify customers when their turn is approaching (SMS/Display)
- **Display Board**: Show current token being served at each counter in real-time
- **Analytics**: Track metrics like average wait time, service time, tokens processed per counter

### Non-Functional Requirements:

- **Concurrency**: Handle multiple tokens generated and called simultaneously without race conditions
- **Real-time Updates**: Display boards should update in real-time when tokens are called
- **Scalability**: System should be extensible for multiple branches/locations
- **Reliability**: System should handle counter failures gracefully
- **Maintainability**: Easy to add new token types, counter types, or notification channels

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might create a single `QueueSystem` class that handles everything - token generation, queue management, counter assignment, notifications, and display updates.

```java
class QueueSystem {
    private int tokenCounter = 0;
    private List<String> waitingQueue = new ArrayList<>();
    private Map<String, String> counterTokens = new HashMap<>();
    
    public String generateToken(String customerName, String type) {
        tokenCounter++;
        String token = type.charAt(0) + "-" + String.format("%03d", tokenCounter);
        waitingQueue.add(token);
        System.out.println("Token generated: " + token);
        return token;
    }
    
    public void callNextToken(String counterId) {
        if (!waitingQueue.isEmpty()) {
            String token = waitingQueue.remove(0);
            counterTokens.put(counterId, token);
            System.out.println("Counter " + counterId + " calling token: " + token);
            // Send SMS notification
            // Update display board
            // Log analytics
        }
    }
    
    public void completeService(String counterId) {
        String token = counterTokens.remove(counterId);
        System.out.println("Service completed for token: " + token);
    }
}
```

### Limitations and Design Flaws:

1. **Violation of Single Responsibility Principle (SRP)**:
   - One class handles token generation, queue management, counter management, notifications, display updates, and analytics
   - Any change to one feature affects the entire class

2. **Violation of Open/Closed Principle (OCP)**:
   - Adding a new token type (e.g., "Emergency") requires modifying the `generateToken()` method
   - Adding a new notification channel (e.g., WhatsApp) requires modifying the class
   - No easy way to change token assignment strategy

3. **No Priority Handling**:
   - Simple FIFO doesn't account for priority tokens
   - VIP customers wait the same as regular customers

4. **Tight Coupling**:
   - Display logic, SMS logic, and analytics are all embedded in the main class
   - Cannot test components in isolation

5. **Concurrency Issues**:
   - Multiple counters calling `callNextToken()` simultaneously could cause race conditions
   - No thread-safety considerations

6. **No State Management**:
   - Token status is not properly tracked through its lifecycle
   - Cannot handle scenarios like "No-Show" or "Recall"

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Singleton** | `QueueManagementSystem` | Single system instance managing all operations |
| **Strategy** | Token Assignment | Different algorithms (FIFO, Priority-based) for selecting next token |
| **Observer** | Notifications | Display board, SMS notifications, analytics all observe queue changes |
| **State** | Token Lifecycle | Clean state transitions (Waiting → Serving → Completed) |
| **Factory** | Token Creation | Create different token types based on customer category |

### Core Classes and Their Interactions:

#### 1. Models Layer (`models/`)
- `Token` - Core entity with token number, type, status, timestamps
- `TokenType` - Enum: REGULAR, PRIORITY, VIP, SENIOR_CITIZEN
- `TokenStatus` - Enum: WAITING, SERVING, COMPLETED, CANCELLED, NO_SHOW
- `Counter` - Service counter with ID, status, operator name
- `CounterStatus` - Enum: AVAILABLE, BUSY, CLOSED
- `Customer` - Customer details (name, phone, email)

#### 2. State Pattern (`states/`)
- `TokenState` - Interface defining token lifecycle operations
- `WaitingState` - Initial state, can be called or cancelled
- `ServingState` - Being served, can complete or mark no-show
- `CompletedState` - Terminal state, service finished
- `CancelledState` - Terminal state, customer cancelled

#### 3. Strategy Pattern (`strategies/`)
- `TokenAssignmentStrategy` - Interface for selecting next token
- `FIFOAssignmentStrategy` - Simple first-in-first-out
- `PriorityAssignmentStrategy` - Considers token priority levels

#### 4. Observer Pattern (`observers/`)
- `QueueObserver` - Interface for queue event notifications
- `DisplayBoardObserver` - Updates display when tokens are called
- `CustomerNotificationObserver` - Sends SMS/email notifications
- `AnalyticsObserver` - Collects wait time, service time metrics

#### 5. Services Layer (`services/`)
- `TokenService` - Token generation and management
- `QueueService` - Queue operations, observer notifications
- `CounterService` - Counter management and assignment

#### 6. Main Entry Point
- `QueueManagementSystem` - Singleton coordinating all components

### Class Diagram (Simplified):

```
┌─────────────────────────────────────────────────────────────────┐
│                    QueueManagementSystem                        │
│                        (Singleton)                              │
├─────────────────────────────────────────────────────────────────┤
│ - tokenService: TokenService                                    │
│ - queueService: QueueService                                    │
│ - counterService: CounterService                                │
├─────────────────────────────────────────────────────────────────┤
│ + generateToken(customer, type): Token                          │
│ + callNextToken(counterId): Optional<Token>                     │
│ + completeService(counterId): void                              │
│ + getDisplayBoard(): Map<String, Token>                         │
└─────────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
   ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
   │TokenService │    │QueueService │    │CounterService│
   │             │    │             │    │             │
   │+generate()  │    │+addToken()  │    │+addCounter()│
   │+getToken()  │    │+getNext()   │    │+getAvailable│
   └─────────────┘    │+notify()    │    └─────────────┘
                      └──────┬──────┘
                             │ notifies
          ┌──────────────────┼──────────────────┐
          ▼                  ▼                  ▼
   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐
   │DisplayBoard │   │Notification │   │ Analytics   │
   │  Observer   │   │  Observer   │   │  Observer   │
   └─────────────┘   └─────────────┘   └─────────────┘
```

---

## 4. Final Design Overview

Our final design achieves:

### Separation of Concerns
- **Models** - Pure data entities with no business logic
- **States** - Encapsulate token lifecycle behavior
- **Strategies** - Interchangeable assignment algorithms
- **Observers** - Decoupled notification mechanisms
- **Services** - Focused business logic layers

### Benefits:

1. **Extensibility (OCP)**:
   - Add new token types by creating new enum values
   - Add new assignment strategies by implementing `TokenAssignmentStrategy`
   - Add new notification channels by implementing `QueueObserver`

2. **Testability**:
   - Each component can be unit tested in isolation
   - Strategies and observers can be mocked easily

3. **Maintainability**:
   - Changes to display logic don't affect queue management
   - Changes to notification don't affect token generation

4. **Thread Safety**:
   - QueueService uses synchronized access to shared state
   - Singleton pattern prevents multiple system instances

### Key Interview Discussion Points:

1. **Concurrency**: How do you prevent two counters from picking the same token?
   - Use synchronized blocks or `ConcurrentLinkedQueue`
   - Atomic operations for state transitions

2. **Fairness**: How to prevent priority tokens from starving regular tokens?
   - Implement aging mechanism - boost priority of long-waiting tokens
   - Set maximum priority slots per time window

3. **Scalability**: How to extend for multiple branches?
   - Branch ID as prefix in token numbers
   - Centralized analytics aggregation
   - Branch-specific QueueManagementSystem instances

4. **Real-time Updates**: WebSocket vs Polling for display boards?
   - Observer pattern enables push notifications
   - WebSocket preferred for real-time, polling for simpler implementation

5. **Failure Recovery**: What if system crashes mid-service?
   - Persist token state to database
   - Load incomplete tokens on restart
   - Mark stale tokens as "No-Show" after timeout

