# 🧠 SOLID Principles

**SOLID principles** ensure clean, maintainable, and scalable code.

### Introduction to SOLID

SOLID is a set of five guidelines for robust OOP design.

| Principle                     | Meaning                                     | Simple Explanation                                                 |
|-------------------------------|---------------------------------------------|--------------------------------------------------------------------|
| **S** – Single Responsibility | One class, one job                          | A class shouldn’t handle unrelated tasks (e.g., User vs. Emailer). |
| **O** – Open/Closed           | Open to extend, closed to modify            | Extend behavior via interfaces, don’t modify code.                 |
| **L** – Liskov Substitution   | Subtypes act like their parent              | Subclasses should be swappable without breaking functionality.     |
| **I** – Interface Segregation | Don’t force unused methods                  | Use small, specific interfaces, not bloated ones.                  |
| **D** – Dependency Inversion  | Depend on abstractions, not implementations | Use interfaces to decouple high-level and low-level modules.       |

### Practical Applications

- Single Responsibility: Split User from email-sending logic.
- Open/Closed: Use a Payment interface to add new payment methods.

---

## 🏗️ Design Patterns Overview

Design patterns are reusable solutions to common design problems.

### What are Design Patterns?

Patterns provide tested approaches to structure code effectively.

### Categories of Design Patterns

### Creational Patterns

Control object creation to reduce complexity.

| Pattern          | Purpose                                |
|------------------|----------------------------------------|
| Singleton        | Ensures only one instance exists       |
| Factory          | Creates objects without exposing logic |
| Builder          | Builds complex objects step-by-step    |
| Prototype        | Clone existing object                  |
| Abstract Factory | Create families of related objects     |

### Structural Patterns

Organize classes and objects into larger structures.

| Pattern   | Purpose                                  |
|-----------|------------------------------------------|
| Adapter   | Converts one interface into another      |
| Decorator | Add functionality dynamically            |
| Composite | Tree structure of objects                |
| Proxy     | Stand-in for another object              |
| Bridge    | Decouple abstraction from implementation |
| Flyweight | Share objects to save memory             |
| Facade    | Simplified interface to a complex system |

### Behavioral Patterns

Handle object communication and responsibilities.

| Pattern                 | Purpose                                   |
|-------------------------|-------------------------------------------|
| Strategy                | Switch behavior at runtime                |
| Observer                | Notify dependents on change               |
| Command                 | Encapsulate a request as an object        |
| State                   | Object changes behavior by internal state |
| Template                | Define algorithm skeleton                 |
| Chain of Responsibility | Pass request along a chain                |
| Mediator                | Central controller for interactions       |
| Memento                 | Save/restore object state                 |
| Visitor                 | Add operations without modifying classes  |
| Interpreter             | Interpret expressions based on grammar    |

### When to Use Design Patterns

- Singleton: Single database connection.
- Factory: Create objects based on user input.
- Observer: Event-driven systems (e.g., UI updates).
