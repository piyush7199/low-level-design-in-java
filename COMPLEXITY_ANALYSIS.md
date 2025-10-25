# Time & Space Complexity Analysis 📊

## Table of Contents
- [Introduction](#introduction)
- [Big O Notation](#big-o-notation)
- [Common Complexities](#common-complexities)
- [Analysis by Problem](#analysis-by-problem)
- [Data Structure Complexities](#data-structure-complexities)
- [Algorithm Complexities](#algorithm-complexities)
- [Tips for Optimization](#tips-for-optimization)

---

## Introduction

Understanding time and space complexity is crucial for:
- **Interviews:** Often asked after design questions
- **Performance:** Choosing right data structures
- **Scalability:** Ensuring system can handle growth
- **Trade-offs:** Balancing time vs space

---

## Big O Notation

### What is Big O?
Big O describes the **worst-case** performance of an algorithm as input size grows.

### Common Notations (Best to Worst):
```
O(1)         - Constant
O(log n)     - Logarithmic
O(n)         - Linear
O(n log n)   - Linearithmic
O(n²)        - Quadratic
O(n³)        - Cubic
O(2ⁿ)        - Exponential
O(n!)        - Factorial
```

### Visual Comparison:
```
Operations for n = 100:
O(1)      →        1 operation      ✅ Excellent
O(log n)  →        7 operations     ✅ Excellent
O(n)      →      100 operations     ✅ Good
O(n log n)→      700 operations     ⚠️  Fair
O(n²)     →   10,000 operations     ❌ Poor
O(2ⁿ)     → 1.27e30 operations      ❌ Terrible
```

---

## Common Complexities

### O(1) - Constant Time
```java
// Array access
int element = array[5];

// HashMap get/put
map.put(key, value);
value = map.get(key);

// Stack push/pop
stack.push(item);
stack.pop();
```

### O(log n) - Logarithmic Time
```java
// Binary search in sorted array
int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}

// TreeMap operations
treeMap.get(key);  // O(log n)
```

### O(n) - Linear Time
```java
// Array traversal
for (int i = 0; i < array.length; i++) {
    // do something
}

// Finding element in unsorted array
for (int num : numbers) {
    if (num == target) return true;
}
```

### O(n log n) - Linearithmic Time
```java
// Efficient sorting algorithms
Arrays.sort(array);  // QuickSort, MergeSort
Collections.sort(list);
```

### O(n²) - Quadratic Time
```java
// Nested loops
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        // do something
    }
}

// Bubble Sort
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n - i - 1; j++) {
        if (arr[j] > arr[j + 1]) {
            // swap
        }
    }
}
```

---

## Analysis by Problem

### Parking Lot System

#### Find Available Spot
**Naive Approach:**
```java
// Time: O(n) - iterate through all spots
// Space: O(1) - no extra space
for (ParkingSpot spot : spots) {
    if (spot.isAvailable()) {
        return spot;
    }
}
```

**Optimized Approach:**
```java
// Time: O(1) - use priority queue of available spots
// Space: O(n) - store available spots in queue
PriorityQueue<ParkingSpot> availableSpots;
return availableSpots.poll();  // O(log n) for heap operations
```

#### Check if Spot Available
```java
// Time: O(1) - direct access
// Space: O(1)
boolean isAvailable = spot.getStatus() == SpotStatus.AVAILABLE;
```

---

### Library Management System

#### Search Book by Title
**Linear Search:**
```java
// Time: O(n) - check all books
// Space: O(1)
for (Book book : books) {
    if (book.getTitle().equals(title)) {
        return book;
    }
}
```

**Optimized with HashMap:**
```java
// Time: O(1) average case
// Space: O(n) - store all books in map
Map<String, Book> booksByTitle;
return booksByTitle.get(title);
```

#### Borrow Book
```java
// Time: O(1) - assuming direct access to BookItem
// Space: O(1) - just creating LendingRecord
BookItem item = findAvailableItem(isbn);  // O(1) with HashMap
LendingRecord record = new LendingRecord(member, item);
```

#### Get Overdue Books
```java
// Time: O(n) - check all active lendings
// Space: O(k) - where k is number of overdue books
List<LendingRecord> overdue = new ArrayList<>();
for (LendingRecord record : activeLendings) {
    if (record.isOverdue()) {
        overdue.add(record);
    }
}
```

---

### Elevator System

#### Find Nearest Elevator
**Naive:**
```java
// Time: O(m) - check all m elevators
// Space: O(1)
for (Elevator elevator : elevators) {
    int distance = Math.abs(elevator.getCurrentFloor() - requestFloor);
    if (distance < minDistance) {
        nearest = elevator;
        minDistance = distance;
    }
}
```

**Optimized:** Use spatial indexing
```java
// Time: O(log m) - binary search on sorted positions
// Space: O(m) - maintain sorted list
```

#### Process Single Floor
```java
// Time: O(1) - constant operations
// Space: O(1)
elevator.moveToFloor(targetFloor);
elevator.openDoors();
elevator.closeDoors();
```

---

### Food Delivery System

#### Find Nearest Delivery Partner
**Naive:**
```java
// Time: O(d) - check all d delivery partners
// Space: O(1)
for (DeliveryPartner partner : partners) {
    if (partner.isAvailable()) {
        double distance = calculateDistance(partner.getLocation(), restaurant);
        // track nearest
    }
}
```

**Optimized with Spatial Indexing (QuadTree):**
```java
// Time: O(log d) - spatial query
// Space: O(d) - quadtree structure
quadTree.findNearestAvailable(restaurantLocation, radius);
```

#### Place Order
```java
// Time: O(1) - mostly constant operations
// Space: O(1) - create one order object
Order order = new Order(customer, restaurant, items);
DeliveryPartner partner = findNearest();  // O(d) or O(log d) with optimization
```

---

### Ride-Sharing System

#### Find Nearest Driver
**Without Spatial Indexing:**
```java
// Time: O(d) - check all d drivers
// Space: O(1)
for (Driver driver : drivers) {
    if (driver.isAvailable()) {
        double distance = location.distanceTo(driver.getLocation());
        // track minimum
    }
}
```

**With Geohashing:**
```java
// Time: O(k) - only check drivers in nearby geohash buckets
//        where k << d (k is much smaller than d)
// Space: O(d) - store drivers in geohash map
String geohash = Geohash.encode(latitude, longitude, precision);
List<Driver> nearbyDrivers = geohashMap.get(geohash);
// Check only nearby drivers
```

#### Calculate Fare
```java
// Time: O(1) - simple arithmetic
// Space: O(1)
double distance = pickup.distanceTo(dropoff);
double fare = baseFare + (distance * ratePerKm * multiplier);
```

---

## Data Structure Complexities

### ArrayList vs LinkedList
```
Operation          | ArrayList | LinkedList
-------------------|-----------|------------
Get by index       | O(1)      | O(n)
Add at end         | O(1)*     | O(1)
Add at beginning   | O(n)      | O(1)
Remove from end    | O(1)      | O(1)
Remove from middle | O(n)      | O(n)
Contains           | O(n)      | O(n)

* Amortized - occasionally O(n) for resizing
```

### HashMap vs TreeMap
```
Operation          | HashMap   | TreeMap
-------------------|-----------|----------
Get                | O(1)*     | O(log n)
Put                | O(1)*     | O(log n)
Remove             | O(1)*     | O(log n)
Contains Key       | O(1)*     | O(log n)
Iterate (sorted)   | O(n log n)| O(n)

* Average case - worst case O(n) with collisions
```

### HashSet vs TreeSet
```
Operation          | HashSet   | TreeSet
-------------------|-----------|----------
Add                | O(1)*     | O(log n)
Remove             | O(1)*     | O(log n)
Contains           | O(1)*     | O(log n)
Min/Max            | O(n)      | O(log n)
```

### Priority Queue (Heap)
```
Operation          | Complexity
-------------------|------------
Peek (min/max)     | O(1)
Poll (remove)      | O(log n)
Add                | O(log n)
Contains           | O(n)
```

---

## Algorithm Complexities

### Sorting Algorithms
```
Algorithm    | Best      | Average   | Worst     | Space
-------------|-----------|-----------|-----------|-------
Quick Sort   | O(n log n)| O(n log n)| O(n²)     | O(log n)
Merge Sort   | O(n log n)| O(n log n)| O(n log n)| O(n)
Heap Sort    | O(n log n)| O(n log n)| O(n log n)| O(1)
Bubble Sort  | O(n)      | O(n²)     | O(n²)     | O(1)
Insertion Sort| O(n)     | O(n²)     | O(n²)     | O(1)
```

### Searching Algorithms
```
Algorithm      | Time      | Space | Notes
---------------|-----------|-------|------------------
Linear Search  | O(n)      | O(1)  | Any array
Binary Search  | O(log n)  | O(1)  | Sorted array only
Hash Table     | O(1)      | O(n)  | Average case
```

---

## Tips for Optimization

### 1. Use Appropriate Data Structures
```java
// ❌ Bad - O(n) for lookups
List<User> users = new ArrayList<>();
// Need to iterate to find user

// ✅ Good - O(1) for lookups
Map<String, User> userMap = new HashMap<>();
User user = userMap.get(userId);
```

### 2. Cache Expensive Computations
```java
// ❌ Bad - Recalculate every time
public double getDistance(Location a, Location b) {
    return calculateDistance(a, b);  // Expensive
}

// ✅ Good - Cache result
private Map<String, Double> distanceCache = new HashMap<>();

public double getDistance(Location a, Location b) {
    String key = a.toString() + "-" + b.toString();
    return distanceCache.computeIfAbsent(key, 
        k -> calculateDistance(a, b));
}
```

### 3. Avoid Nested Loops When Possible
```java
// ❌ Bad - O(n²)
for (User user : users) {
    for (Order order : orders) {
        if (order.getUserId().equals(user.getId())) {
            // process
        }
    }
}

// ✅ Good - O(n + m)
Map<String, User> userMap = createUserMap(users);  // O(n)
for (Order order : orders) {  // O(m)
    User user = userMap.get(order.getUserId());  // O(1)
    // process
}
```

### 4. Use Early Termination
```java
// ❌ Bad - Always checks all
public boolean hasActiveUser(List<User> users) {
    boolean hasActive = false;
    for (User user : users) {
        if (user.isActive()) {
            hasActive = true;
        }
    }
    return hasActive;
}

// ✅ Good - Returns as soon as found
public boolean hasActiveUser(List<User> users) {
    for (User user : users) {
        if (user.isActive()) {
            return true;  // Early termination
        }
    }
    return false;
}
```

### 5. Use Batch Operations
```java
// ❌ Bad - Multiple DB calls
for (User user : users) {
    database.save(user);  // N database calls
}

// ✅ Good - Single batch call
database.saveAll(users);  // 1 database call
```

---

## Interview Tips

### Discussing Complexity:
1. **Always mention both time AND space**
   - "This solution is O(n) time and O(1) space"

2. **Clarify the variables**
   - "Where n is the number of users and m is the number of orders"

3. **Mention best/average/worst cases when relevant**
   - "HashMap operations are O(1) average, O(n) worst case"

4. **Discuss trade-offs**
   - "We can reduce time from O(n) to O(1) but it increases space from O(1) to O(n)"

5. **Suggest optimizations**
   - "We could use caching to improve repeated lookups"

---

## Quick Reference Card

```
O(1)      → Excellent → HashMap get, Array access
O(log n)  → Great     → Binary search, TreeMap operations
O(n)      → Good      → Single loop, Array traversal
O(n log n)→ Fair      → Efficient sorting
O(n²)     → Poor      → Nested loops
O(2ⁿ)     → Terrible  → Avoid if possible
```

---

**Remember:** Premature optimization is the root of all evil. First make it work, then make it right, then make it fast!

Happy Optimizing! 🚀

