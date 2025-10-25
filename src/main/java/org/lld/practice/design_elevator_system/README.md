# Design An Elevator System

## 1. Problem Statement and Requirements

Our goal is to design a software system for managing elevators in a multi-story building.

### Functional Requirements:

- **Request Handling:** Handle both external requests (floor buttons) and internal requests (cabin buttons).
- **Direction Management:** Elevators should move efficiently, serving requests in their current direction first.
- **Multiple Elevators:** Support multiple elevators working independently but coordinated.
- **Door Operations:** Handle door opening/closing with safety checks.
- **Capacity Management:** Track and enforce maximum weight/passenger capacity.
- **Floor Selection:** Allow passengers to select destination floors.
- **Status Display:** Show current floor, direction, and status of each elevator.
- **Emergency Mode:** Handle emergency situations (fire alarm, maintenance).

### Non-Functional Requirements:

- **Efficiency:** Minimize average wait time and travel time for passengers.
- **Scalability:** Support buildings with many floors and multiple elevators.
- **Safety:** Ensure safe operations (door safety, overload detection).
- **Maintainability:** Easy to modify scheduling algorithms or add new features.
- **Concurrency:** Handle multiple simultaneous requests from different floors.

---

## 2. Naive Solution: The "Starting Point"

A beginner might create a single `Elevator` class handling all logic.

### The Thought Process:

"An elevator moves between floors and responds to button presses." This leads to a simple but inefficient design.

```java
class Elevator {
    private int currentFloor;
    private Direction direction;
    private List<Integer> requests;
    
    public void requestFloor(int floor) {
        requests.add(floor);
        // ... move to floor ...
    }
    
    public void moveToNextFloor() {
        // ... simple movement logic ...
    }
    
    public void openDoor() {
        // ... open door ...
    }
    
    public void closeDoor() {
        // ... close door ...
    }
}
```

### Limitations and Design Flaws:

- **No Separation of Concerns:** Door operations, movement logic, and request handling are all mixed together.
- **Inefficient Scheduling:** Requests are served in order received, not optimally.
- **No Multi-Elevator Support:** Can't coordinate multiple elevators efficiently.
- **Tight Coupling:** All components are tightly coupled, making testing and modification difficult.
- **No State Management:** Elevator states (moving, idle, doors opening) are not explicitly modeled.
- **Poor Concurrency:** Multiple requests could cause race conditions.

---

## 3. Improved Solution: The "Mentor's Guidance"

The key is to model the elevator system with clear separation of concerns and proper scheduling.

### The "Why": Key Design Patterns

- **State Pattern:** An elevator has distinct states (idle, moving up, moving down, doors opening, doors closing, maintenance). Each state has different valid operations.
- **Strategy Pattern:** Different scheduling algorithms (FCFS, SCAN, LOOK) can be implemented as strategies for optimal elevator dispatch.
- **Observer Pattern:** Floors and displays observe elevator state changes to update their status.
- **Command Pattern:** Each floor request or button press can be encapsulated as a command for better queuing and processing.

### Core Classes and Their Interactions:

We'll create a well-structured, modular system:

1. **`Elevator` (The Core Unit):**
    - Represents a single elevator car.
    - Maintains current state, floor, direction, and capacity.
    - Delegates state-specific behavior to state objects.

2. **`ElevatorState` Interface (The State):**
    - Defines contracts for different elevator states.
    - Implementations: `IdleState`, `MovingUpState`, `MovingDownState`, `DoorsOpeningState`, `DoorsClosingState`.

3. **`ElevatorController` (The Brain):**
    - Manages a single elevator's operation.
    - Processes requests and determines next actions.
    - Maintains request queues (up and down).

4. **`ElevatorSystem` (The Orchestrator):**
    - Manages multiple elevators.
    - Dispatches requests to the most appropriate elevator.
    - Uses a scheduling strategy to optimize performance.

5. **`Request` (The Command):**
    - Encapsulates a floor request with direction.
    - Can be external (from floor) or internal (from cabin).

6. **`SchedulingStrategy` Interface:**
    - Defines how to select the best elevator for a request.
    - Implementations: `NearestCarStrategy`, `DirectionBasedStrategy`.

7. **`Door` (The Safety Component):**
    - Manages door operations with safety checks.
    - Handles opening, closing, and obstruction detection.

8. **`Display` (The UI Component):**
    - Shows elevator status on each floor and inside cabin.
    - Observes elevator state changes.

9. **`Building` (The Container):**
    - Represents the building with floors and elevators.
    - Provides configuration (total floors, elevator count).

---

## 4. Final Design Overview

Our final design is a sophisticated, efficient system:

* The `Elevator` uses State pattern to manage its behavior clearly and safely.
* The `ElevatorController` encapsulates the logic for a single elevator's operation.
* The `ElevatorSystem` coordinates multiple elevators using scheduling strategies.
* The `SchedulingStrategy` allows for different optimization approaches without changing core code.
* The `Request` command pattern allows for easy queuing and processing.
* The `Door` component handles safety concerns independently.

This design is:

- **More Efficient:** Scheduling strategies minimize wait times. Elevators serve requests in their current direction first.
- **More Maintainable:** Adding a new scheduling algorithm only requires implementing the `SchedulingStrategy` interface.
- **Better State Management:** The State pattern prevents invalid operations (e.g., opening doors while moving).
- **Scalable:** Easy to add more elevators or floors without changing existing code.
- **Thread-Safe:** Proper synchronization for concurrent request handling.
- **Testable:** Each component can be tested independently.

### Key Design Patterns Used:

- **State Pattern:** For elevator states
- **Strategy Pattern:** For scheduling algorithms
- **Observer Pattern:** For status displays
- **Command Pattern:** For request handling
- **Singleton Pattern:** For the elevator system controller
- **Facade Pattern:** `ElevatorSystem` provides simplified interface

### Scheduling Strategies:

1. **Nearest Car:** Assign request to closest idle elevator
2. **Direction-Based:** Assign to elevator already moving in that direction
3. **SCAN (Elevator Algorithm):** Move in one direction serving all requests, then reverse
4. **LOOK:** Like SCAN but only goes as far as the last request in each direction

