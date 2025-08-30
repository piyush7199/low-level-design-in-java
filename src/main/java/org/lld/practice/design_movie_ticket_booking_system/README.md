# Design BookMyShow System

## 1. Problem Statement and Requirements

Our goal is to design a simplified online movie ticket booking system.

### Functional Requirements:

- **User Management:** Users should be able to register, log in, and view their booking history.
- **Movie and Theatre Management:**
    - The System must manage a list of movies, each with a name, duration and language.
    - It must manage multiple theaters, each located in different city.
- **Show Management:** A theatre can show multiple movies. Each movie-in-a-theatre called a "Show" and has specific
  date, time and screen number.
- **Seat Booking:**
    - Each show has a set of seats, some of which may be booked.
    - A user should be able to select and book one or more seats for a specific show.
    - The system must prevent a single seat from being booked by multiple users.
- **Payment:** The system must handle for the tickets.
- **Cancellation:** A user can cancel a booking within a specific time frame.

### Non-Functional Requirement:

- **Concurrency:** The system must handle multiple users trying to book the same seat simultaneously without any race
  conditions. This is a critical requirement.
- **Scalability:** The system should be able to handle a large number of users, movies and show across many cities.
- **Availability:** The booking service should be highly available, especially during peak times.
- **Maintainability:** It should be easy to add new theaters, new of movie listings, or new payment gateways.

---

## 2. Naive Solution: The "Starting Point"

A beginner might start with a single, massive class called BookingSystem or BookMyShow.

## The Thought Process:

"I'll have a big class that holds all the movies, all the theaters, and all the bookings. When a user wants to book a
ticket, I'll call a method on this single class to handle everything from checking seat availability to processing the
payment."

```java
class BookingSystem {
    private List<Movie> allMovies;
    private List<Theater> allTheaters;
    private List<Booking> allBookings;

    public void bookTicket(User user, Show show, List<Seat> seats) {
        // ... Check if seats are available
        // ... Mark seats as booked
        // ... Process payment
        // ... Create a booking
    }
}
```

### Limitations and Design Flaws:

This approach is fundamentally flawed for a system with these requirements, especially concurrency:

- **Violation of SOLID Principles:**
    - **Single Responsibility Principle (SRP):**  This class does everything. It's responsible for managing movies,
      theaters, users, shows, bookings and payment. Any change to one area risks breaking another.
- **Tight Coupling:** All components are tightly coupled within this one class. You can't reuse a `Theater` object
  without
  bringing along a entire `BookingSystem`.
- **Concurrency Nightmare:** The most critical flaw. A single shared BookingSystem object will be a massive bottleneck.
  If two users try to book the same seat at the same time, they will both be modifying the same shared data structure,
  leading to a race condition. Locking the entire `BookingSystem` object would destroy performance and scalability.
- **Lack of Scalability:** The `List` or `Map` data structures holding all movies, theaters, and bookings would become
  massive and unmanageable. The system would grind to a halt under a heavy load.

The naive solution is like having a single receptionist at a giant hotel. They have to do everything: check-ins,
check-outs, room service orders, and cleaning. It's impossible for this to scale.

---

## 3. Improved Solution: The "Mentor's Guidance"

The key to a good design is to separate concerns and handle concurrency at a granular level. We need to model the system
as a collection of specialized services and entities.

### The "Why": The Strategy, Singleton, and Facade Patterns

- **Repositories:** We'll use the Repository pattern to abstract the data storage layer. We will have separate
  repositories for `Movie`, `Theater`, `Show`, and `Booking`. This ensures that our business logic is independent of the
  data source (in-memory, database, etc.).
- **Seat Locking for Concurrency:** The core of the concurrency problem is the seat booking process. The solution is to
  use a **distributed locking mechanism**. When a user selects a seat, we don't book it immediately. We lock it for a
  brief period (e.g., 5-10 minutes). If the user completes the payment, we finalize the booking and release the lock. If
  they don't, the lock expires, and the seat becomes available again.
- **Payment Strategy:** The **Strategy pattern** is perfect for the payment process. We can have a `PaymentStrategy`
  interface with concrete implementations for credit card, UPI, or other methods. This allows us to easily add new
  payment methods.
- **Singleton for Managers:** We can use the **Singleton pattern** for managers that need to be globally accessible,
  like a BookingManager or TheaterManager.
- **Facade for the Booking Process:** The BookingService can act as a Facade. It provides a simplified, high-level
  interface (`bookTickets()`) that hides all the complexity of checking seat availability, handling the locking
  mechanism, and processing the payment.

### Core Classes and Their Interactions:

We will design a system of collaborating services, each with a single, clear responsibility.

1. **Entity Classes (Data Models):**
    - `Movie`: `movieId`, `name`, `duration`, `language`.
    - `Theater`: `theaterId`, `name`, `city`, `location`.
    - `Show`: `showId`, `movieId`, `screenId`, `showTime`, `seats`.
    - `Seat`: `seatId`, `row`, `column`, `status` (Available, Booked, Locked).
    - `Booking`: `bookingId`, `userId`, `showId`, `bookedSeats`, `bookingTime`, `status`.

2. **Repository Classes (Data Layer):**
    - `MovieRepository`, `TheaterRepository`, `ShowRepository`, `BookingRepository`. Each handles CRUD operations for
      its respective entity.

3. **Manager Classes (Service Layer):**
    - `TheaterManager`: Manages theaters and their associated shows.
    - `BookingManager` **(The Facade):** This is the core of our system. It's where the complex booking logic resides.
        - `bookTickets(userId, showId, selectedSeats):` The main method.
        - Internal Logic: It will acquire a lock on the selected seats, start a timer, and then delegate to a
          `PaymentService`.
    - `SeatLockProvider`: A specialized class that handles the distributed seat locking. It's crucial for concurrency.
4. **Strategy Pattern (Payment):**
    - `PaymentStrategy`: Interface with a` processPayment()` method.
    - `CreditCardPayment`, UpiPayment: Concrete implementations.
    - `PaymentService`: A class that uses a specific `PaymentStrategy` to process a payment.

---

## 4. Final Design Overview

Our final design is a multi-layered, concurrent-safe system.

- **Layered Architecture:**
    - **Data Layer:** The repositories handle all data persistence.
    - **Service Layer:** The managers and services contain the business logic.
    - **API/Controller Layer:** (Implicit) Handles user requests and calls the BookingManager.
- **Concurrency Handling:** The SeatLockProvider and the `BookingManager` are the core components that prevent race
  conditions. The process is:
    1. User selects seats.
    2. The `BookingManager` requests the `SeatLockProvider` to lock those specific seats.
    3. The `SeatLockProvider` ensures the lock is exclusive.
    4. The user is redirected to the payment page.
    5. If payment is successful, the `BookingManager` confirms the booking and releases the locks.
    6. If payment fails or times out, the locks are released, and the seats become available again.

This design is:

- **Highly Concurrent:** By locking only the specific seats, we allow other users to book tickets for the same show on
  different seats simultaneously.
- **Scalable:** The decoupled services and repositories allow us to scale individual components. We can have separate
  servers
  for the booking service, the movie service, etc.
- **Maintainable:** The clear separation of concerns makes it easy to add new features without breaking existing
  functionality.
