# Design Ride/Delivery Matching Engine

## 1. Problem Statement and Requirements

Design a Matching Engine for ride-sharing (like Uber/Lyft) or food delivery (like DoorDash/Instacart) platforms. The engine matches incoming ride/delivery requests with available drivers/delivery partners based on various factors.

### Functional Requirements:

- **Driver Management**: Track driver location, availability status, and attributes
- **Request Matching**: Match incoming requests with the best available driver
- **Multiple Matching Strategies**: Support different matching algorithms:
  - **Nearest Driver**: Match with closest available driver
  - **Priority-Based**: Consider driver priority/tier (premium, regular)
  - **Scoring-Based**: Multi-factor scoring (distance, rating, acceptance rate)
  - **Load Balanced**: Distribute requests evenly across drivers
- **Real-time Updates**: Handle driver location updates and status changes
- **Match Confirmation**: Handle driver accept/reject flow
- **Timeout Handling**: Reassign if driver doesn't respond

### Non-Functional Requirements:

- **Low Latency**: Match within milliseconds for good user experience
- **Scalability**: Handle thousands of concurrent requests
- **Fairness**: Ensure fair distribution of rides among drivers
- **Accuracy**: Optimal matching considering multiple factors
- **Extensibility**: Easy to add new matching criteria

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might iterate through all drivers to find the nearest one:

```java
class SimpleMatchingEngine {
    private List<Driver> drivers = new ArrayList<>();
    
    public Driver findDriver(Location pickupLocation) {
        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                double distance = calculateDistance(
                    driver.getLocation(), pickupLocation);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestDriver = driver;
                }
            }
        }
        return nearestDriver;
    }
}
```

### Limitations and Design Flaws:

1. **O(n) Time Complexity**:
   - Iterates through ALL drivers for every request
   - Doesn't scale with thousands of drivers
   - No spatial indexing for efficient proximity search

2. **Single Matching Criterion**:
   - Only considers distance
   - Ignores driver rating, vehicle type, acceptance rate
   - No way to balance load across drivers

3. **No Fairness Mechanism**:
   - Same nearby driver always gets matched
   - Other drivers may never get rides
   - No consideration of how long driver has been waiting

4. **Race Conditions**:
   - Not thread-safe for concurrent requests
   - Same driver could be matched to multiple rides
   - No locking mechanism

5. **No Match Lifecycle**:
   - No handling of driver accept/reject
   - No timeout mechanism
   - No reassignment logic

6. **Violation of OCP**:
   - Hard to add new matching criteria
   - No separation of concerns
   - Tightly coupled logic

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Matching Algorithms | Different algorithms (Nearest, Scoring, Load-balanced) |
| **Observer** | Status Updates | Notify when match status changes |
| **Factory** | Engine Creation | Create engines with different strategies |
| **State** | Match Lifecycle | Pending → Accepted/Rejected → Completed |

### Spatial Indexing for Efficient Search:

For production systems, use spatial data structures:

```
┌─────────────────────────────────────────────────────────────┐
│                    GEOSPATIAL INDEX                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Option 1: Quadtree                                        │
│  ┌─────────┬─────────┐                                     │
│  │ NW      │ NE      │  - Divide space into quadrants     │
│  │  🚗     │   🚗🚗  │  - O(log n) search                  │
│  ├─────────┼─────────┤  - Good for clustered data          │
│  │ SW      │ SE      │                                     │
│  │   🚗    │  🚗     │                                     │
│  └─────────┴─────────┘                                     │
│                                                             │
│  Option 2: Geohash Grid                                    │
│  ┌───┬───┬───┬───┐                                         │
│  │u4p│u4q│u4r│u4s│    - Hash location to grid cell        │
│  ├───┼───┼───┼───┤    - O(1) cell lookup                   │
│  │u4t│u4u│u4v│u4w│    - Search adjacent cells for nearby   │
│  └───┴───┴───┴───┘                                         │
│                                                             │
│  Option 3: R-Tree                                          │
│  - Balanced tree for spatial data                          │
│  - Efficient range queries                                 │
│  - Used by PostGIS, MongoDB                                │
└─────────────────────────────────────────────────────────────┘
```

For this LLD, we'll use a simplified grid-based approach.

### Matching Score Calculation:

```
┌─────────────────────────────────────────────────────────────┐
│                   SCORING ALGORITHM                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Final Score = Σ (weight_i × normalized_factor_i)          │
│                                                             │
│  Factors:                                                   │
│  ┌────────────────────┬────────┬───────────────────────┐   │
│  │ Factor             │ Weight │ Normalization         │   │
│  ├────────────────────┼────────┼───────────────────────┤   │
│  │ Distance           │  0.40  │ 1 - (dist/max_dist)   │   │
│  │ Driver Rating      │  0.25  │ rating / 5.0          │   │
│  │ Acceptance Rate    │  0.15  │ rate / 100            │   │
│  │ Wait Time          │  0.10  │ waitTime/maxWait      │   │
│  │ Vehicle Match      │  0.10  │ 1.0 or 0.5            │   │
│  └────────────────────┴────────┴───────────────────────┘   │
│                                                             │
│  Example:                                                   │
│  Driver A: dist=2km, rating=4.8, acceptance=95%            │
│  Score = 0.4×0.8 + 0.25×0.96 + 0.15×0.95 + ...            │
│        = 0.32 + 0.24 + 0.14 + ... = 0.85                   │
└─────────────────────────────────────────────────────────────┘
```

