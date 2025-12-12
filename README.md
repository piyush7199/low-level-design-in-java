# Low-Level Design (LLD) in Java 🚀

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Design Patterns](https://img.shields.io/badge/Design%20Patterns-23-blue.svg)](https://en.wikipedia.org/wiki/Software_design_pattern)
[![Interview Ready](https://img.shields.io/badge/Interview-Ready-success.svg)](https://github.com)

Welcome to the **most comprehensive Low-Level Design resource** for Java developers and interview candidates!  

This repository contains **13 design patterns**, **27 real-world system designs**, and **detailed explanations** of OOP principles, SOLID, and software architecture best practices.

Perfect for:
- 🎯 **SDE Interview Preparation** (Amazon, Google, Microsoft, etc.)
- 💼 **Software Engineers** learning system design
- 🎓 **Computer Science Students** mastering OOP and design patterns
- 👨‍💻 **Developers** building scalable, maintainable systems

---

## 📖 What is Low-Level Design (LLD)?

**Low-Level Design** is the detailed design phase where you convert high-level architecture into:
- ✅ **Class diagrams** and object relationships
- ✅ **Detailed code structures** with proper abstractions
- ✅ **Design patterns** for common problems
- ✅ **SOLID principles** for maintainability

**Why LLD matters in interviews:**
- 60% of system design interviews include LLD rounds
- Tests your ability to write clean, extensible code
- Demonstrates understanding of OOP and design patterns
- Shows real-world problem-solving skills

---

## 🎯 Why This Repository?

### For Interview Candidates:
- ✅ **Real interview questions** from FAANG companies
- ✅ **Naive vs Improved** solutions showing evolution
- ✅ **Pattern identification** - learn when to use which pattern
- ✅ **Common pitfalls** and how to avoid them

### For Learning:
- 📘 **Comprehensive documentation** for every concept
- 🔹 **13 Design Patterns** with real-world use cases
- 🛠 **27 Complete Systems** from simple to complex
- 🧩 **Progressive difficulty** - start simple, master complex
- 📊 **UML diagrams** for visual understanding

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

## 📘 Documentation & Guides

### Core Concepts
- 📖 [OOP Concepts](src/main/resources/docs/basic/oop.md) - Four pillars explained
- 🎯 [SOLID Principles](src/main/resources/docs/basic/solid.md) - With examples
- 📐 [UML Basics](src/main/resources/docs/basic/uml.md) - Class diagrams

### Interview Preparation
- 🎯 **[INTERVIEW_GUIDE.md](./INTERVIEW_GUIDE.md)** - Complete interview strategy ⭐ Must Read
- 📝 **[INTERVIEW_CHEATSHEET.md](./INTERVIEW_CHEATSHEET.md)** - Quick reference before interview
- 🎨 **[PATTERNS_GUIDE.md](./PATTERNS_GUIDE.md)** - All design patterns explained
- ⚡ **[BEST_PRACTICES.md](./BEST_PRACTICES.md)** - Code quality guidelines

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

Each problem includes:
- ✅ **Naive Solution**: Simple implementation showing common pitfalls
- ✅ **Improved Solution**: Well-designed implementation using SOLID principles and design patterns
- ✅ **Comprehensive README**: Problem statement, requirements, design considerations, and patterns used

| #  | System Design Problem                                                                                      | Key Patterns Used                                    |
|----|-----------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| 1  | [Car Rental System](./src/main/java/org/lld/practice/design_car_rental_system)                            | Strategy, State, Factory                             |
| 2  | [Parking Lot System](./src/main/java/org/lld/practice/design_parking_lot_system)                          | Singleton, Strategy                                  |
| 3  | [Vending Machine](./src/main/java/org/lld/practice/design_vending_machine)                                | State, Strategy                                      |
| 4  | [Snake And Ladder](./src/main/java/org/lld/practice/design_snake_and_ladder)                              | Factory, Strategy                                    |
| 5  | [Online Movie Booking System](./src/main/java/org/lld/practice/design_movie_ticket_booking_system)        | Singleton, Factory, Strategy                         |
| 6  | [Online Hotel Booking System](./src/main/java/org/lld/practice/design_online_hotel_booking_system)        | Strategy, Observer, Factory                          |
| 7  | [ATM System](./src/main/java/org/lld/practice/design_atm_system)                                          | State, Strategy, Command, Singleton                  |
| 8  | [Library Management System](./src/main/java/org/lld/practice/design_library_management_system)            | Strategy, Observer, Repository, Facade               |
| 9  | [Elevator System](./src/main/java/org/lld/practice/design_elevator_system)                                | State, Strategy, Observer, Command                   |
| 10 | [Food Delivery System](./src/main/java/org/lld/practice/design_food_delivery_system)                      | Strategy, State, Observer, Factory                   |
| 11 | [Ride-Sharing System](./src/main/java/org/lld/practice/design_ride_sharing_system)                        | Strategy, State, Observer, Factory, Singleton        |
| 12 | [Queue Management System](./src/main/java/org/lld/practice/design_queue_management_system)                | Singleton, Strategy, Observer, State                 |
| 13 | [Cache Management System](./src/main/java/org/lld/practice/design_cache_management_system)                | Strategy, Factory, Decorator, Builder                |
| 14 | [Rate Limiter](./src/main/java/org/lld/practice/design_rate_limiter)                                       | Strategy, Factory, Builder                           |
| 15 | [Matching Engine](./src/main/java/org/lld/practice/design_matching_engine)                                 | Strategy, Observer                                   |
| 16 | [Wallet / Ledger System](./src/main/java/org/lld/practice/design_wallet_system)                            | Command, Observer, Builder                           |
| 17 | [Pub/Sub System (Mini-Kafka)](./src/main/java/org/lld/practice/design_pub_sub_system)                      | Observer, Strategy, Singleton                        |
| 18 | [Task Scheduler](./src/main/java/org/lld/practice/design_task_scheduler)                                   | Strategy, Command, Observer, Singleton               |
| 19 | [E-commerce Cart System](./src/main/java/org/lld/practice/design_ecommerce_cart)                           | Strategy, Decorator, Factory, Builder                |
| 20 | [Logger System](./src/main/java/org/lld/practice/design_logger_system)                                      | Strategy, Observer, Factory, Chain of Responsibility |
| 21 | [Notification System](./src/main/java/org/lld/practice/design_notification_system)                        | Strategy, Factory, Observer, Command                  |
| 22 | [URL Shortener (TinyURL)](./src/main/java/org/lld/practice/design_url_shortener)                          | Strategy, Factory, Repository                        |
| 23 | [Chat Application](./src/main/java/org/lld/practice/design_chat_application)                              | Observer, Strategy, State, Repository                |
| 24 | [Leaderboard System](./src/main/java/org/lld/practice/design_leaderboard_system)                          | Strategy, Factory, Observer                          |
| 25 | [Calendar/Event Booking System](./src/main/java/org/lld/practice/design_calendar_event_booking)          | Strategy, Factory, Observer, State, Repository        |
| 26 | [Expense Sharing System (Splitwise-like)](./src/main/java/org/lld/practice/design_expense_sharing_system)  | Strategy, Factory, Graph Algorithm                     |
| 27 | [Tic-Tac-Toe Game](./src/main/java/org/lld/practice/design_tic_tac_toe)                                    | Strategy, State, Factory, Observer                    |


---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven (for building)
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

### Quick Start

1. **Clone the repository**
```bash
git clone https://github.com/your-username/low-level-design-in-java.git
cd low-level-design-in-java
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run examples**
```bash
# Run a specific pattern example
mvn exec:java -Dexec.mainClass="org.lld.patterns.creational.singleton.Main"

# Run a practice problem
mvn exec:java -Dexec.mainClass="org.lld.practice.design_parking_lot_system.improved_solution.Main"
```

### Project Structure
```
src/main/java/org/lld/
├── patterns/           # Design pattern implementations
│   ├── creational/    # Factory, Singleton, Builder, etc.
│   ├── structural/    # Adapter, Decorator, Proxy, etc.
│   └── behavioural/   # Observer, Strategy, State, etc.
│
├── practice/          # Real-world system designs
│   ├── design_parking_lot_system/
│   ├── design_atm_system/
│   └── ...
│
└── resources/docs/    # Documentation
    └── basic/         # OOP, SOLID, UML guides
```

---

## 📚 Learning Path

### For Beginners (Start Here):
1. Read [OOP Concepts](src/main/resources/docs/basic/oop.md)
2. Understand [SOLID Principles](src/main/resources/docs/basic/solid.md)
3. Start with simple patterns:
   - [Singleton](./src/main/java/org/lld/patterns/creational/singleton)
   - [Factory](./src/main/java/org/lld/patterns/creational/factory)
   - [Strategy](./src/main/java/org/lld/patterns/behavioural/strategy)
4. Try simple problems:
   - [Vending Machine](./src/main/java/org/lld/practice/design_vending_machine)
   - [Snake and Ladder](./src/main/java/org/lld/practice/design_snake_and_ladder)

### For Intermediate (Building Skills):
5. Study structural patterns (Decorator, Adapter, Flyweight)
6. Master behavioral patterns (Observer, State, Command)
7. Practice medium problems:
   - [Parking Lot System](./src/main/java/org/lld/practice/design_parking_lot_system)
   - [Library Management](./src/main/java/org/lld/practice/design_library_management_system)
   - [Elevator System](./src/main/java/org/lld/practice/design_elevator_system)

### For Advanced (Interview Ready):
8. Combine multiple patterns in complex systems
9. Practice with real-world systems:
   - [ATM System](./src/main/java/org/lld/practice/design_atm_system)
   - [Food Delivery System](./src/main/java/org/lld/practice/design_food_delivery_system)
   - [Ride-Sharing System](./src/main/java/org/lld/practice/design_ride_sharing_system)
10. Review [INTERVIEW_GUIDE.md](./INTERVIEW_GUIDE.md) for tips

---

## 💡 Interview Tips

### Common LLD Interview Questions:
- Design a Parking Lot System ⭐ Most Common
- Design an Elevator System
- Design a Library Management System
- Design a Food Delivery App (Uber Eats)
- Design a Ride-Sharing App (Uber)
- Design a Hotel Booking System
- Design a Movie Ticket Booking System
- Design a Vending Machine
- Design an ATM
- Design a Car Rental System

### What Interviewers Look For:
1. ✅ **Requirements gathering** - Ask clarifying questions
2. ✅ **Core entities identification** - Find main objects
3. ✅ **Relationships** - Understand interactions
4. ✅ **Design patterns usage** - Apply appropriately
5. ✅ **SOLID principles** - Write clean code
6. ✅ **Extensibility** - Design for future changes
7. ✅ **Trade-offs** - Discuss pros/cons of your decisions

### How to Use This Repo for Interviews:
1. **First:** Try solving the problem yourself (30 mins)
2. **Then:** Look at the naive solution to see common mistakes
3. **Finally:** Study the improved solution to learn best practices
4. **Practice:** Implement it from scratch without looking
5. **Explain:** Practice explaining your design out loud

See [INTERVIEW_GUIDE.md](./INTERVIEW_GUIDE.md) for detailed tips and strategies.

---

## 🔥 Key Design Patterns Quick Reference

| Pattern | Use When | Example From Problems |
|---------|----------|----------------------|
| **Singleton** | Need exactly one instance | ParkingLot, BankingService |
| **Factory** | Object creation varies | Vehicle types, User types |
| **Builder** | Complex object construction | Building Car with options |
| **Strategy** | Algorithm varies at runtime | Pricing, Matching, Payment |
| **State** | Behavior changes with state | Order lifecycle, Ride status |
| **Observer** | One-to-many notifications | Order updates, Ride tracking |
| **Decorator** | Add responsibilities dynamically | Coffee with add-ons |
| **Adapter** | Interface compatibility | Legacy system integration |
| **Command** | Encapsulate requests | Transaction operations |
| **Repository** | Data access abstraction | BookCatalog, MemberRegistry |


---

## 🎓 Additional Resources

- [Design Patterns Book](https://en.wikipedia.org/wiki/Design_Patterns) - Gang of Four
- [Effective Java](https://www.oreilly.com/library/view/effective-java/9780134686097/) - Joshua Bloch
- [Clean Code](https://www.oreilly.com/library/view/clean-code-a/9780136083238/) - Robert Martin
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Refactoring Guru](https://refactoring.guru/design-patterns) - Visual pattern guide

---

## 🤝 Contributing

We welcome contributions! Here's how:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/new-pattern`)
3. **Commit** your changes (`git commit -am 'Add new pattern'`)
4. **Push** to the branch (`git push origin feature/new-pattern`)
5. **Create** a Pull Request

### Contribution Ideas:
- 🆕 Add new design patterns (Proxy, Bridge, Composite)
- 📝 Improve documentation and examples
- 🐛 Fix bugs or improve existing solutions
- 🎨 Add UML diagrams
- ✅ Add unit tests
- 🌍 Add translations

---

## 📈 Repository Stats

- **Design Patterns:** 13 implemented
- **Practice Problems:** 27 complete systems
- **Code Examples:** 350+ files
- **Lines of Code:** 15000+
- **Documentation:** Comprehensive READMEs for each topic

---

## ⭐ Show Your Support

If this repository helped you:
- ⭐ **Star** the repository
- 🔀 **Fork** it for your own learning
- 📢 **Share** with friends and colleagues
- 💬 **Feedback** - open an issue with suggestions

---

## 📧 Contact & Support

- **Issues:** [GitHub Issues](https://github.com/your-username/low-level-design-in-java/issues)
- **Discussions:** [GitHub Discussions](https://github.com/your-username/low-level-design-in-java/discussions)

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgements

Created with ❤️ for the developer community.

Special thanks to:
- Gang of Four for Design Patterns
- Robert Martin for SOLID principles
- All contributors and supporters

**Remember:** Good design is not about following rules blindly, but understanding trade-offs and making informed decisions!

Happy Learning and Good Luck with your interviews! 🚀
