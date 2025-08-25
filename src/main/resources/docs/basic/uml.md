# 📊 UML Diagram Basics

**UML (Unified Modeling Language)** visualizes system designs, acting as blueprints for LLD.

### What is UML?

UML standardizes the representation of system components and interactions.

### Types of UML Diagrams

| UML Diagram          | Purpose                                               |
|----------------------|-------------------------------------------------------|
| **Class Diagram**    | Shows classes, attributes, methods, and relationships |
| **Sequence Diagram** | Shows flow of messages between objects over time      |
| **Use Case Diagram** | Shows user interaction with system functionalities    |
| **Activity Diagram** | Shows flow of actions, decisions, and concurrency     |

### Example: Class Diagram

```
+---------------------+
|      User           |
+---------------------+
| - name: String      |
| - email: String     |
+---------------------+
| + login(): void     |
| + logout(): void    |
+---------------------+
```

- `+` means public, `-` means private.
- Use arrows for relationships (e.g., `→` for association, `◄|–` for inheritance).

---

## 🔗 Object Relationships in LLD

Understanding class interactions is key to cohesive system design.

### Overview of Relationships

Classes relate through various patterns, defining how they collaborate.

### Types of Relationships

| Type        | Meaning                     | Example             | Lifespan    | Code Tip                  |
|-------------|-----------------------------|---------------------|-------------|---------------------------|
| Association | General connection          | `Student ↔ Teacher` | Independent | Object as a field         |
| Dependency  | Temporary usage             | `Order → Payment`   | Short       | Passed via method         |
| Aggregation | Whole-part (can live alone) | `Library has Books` | Independent | Has-a relationship        |
| Composition | Whole-part (dies together)  | `Human has Heart`   | Dependent   | Initialize in constructor |

### Code Examples: Relationships

#### Association

```java
class Student {
    String name;
}

class Course {
    Student student; // Course has a Student
}
```

#### 🧩 Dependency (uses)

```java
class OrderService {
    void placeOrder(Payment payment) {
        payment.pay(); // Temporary use of Payment
    }
}
```

#### ⚪ Aggregation (whole-part, weak)

```java
class Book {
    String title;
}

class Library {
    List<Book> books; // Books can exist without Library
}
```

#### ⚫ Composition (whole-part, strong)

```java
class Heart {
    void beat() { /* ... */ }
}

class Human {
    private final Heart heart = new Heart(); // Heart dies with Human
}
```