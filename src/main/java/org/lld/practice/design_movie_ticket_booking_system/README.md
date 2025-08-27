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