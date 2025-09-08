# Design A Online Hotel Booking System

## 1. Problem Statement and Requirements

Our goal is to design a simplified online hotel booking system.

### Functional Requirements:

- **User Management:** Users can register, log in, and view their booking history.
- **Hotel & Room Management:**
    - The system must manage multiple hotels, each located in a specific city.
    - Each hotel has different types of rooms (e.g., single, double, suite).
    - The system must track the availability of each room.
- **Search & Booking:**
    - Users can search for rooms based on city, dates, and room type.
    - They can select a specific room and book it for a defined period.
    - The system must prevent double-booking of the same room.
- **Payment:** The system must handle payment for the booking.
- **Cancellation:** Users can cancel a booking within a specific time frame.

### Non-Functional Requirements:

- **Concurrency:** The system must handle multiple users trying to book the same room simultaneously without race
  conditions. This is a critical requirement.
- **Scalability:** The system should handle millions of users, thousands of hotels, and millions of bookings across many
  cities.
- **Availability:** The booking service should be highly available, especially during peak travel seasons.
- **Maintainability:** It should be easy to add new hotels, room types, or pricing models.

---

## 2. Naive Solution: The "Starting Point"

A beginner might, once again, opt for a single, monolithic class, such as BookingSystem, to handle all logic.

```java
class BookingSystem {
    List<Hotel> allHotels;
    List<Room> allRooms;
    List<Booking> allBookings;

    public void bookRoom(User user, Room room, Date checkIn, Date checkOut) {
        // ... Check room availability by iterating through all bookings
        // ... If available, create a new booking and add to the list
    }
}
```

### Limitations and Flaws:

- **Scalability Issues:** Iterating through a massive list of bookings for every availability check is inefficient and
  will not scale. This is a linear search on a potentially huge dataset.
- **Concurrency Nightmare:** A single shared object will be a massive bottleneck. Without proper synchronization, two
  users could check for availability and both mistakenly believe a room is free, leading to a double-booking. Locking
  the entire BookingSystem would destroy performance.
- **Single Responsibility Principle (SRP) Violation:** This class is responsible for managing users, hotels, rooms, and
  bookings. Any change to one part of the system could impact others.
- **Lack of Flexibility:** Adding a new room type or a different booking rule would require modifying this single, large
  class.

This naive solution is like having a single, universal calendar for a whole city of hotels. It's impossible for it to
scale.

---

## 3. Improved Solution: The "Mentor's Guidance"

A robust design requires a layered, component-based architecture that separates concerns and handles concurrency at a
fine-grained level.

### Key Design Patterns and Principles:

- **Repository Pattern:** We will use dedicated repository classes (e.g., `HotelRepository`, `RoomRepository`,
  `BookingRepository`) to abstract data access. This decouples the business logic from the data storage mechanism (e.g.,
  an SQL database vs. a NoSQL database).
- **Service Layer:** Business logic will reside in dedicated service classes like `HotelService`, `BookingService`, and
  `PaymentService`. The `BookingService` will be the primary orchestrator.
- **Concurrency Handling:** The most critical part. To prevent double-booking, the `BookingService` will use a
  transactional approach or pessimistic locking on the specific room being booked. When a user selects a room, its
  status can be temporarily set to "`reserved`" or "`pending`" within a transaction that either commits (on successful
  payment) or rolls back (on failure or timeout). This ensures atomicity.
- **Strategy Pattern:** The pricing model can be complex (e.g., standard rates, weekend rates, loyalty discounts). A
  `PricingStrategy` interface with concrete implementations for `StandardPricing`, `WeekendPricing`, etc., makes the
  system flexible and extensible.
- **Factory Pattern:** A RoomFactory can be used to create different types of rooms (`SingleRoom`, `DoubleRoom`). This
  hides the creation logic from the client code and makes it easy to add new room types in the future.

### Core Components and Their Roles:

1. **Entities (Data Models):** `User`, `Hotel`, `Room`, `Booking`. These are simple data-holding classes.
2. **Repositories**: `UserRepository`, `HotelRepository`, `RoomRepository`, `BookingRepository`. They handle all
   database interactions.
3. **Services:**
    - `HotelService`: Manages hotel information (e.g., adding/removing hotels, updating room lists).
    - `BookingService`: The core orchestrator. It uses the `RoomRepository` to check availability and the
      `BookingRepository` to create a new booking. It is also responsible for handling the booking transaction and
      concurrency.
    - `PaymentService`: Handles all financial transactions and uses a `PricingStrategy` to calculate the cost.

4. `RoomFactory:` Creates specific room objects based on type.
5. `PricingStrategy`: Defines the interface for different billing models.

---

## 4. Final Design Overview

The final design is a well-structured, modular, and scalable system.

- **Layered Architecture:** The separation of the data, service, and presentation layers makes the system clean and easy
  to maintain.
- **Concurrency-Safe:** The `BookingService` handles race conditions by using database transactions or locking
  mechanisms, ensuring data integrity.
- **Highly Extensible:** Adding new features like special promotions or new room types is as simple as creating a new
  class or adding a new repository method, without affecting core functionality.
- **Decoupled Components:** Each service is independent. The `BookingService` doesn't need to know how the
  `PaymentService` processes a payment; it just calls its `processPayment` method.