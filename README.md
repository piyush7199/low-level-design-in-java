# Low-Level Design (LLD) in Java 🚀

Welcome to the **Low-Level Design in Java** repository!  
This project is a collection of **fundamental LLD concepts, design patterns, and practice problems** implemented in
Java.  
It serves as a hands-on guide for mastering **object-oriented principles, UML, SOLID, and real-world LLD case studies**.

---

## 📖 What is Low-Level Design (LLD)?

Low-Level Design (LLD) is the process of converting high-level solutions into **detailed class diagrams, interactions,
and actual code structures**.  
It ensures that the **system is modular, extensible, and maintainable** by applying **OOP principles, SOLID, and Design
Patterns**.

---

## 🎯 Why this Repository?

- 📘 Learn **OOP, UML, SOLID, KISS, DRY, YAGNI** with examples
- 🔹 Explore **Design Patterns** (Creational, Structural, Behavioral) in Java
- 🛠 Practice **real-world case studies** (e.g., Parking Lot) with **naive → improved solutions**
- 🧩 Build a **strong foundation** for interviews and scalable system design

---

## 📂 Repository Structure

```
low-level-design-in-java/
│── pom.xml
│── README.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/lld/
│   │   │       ├── basic/
│   │   │       ├── patterns/
│   │   │       └── practice/
│   │   │
│   │   └── resources/
│   │       └── docs/
│   │           └── basic/
│   │               └── solid.md
│   │
│   └── test/java/
```

---

## 📘 Basics (Documentation)

- [OOP Concepts](src/main/resources/docs/basic/oop.md)
- [SOLID Principles](src/main/resources/docs/basic/solid.md)
- [UML Basics](src/main/resources/docs/basic/uml.md)

---

## 🔹 Design Patterns (Code)

### 🔨 Creational Design Patterns

| # | Pattern Name                                                                    | Description                                                                                                            |
|---|---------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| 1 | [Singleton](./src/main/java/org/lld/patterns/creational/singleton)              | Ensure a class has only one instance and provide a global point of access to it.                                       |
| 2 | [Builder](./src/main/java/org/lld/patterns/creational/builder)                  | Separates complex object construction from its representation.                                                         |
| 3 | [Factory](./src/main/java/org/lld/patterns/creational/factory)                  | Creates objects without exposing the instantiation logic.                                                              |
| 4 | [Prototype](./src/main/java/org/lld/patterns/creational/prototype)              | Create new objects by copying existing ones, reducing the cost of creation.                                            |
| 5 | [Abstract Factory](./src/main/java/org/lld/patterns/creational/abstractFactory) | Provides an interface for creating families of related or dependent objects without specifying their concrete classes. |

### 🧱 Structural Design Patterns

| # | Pattern Name                                                       | Description                                                                 |
|---|--------------------------------------------------------------------|-----------------------------------------------------------------------------|
| 1 | [Decorator](./src/main/java/org/lld/patterns/structural/decorator) | Dynamically adds new behavior to objects at runtime.                        |
| 2 | [Flyweight](./src/main/java/org/lld/patterns/structural/flyweight) | Reduces memory usage by sharing common parts of object state among objects. |
| 3 | [Adapter](./src/main/java/org/lld/patterns/structural/adapter)     | Converts one interface into another expected by the client.                 |

### 🧠 Behavioural Design Patterns

| # | Pattern Name                                                                                  | Description                                                                                                                        |
|---|-----------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| 1 | [Chain Of Responsibility](./src/main/java/org/lld/patterns/behavioural/chainOfResponsibility) | Passes a request along a chain of handlers until one of them handles it.                                                           |
| 2 | [Observer](./src/main/java/org/lld/patterns/behavioural/observer)                             | Defines a one-to-many dependency so that when one object changes state, all its dependents are notified and updated automatically. |
| 3 | [Strategy](./src/main/java/org/lld/patterns/behavioural/strategy)                             | Enables selecting an algorithm's behavior at runtime by encapsulating it within a class and making it interchangeable.             |
| 4 | [Command](./src/main/java/org/lld/patterns/behavioural/command)                               | Encapsulates a request as an object, thereby allowing users to parameterize clients, delay execution, or queue and log operations. |
| 5 | [State](./src/main/java/org/lld/patterns/behavioural/state)                                   | Allows an object to change its behavior when its internal state changes, appearing as if it changed its class.                     |

---

## 🛠 Practice Problems

| # | System Design Problem                                                                              |
|---|----------------------------------------------------------------------------------------------------|
| 1 | [Car Rental System](./src/main/java/org/lld/practice/design_car_rental_system)                     |
| 2 | [Parking Lot System](./src/main/java/org/lld/practice/design_parking_lot_system)                   |
| 3 | [Vending Machine](./src/main/java/org/lld/practice/design_vending_machine)                         |
| 4 | [Snake And Ladder](./src/main/java/org/lld/practice/design_snake_and_ladder)                       |
| 5 | [Online Movie Booking System](./src/main/java/org/lld/practice/design_movie_ticket_booking_system) |

---

## 🧑‍💻 Contribution

Feel free to contribute:

- Add new design patterns
- Improve practice solutions
- Enhance documentation

---

## 🚀 Getting Started

Clone the repo

   ```bash
   git clone https://github.com/your-userōname/low-level-design-in-java.git
   cd low-level-design-in-java
   ```

---

## ⭐ Acknowledgements

This repository is inspired by **real-world interview prep & scalable design needs**.  
If you find this useful, please ⭐ star the repo and share it with others!
