# Design Stock Trading System

## 1. Problem Statement and Requirements

Design a Stock Trading System that allows users to buy and sell stocks, manage portfolios, and execute trades through an order matching engine.

### Functional Requirements:

- **User Management**: Register users, manage accounts, track balances
- **Order Placement**: Place buy/sell orders (market orders, limit orders)
- **Order Matching**: Match buy and sell orders based on price and time priority
- **Portfolio Management**: Track user holdings, portfolio value, profit/loss
- **Order Types**: Support market orders (immediate execution) and limit orders (execute at specific price)
- **Order Status**: Track order lifecycle (Pending, Partially Filled, Filled, Cancelled)
- **Trade Execution**: Execute trades when orders match, update balances and holdings
- **Order Book**: Maintain order book showing buy/sell orders for each stock
- **Price Discovery**: Determine stock prices based on executed trades

### Non-Functional Requirements:

- **Low Latency**: Execute trades within milliseconds
- **Concurrency**: Handle thousands of concurrent orders
- **Data Consistency**: Ensure atomic operations for trade execution
- **Scalability**: Support multiple stocks and high trading volume
- **Reliability**: No order loss, proper error handling

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might create a simple system with basic order matching:

```java
class SimpleTradingSystem {
    private List<Order> buyOrders = new ArrayList<>();
    private List<Order> sellOrders = new ArrayList<>();
    
    public void placeOrder(Order order) {
        if (order.getType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
        matchOrders();
    }
    
    private void matchOrders() {
        for (Order buy : buyOrders) {
            for (Order sell : sellOrders) {
                if (buy.getPrice() >= sell.getPrice()) {
                    executeTrade(buy, sell);
                }
            }
        }
    }
}
```

### Limitations and Design Flaws:

1. **O(n²) Time Complexity**:
   - Nested loops for matching - very inefficient
   - Doesn't scale with high order volume
   - No priority queue for efficient matching

2. **No Order Types**:
   - Only supports simple price matching
   - No distinction between market and limit orders
   - No time priority consideration

3. **Race Conditions**:
   - Not thread-safe for concurrent orders
   - Multiple threads could match same order
   - No synchronization mechanism

4. **No Order Book**:
   - Orders stored in simple lists
   - No efficient price-time priority
   - Can't see market depth

5. **No Partial Fills**:
   - Orders either fully matched or not
   - No handling of partial order execution
   - Wastes liquidity

6. **No State Management**:
   - Orders don't have lifecycle states
   - Can't track order status
   - No cancellation mechanism

7. **Violation of SOLID**:
   - Single class doing everything
   - No separation of concerns
   - Hard to extend with new order types

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Order Matching | Different matching algorithms (Price-Time, Pro-Rata) |
| **State** | Order Lifecycle | Order states (Pending, Partially Filled, Filled, Cancelled) |
| **Factory** | Order Creation | Create different order types (Market, Limit) |
| **Observer** | Trade Notifications | Notify users about order execution |
| **Singleton** | Trading Engine | Single instance managing all trades |
| **Command** | Order Operations | Encapsulate order placement, cancellation |

### Core Classes and Their Interactions:

#### 1. Models Layer (`models/`)
- `Order` - Order entity with type, price, quantity, status
- `OrderType` - Enum: BUY, SELL
- `OrderStatus` - Enum: PENDING, PARTIALLY_FILLED, FILLED, CANCELLED
- `OrderSide` - Enum: MARKET, LIMIT
- `Trade` - Executed trade with price, quantity, timestamp
- `Portfolio` - User's stock holdings and cash balance
- `Stock` - Stock entity with symbol, name, current price

#### 2. State Pattern (`states/`)
- `OrderState` - Interface defining order lifecycle operations
- `PendingState` - Initial state, can be filled or cancelled
- `PartiallyFilledState` - Partially executed, can be filled more or cancelled
- `FilledState` - Terminal state, order fully executed
- `CancelledState` - Terminal state, order cancelled

#### 3. Strategy Pattern (`strategies/`)
- `MatchingStrategy` - Interface for order matching algorithms
- `PriceTimePriorityStrategy` - Match by price, then time (standard)
- `ProRataStrategy` - Proportional matching for large orders

#### 4. Services (`services/`)
- `TradingEngine` - Singleton managing order matching and execution
- `OrderService` - Handles order placement, cancellation, querying
- `PortfolioService` - Manages user portfolios, holdings, balances
- `OrderBookService` - Maintains order book for each stock

#### 5. Observers (`observers/`)
- `TradeObserver` - Interface for trade notifications
- `UserNotificationObserver` - Notifies users about order execution
- `MarketDataObserver` - Publishes market data updates

### Key Design Benefits:

- **Efficient Matching**: Priority queues for O(log n) order matching
- **Order Lifecycle**: State pattern manages order transitions
- **Extensibility**: Easy to add new order types or matching strategies
- **Thread Safety**: Proper synchronization for concurrent orders
- **Separation of Concerns**: Each component has single responsibility

---

## 4. Final Design Overview

The improved solution uses Strategy pattern for flexible matching algorithms, State pattern for order lifecycle management, and proper separation of concerns to create a scalable, maintainable stock trading system.

### Architecture Highlights:

- **Order Book**: Priority queues (buy: max-heap, sell: min-heap) for efficient matching
- **Atomic Operations**: Thread-safe order execution with proper locking
- **Partial Fills**: Support for partial order execution
- **Real-time Updates**: Observer pattern for trade notifications
- **Extensibility**: Easy to add new order types (Stop-Loss, Iceberg, etc.)

---

## 5. Interview Discussion Points

1. **Order Matching Algorithm**: How does price-time priority work?
2. **Concurrency**: How to handle race conditions in order matching?
3. **Partial Fills**: How to handle orders that can't be fully filled?
4. **Order Book Depth**: How to efficiently show market depth?
5. **Scalability**: How to scale for high-frequency trading?
6. **Data Persistence**: How to persist orders and trades?
7. **Market Data**: How to broadcast real-time market data?

---

## 6. Key Learnings

- **Priority Queues**: Essential for efficient order matching
- **State Management**: Critical for order lifecycle tracking
- **Thread Safety**: Must handle concurrent order placement
- **Order Types**: Different order types require different handling
- **Partial Execution**: Real-world systems must handle partial fills
- **Price Discovery**: Market price determined by executed trades