### Core Classes:

#### 1. Models Layer (`models/`)
- `Driver` - Driver entity with location, status, attributes
- `DriverStatus` - Enum: AVAILABLE, BUSY, OFFLINE
- `Rider` - Rider entity requesting a ride
- `RideRequest` - Request with pickup/dropoff, vehicle type
- `Match` - Matched pair of driver and request
- `MatchStatus` - Enum: PENDING, ACCEPTED, REJECTED, TIMEOUT, COMPLETED
- `Location` - Coordinates with distance calculation
- `VehicleType` - Enum: BIKE, CAR, SUV, PREMIUM

#### 2. Strategy Pattern (`strategies/`)
- `MatchingStrategy` - Interface for matching algorithms
- `NearestDriverStrategy` - Match closest available driver
- `ScoringBasedStrategy` - Multi-factor scoring
- `LoadBalancedStrategy` - Fair distribution across drivers
- `PriorityBasedStrategy` - Consider driver priority/tier

#### 3. Services (`services/`)
- `DriverService` - Manage drivers, locations, status
- `MatchingService` - Core matching logic
- `LocationService` - Geospatial operations

#### 4. Engine
- `MatchingEngine` - Main entry point (Singleton)

### Class Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                     MatchingEngine                          │
│                      (Singleton)                            │
├─────────────────────────────────────────────────────────────┤
│ - driverService: DriverService                              │
│ - matchingService: MatchingService                          │
│ - strategy: MatchingStrategy                                │
├─────────────────────────────────────────────────────────────┤
│ + findMatch(request: RideRequest): Optional<Match>         │
│ + updateDriverLocation(driverId, location): void           │
│ + setDriverStatus(driverId, status): void                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ uses
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  <<interface>>                              │
│                  MatchingStrategy                           │
├─────────────────────────────────────────────────────────────┤
│ + findBestMatch(request, drivers): Optional<Driver>        │
│ + rankDrivers(request, drivers): List<ScoredDriver>        │
└─────────────────────────────────────────────────────────────┘
                              △
          ┌───────────────────┼───────────────────┐
          │                   │                   │
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│NearestDriver    │  │ScoringBased     │  │LoadBalanced     │
│Strategy         │  │Strategy         │  │Strategy         │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

---

## 4. Final Design Overview

### Match Flow:

```
┌──────────┐    ┌───────────────┐    ┌─────────────────┐
│  Rider   │───>│ RideRequest   │───>│ MatchingEngine  │
│ requests │    │ (pickup, type)│    │                 │
└──────────┘    └───────────────┘    └────────┬────────┘
                                              │
                                              ▼
                                    ┌─────────────────┐
                                    │ DriverService   │
                                    │ (get available) │
                                    └────────┬────────┘
                                              │
                                              ▼
                                    ┌─────────────────┐
                                    │MatchingStrategy │
                                    │ (rank drivers)  │
                                    └────────┬────────┘
                                              │
                                              ▼
                                    ┌─────────────────┐
                                    │    Match        │
                                    │ (driver, rider) │
                                    └────────┬────────┘
                                              │
                    ┌─────────────────────────┼─────────────────────────┐
                    ▼                         ▼                         ▼
            ┌───────────┐            ┌───────────┐            ┌───────────┐
            │  PENDING  │───────────>│ ACCEPTED  │───────────>│ COMPLETED │
            └───────────┘            └───────────┘            └───────────┘
                    │                         
                    ▼                         
            ┌───────────┐            
            │ REJECTED/ │            
            │ TIMEOUT   │───> Try next driver           
            └───────────┘            
```

### Interview Discussion Points:

1. **How to handle high-density areas?**
   - Grid-based partitioning
   - Limit candidates to nearby grid cells
   - Pre-filter before scoring

2. **How to ensure fairness?**
   - Track "idle time" per driver
   - Weighted random selection among top candidates
   - Round-robin within same score tier

3. **How to handle surge pricing?**
   - Dynamic multiplier based on demand/supply ratio
   - Feed into matching score to incentivize drivers

4. **Distributed system considerations?**
   - Driver locations in Redis with geospatial index
   - Pub/sub for real-time updates
   - Consistent hashing for request routing

5. **How to handle driver acceptance?**
   - Timeout mechanism (e.g., 30 seconds)
   - Cascade to next best match on rejection
   - Track acceptance rate for future scoring

