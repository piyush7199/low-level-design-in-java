# Design Online Auction System

## 1. Problem Statement and Requirements

Design an Online Auction System (like eBay) where users can create auctions, place bids, and win items through competitive bidding.

### Functional Requirements:

- **User Management**: Register users, manage accounts
- **Auction Creation**: Sellers can create auctions with items, starting price, duration
- **Bidding**: Buyers can place bids on active auctions
- **Bid Validation**: Validate bids (must be higher than current bid, auction must be active)
- **Auction States**: Track auction lifecycle (Scheduled, Active, Ended, Cancelled)
- **Auto-bidding**: Support proxy bidding (automatic bidding up to max amount)
- **Time Management**: Automatically end auctions at specified time
- **Winner Determination**: Determine winner when auction ends
- **Notifications**: Notify users about bid updates, auction end, winning/losing

### Non-Functional Requirements:

- **Concurrency**: Handle multiple simultaneous bids
- **Real-time Updates**: Update current bid in real-time
- **Data Consistency**: Ensure bid amounts are always correct
- **Scalability**: Support thousands of concurrent auctions
- **Reliability**: No bid loss, proper error handling

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might create a simple system with basic bid tracking:

```java
class SimpleAuctionSystem {
    private Map<String, Auction> auctions = new HashMap<>();
    
    public void placeBid(String auctionId, String userId, double amount) {
        Auction auction = auctions.get(auctionId);
        if (amount > auction.getCurrentBid()) {
            auction.setCurrentBid(amount);
            auction.setWinner(userId);
        }
    }
}
```

### Limitations and Design Flaws:

1. **Race Conditions**:
   - Multiple threads can place bids simultaneously
   - No synchronization mechanism
   - Bid amounts can be incorrect

2. **No Auction States**:
   - Can't track auction lifecycle
   - No scheduled/active/ended states
   - Can bid on ended auctions

3. **No Bid History**:
   - Only tracks current bid
   - No record of all bids
   - Can't show bid history

4. **No Time Management**:
   - No automatic auction ending
   - No scheduled start times
   - Manual management required

5. **No Proxy Bidding**:
   - Only supports manual bids
   - No automatic bidding up to max
   - Poor user experience

6. **No Notifications**:
   - Users not notified of bid updates
   - No winner notifications
   - Poor engagement

7. **Violation of SOLID**:
   - Single class doing everything
   - No separation of concerns
   - Hard to extend

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **State** | Auction Lifecycle | Auction states (Scheduled, Active, Ended, Cancelled) |
| **Observer** | Notifications | Notify users about bid updates, auction end |
| **Strategy** | Bidding Rules | Different bidding strategies (Standard, Proxy, Reserve) |
| **Singleton** | Auction Service | Single instance managing all auctions |
| **Command** | Bid Operations | Encapsulate bid placement, cancellation |

### Core Classes and Their Interactions:

#### 1. Models Layer (`models/`)
- `Auction` - Auction entity with item, seller, start/end time, current bid
- `Bid` - Bid entity with amount, bidder, timestamp
- `AuctionStatus` - Enum: SCHEDULED, ACTIVE, ENDED, CANCELLED
- `Item` - Item being auctioned

#### 2. State Pattern (`states/`)
- `AuctionState` - Interface defining auction lifecycle operations
- `ScheduledState` - Auction not started yet
- `ActiveState` - Auction is live, accepting bids
- `EndedState` - Auction ended, winner determined
- `CancelledState` - Auction cancelled

#### 3. Strategy Pattern (`strategies/`)
- `BiddingStrategy` - Interface for bidding rules
- `StandardBiddingStrategy` - Standard bid validation
- `ProxyBiddingStrategy` - Automatic bidding up to max

#### 4. Services (`services/`)
- `AuctionService` - Singleton managing auctions
- `BidService` - Handles bid placement, validation
- `NotificationService` - Sends notifications

#### 5. Observers (`observers/`)
- `AuctionObserver` - Interface for auction events
- `BidObserver` - Notifies about new bids
- `AuctionEndObserver` - Notifies when auction ends

### Key Design Benefits:

- **Thread Safety**: Proper synchronization for concurrent bids
- **State Management**: State pattern manages auction lifecycle
- **Extensibility**: Easy to add new bidding strategies
- **Real-time Updates**: Observer pattern for instant notifications
- **Bid History**: Complete record of all bids

---

## 4. Final Design Overview

The improved solution uses State pattern for auction lifecycle, Strategy pattern for flexible bidding rules, and Observer pattern for real-time notifications to create a robust, scalable auction system.

### Architecture Highlights:

- **Bid Validation**: Thread-safe bid validation with proper locking
- **Proxy Bidding**: Automatic bidding up to user's maximum
- **Time Management**: Scheduled tasks for auction start/end
- **Bid History**: Complete audit trail of all bids
- **Notifications**: Real-time updates for all participants

---

## 5. Interview Discussion Points

1. **Concurrency**: How to handle simultaneous bids on same auction?
2. **Proxy Bidding**: How does automatic bidding work?
3. **Time Management**: How to automatically end auctions?
4. **Bid Validation**: What are the rules for valid bids?
5. **Scalability**: How to scale for thousands of auctions?
6. **Notifications**: How to notify users in real-time?
7. **Data Consistency**: How to ensure bid amounts are correct?

---

## 6. Key Learnings

- **State Management**: Critical for auction lifecycle
- **Thread Safety**: Must handle concurrent bids correctly
- **Bid Validation**: Complex rules for valid bids
- **Proxy Bidding**: Enhances user experience
- **Time Management**: Scheduled tasks for automation
- **Notifications**: Observer pattern enables real-time updates

