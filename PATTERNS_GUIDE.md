# Design Patterns Quick Reference Guide 📚

## Table of Contents
- [What are Design Patterns?](#what-are-design-patterns)
- [Creational Patterns](#creational-patterns)
- [Structural Patterns](#structural-patterns)
- [Behavioral Patterns](#behavioral-patterns)
- [Pattern Selection Guide](#pattern-selection-guide)
- [Common Combinations](#common-combinations)
- [Anti-Patterns to Avoid](#anti-patterns-to-avoid)

---

## What are Design Patterns?

**Design patterns** are reusable solutions to common software design problems. They represent best practices refined by experienced developers over time.

### Benefits:
✅ **Proven solutions** - tested in real-world applications  
✅ **Common vocabulary** - communicate designs effectively  
✅ **Accelerate development** - don't reinvent the wheel  
✅ **Improve maintainability** - well-structured code  
✅ **Facilitate refactoring** - cleaner code evolution  

### Categories:
1. **Creational** - Object creation mechanisms
2. **Structural** - Object composition and relationships
3. **Behavioral** - Communication between objects

---

## Creational Patterns

### 1. Singleton Pattern 🔒

**Intent:** Ensure a class has only one instance and provide global access to it.

**When to Use:**
- Database connections
- Configuration managers
- Logging services
- Thread pools
- Caching

**Real-World Examples:**
- Database connection pool
- Application configuration
- Printer spooler

**Code Example:**
```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        // Private constructor
        connection = createConnection();
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
}
```

**Pros:**
✅ Controlled access to sole instance  
✅ Reduced namespace pollution  
✅ Lazy initialization possible  

**Cons:**
❌ Difficult to unit test  
❌ Can hide dependencies  
❌ Potential threading issues  

**In This Repo:** [Singleton Implementation](./src/main/java/org/lld/patterns/creational/singleton)

---

### 2. Factory Pattern 🏭

**Intent:** Create objects without exposing creation logic.

**When to Use:**
- Object type determined at runtime
- Centralized object creation
- Complex object creation logic

**Real-World Examples:**
- Vehicle factory (Car, Truck, Motorcycle)
- Document creator (PDF, Word, Excel)
- Payment method (Credit Card, PayPal, Bitcoin)

**Code Example:**
```java
public interface Vehicle {
    void drive();
}

public class Car implements Vehicle {
    public void drive() { System.out.println("Driving car"); }
}

public class Truck implements Vehicle {
    public void drive() { System.out.println("Driving truck"); }
}

public class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        switch(type) {
            case "car": return new Car();
            case "truck": return new Truck();
            default: throw new IllegalArgumentException();
        }
    }
}
```

**Pros:**
✅ Loose coupling  
✅ Easy to extend  
✅ Centralized creation logic  

**Cons:**
❌ Can become complex  
❌ May require many subclasses  

**In This Repo:** [Factory Implementation](./src/main/java/org/lld/patterns/creational/factory)

---

### 3. Builder Pattern 🔨

**Intent:** Separate complex object construction from representation.

**When to Use:**
- Object has many parameters (especially optional)
- Step-by-step construction needed
- Different representations of same object

**Real-World Examples:**
- Building a Car with options
- Creating a House
- Constructing a Pizza with toppings

**Code Example:**
```java
public class Pizza {
    private String size;
    private boolean cheese;
    private boolean pepperoni;
    private boolean bacon;
    
    private Pizza(PizzaBuilder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.bacon = builder.bacon;
    }
    
    public static class PizzaBuilder {
        private String size;
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean bacon = false;
        
        public PizzaBuilder(String size) {
            this.size = size;
        }
        
        public PizzaBuilder addCheese() {
            this.cheese = true;
            return this;
        }
        
        public PizzaBuilder addPepperoni() {
            this.pepperoni = true;
            return this;
        }
        
        public Pizza build() {
            return new Pizza(this);
        }
    }
}

// Usage
Pizza pizza = new Pizza.PizzaBuilder("large")
    .addCheese()
    .addPepperoni()
    .build();
```

**Pros:**
✅ Immutable objects  
✅ Readable code  
✅ Different representations  

**Cons:**
❌ More code  
❌ May be overkill for simple objects  

**In This Repo:** [Builder Implementation](./src/main/java/org/lld/patterns/creational/builder)

---

### 4. Prototype Pattern 📋

**Intent:** Create new objects by cloning existing ones.

**When to Use:**
- Object creation is expensive
- Need similar objects with slight variations
- Hide complexity of creating new instances

**Real-World Examples:**
- Cloning database records
- Game character templates
- Document templates

**Pros:**
✅ Performance boost  
✅ Reduced subclassing  

**Cons:**
❌ Deep vs shallow copy complexity  
❌ Circular references issues  

**In This Repo:** [Prototype Implementation](./src/main/java/org/lld/patterns/creational/prototype)

---

### 5. Abstract Factory Pattern 🏢

**Intent:** Create families of related objects without specifying concrete classes.

**When to Use:**
- System should be independent of object creation
- Need families of related objects
- Want to provide library of products

**Real-World Examples:**
- UI frameworks (Windows/Mac buttons, textboxes)
- Database drivers (MySQL, PostgreSQL)
- Cross-platform GUI toolkits

**Pros:**
✅ Isolates concrete classes  
✅ Easy to exchange product families  
✅ Consistency among products  

**Cons:**
❌ Difficult to add new products  
❌ Increased complexity  

**In This Repo:** [Abstract Factory Implementation](./src/main/java/org/lld/patterns/creational/abstractFactory)

---

## Structural Patterns

### 1. Adapter Pattern 🔌

**Intent:** Convert interface of a class into another interface clients expect.

**When to Use:**
- Want to use existing class with incompatible interface
- Integrate legacy code with new code
- Use third-party library with different interface

**Real-World Examples:**
- Power adapter (110V to 220V)
- Media player adapters
- Legacy system integration

**Code Example:**
```java
// Existing interface
interface MediaPlayer {
    void play(String audioType, String fileName);
}

// Incompatible interface
interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}

// Adapter
class MediaAdapter implements MediaPlayer {
    AdvancedMediaPlayer advancedPlayer;
    
    public MediaAdapter(String audioType) {
        if(audioType.equals("vlc")) {
            advancedPlayer = new VlcPlayer();
        } else if (audioType.equals("mp4")) {
            advancedPlayer = new Mp4Player();
        }
    }
    
    public void play(String audioType, String fileName) {
        if(audioType.equals("vlc")) {
            advancedPlayer.playVlc(fileName);
        } else if(audioType.equals("mp4")) {
            advancedPlayer.playMp4(fileName);
        }
    }
}
```

**Pros:**
✅ Reuse existing code  
✅ Single Responsibility Principle  
✅ Flexible integration  

**Cons:**
❌ Increased complexity  
❌ Performance overhead  

**In This Repo:** [Adapter Implementation](./src/main/java/org/lld/patterns/structural/adapter)

---

### 2. Decorator Pattern 🎨

**Intent:** Add new functionality to objects dynamically without altering structure.

**When to Use:**
- Add responsibilities to objects dynamically
- Responsibilities can be withdrawn
- Extension by subclassing is impractical

**Real-World Examples:**
- Coffee with add-ons (milk, sugar, whipped cream)
- Pizza with toppings
- Text formatting (bold, italic, underline)

**Code Example:**
```java
interface Coffee {
    double cost();
    String description();
}

class SimpleCoffee implements Coffee {
    public double cost() { return 5.0; }
    public String description() { return "Simple Coffee"; }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double cost() {
        return coffee.cost() + 1.5;
    }
    
    public String description() {
        return coffee.description() + ", Milk";
    }
}

// Usage
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new SugarDecorator(coffee);
```

**Pros:**
✅ Add functionality without inheritance  
✅ Flexible combinations  
✅ Single Responsibility Principle  

**Cons:**
❌ Many small objects  
❌ Can be complex  

**In This Repo:** [Decorator Implementation](./src/main/java/org/lld/patterns/structural/decorator)

---

### 3. Flyweight Pattern 🪶

**Intent:** Minimize memory usage by sharing common data among objects.

**When to Use:**
- Large number of similar objects
- Most object state can be made extrinsic
- Identity of objects not important

**Real-World Examples:**
- Text editor characters
- Game objects (trees, bullets)
- String pool in Java

**Pros:**
✅ Memory savings  
✅ Performance improvement  

**Cons:**
❌ Complexity increase  
❌ Slower object creation  

**In This Repo:** [Flyweight Implementation](./src/main/java/org/lld/patterns/structural/flyweight)

---

## Behavioral Patterns

### 1. Strategy Pattern 🎯

**Intent:** Define family of algorithms, encapsulate each, make them interchangeable.

**When to Use:**
- Multiple algorithms for specific task
- Want to switch algorithms at runtime
- Avoid conditional statements for algorithm selection

**Real-World Examples:**
- Payment methods (Credit Card, PayPal, Bitcoin)
- Sorting algorithms (QuickSort, MergeSort)
- Compression algorithms (ZIP, RAR, TAR)

**Code Example:**
```java
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardStrategy implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class PayPalStrategy implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }
    
    public void checkout(double amount) {
        paymentStrategy.pay(amount);
    }
}
```

**Pros:**
✅ Eliminates conditionals  
✅ Open/Closed Principle  
✅ Easy to add strategies  

**Cons:**
❌ Clients must know strategies  
❌ Increased number of objects  

**In This Repo:** [Strategy Implementation](./src/main/java/org/lld/patterns/behavioural/strategy)

---

### 2. Observer Pattern 👁️

**Intent:** Define one-to-many dependency so when one object changes, all dependents are notified.

**When to Use:**
- One object change affects unknown number of others
- Object should notify others without knowing who they are
- Loose coupling between objects

**Real-World Examples:**
- Event handling systems
- Newsletter subscriptions
- Stock price monitoring
- Social media notifications

**Code Example:**
```java
interface Observer {
    void update(String message);
}

class Subject {
    private List<Observer> observers = new ArrayList<>();
    
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    public void notifyAllObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}

class EmailNotifier implements Observer {
    public void update(String message) {
        System.out.println("Email: " + message);
    }
}

class SMSNotifier implements Observer {
    public void update(String message) {
        System.out.println("SMS: " + message);
    }
}
```

**Pros:**
✅ Loose coupling  
✅ Dynamic relationships  
✅ Broadcast communication  

**Cons:**
❌ Unexpected updates  
❌ Memory leaks if not managed  

**In This Repo:** [Observer Implementation](./src/main/java/org/lld/patterns/behavioural/observer)

---

### 3. State Pattern 🔄

**Intent:** Allow object to alter behavior when internal state changes.

**When to Use:**
- Object behavior depends on its state
- Large conditional statements based on state
- States change frequently

**Real-World Examples:**
- TCP connection states
- Order processing (Placed, Confirmed, Shipped, Delivered)
- Vending machine states
- ATM states

**Code Example:**
```java
interface OrderState {
    void next(Order order);
    void prev(Order order);
    void printStatus();
}

class Order {
    private OrderState state;
    
    public Order() {
        state = new PlacedState();
    }
    
    public void setState(OrderState state) {
        this.state = state;
    }
    
    public void nextState() {
        state.next(this);
    }
    
    public void previousState() {
        state.prev(this);
    }
}

class PlacedState implements OrderState {
    public void next(Order order) {
        order.setState(new ConfirmedState());
    }
    
    public void prev(Order order) {
        System.out.println("Already in initial state");
    }
    
    public void printStatus() {
        System.out.println("Order placed");
    }
}
```

**Pros:**
✅ Eliminates conditionals  
✅ Encapsulates state-specific behavior  
✅ Easy to add new states  

**Cons:**
❌ Many state classes  
❌ Can be overkill  

**In This Repo:** [State Implementation](./src/main/java/org/lld/patterns/behavioural/state)

---

### 4. Command Pattern 📋

**Intent:** Encapsulate a request as an object.

**When to Use:**
- Parameterize objects with operations
- Queue or log operations
- Support undo/redo
- Support transactions

**Real-World Examples:**
- GUI buttons and menu items
- Transaction processing
- Macro recording
- Job queues

**Pros:**
✅ Decouples sender and receiver  
✅ Easy to add new commands  
✅ Supports undo/redo  

**Cons:**
❌ Many command classes  
❌ Increased complexity  

**In This Repo:** [Command Implementation](./src/main/java/org/lld/patterns/behavioural/command)

---

### 5. Chain of Responsibility Pattern ⛓️

**Intent:** Pass request along chain of handlers until one handles it.

**When to Use:**
- Multiple objects can handle request
- Handler not known in advance
- Set of handlers dynamically specified

**Real-World Examples:**
- Exception handling
- Approval workflows
- Event bubbling in UI
- Logging frameworks

**Pros:**
✅ Reduced coupling  
✅ Flexible assignment  
✅ Easy to add handlers  

**Cons:**
❌ Request might go unhandled  
❌ Can be hard to debug  

**In This Repo:** [Chain of Responsibility Implementation](./src/main/java/org/lld/patterns/behavioural/chainOfResponsibility)

---

## Pattern Selection Guide

### Choose Pattern Based on Problem:

| Problem | Pattern | Why |
|---------|---------|-----|
| Need only one instance | Singleton | Controls instance creation |
| Different object types at runtime | Factory | Centralizes creation logic |
| Complex object construction | Builder | Step-by-step construction |
| Expensive object creation | Prototype | Cloning is faster |
| Related object families | Abstract Factory | Ensures consistency |
| Incompatible interfaces | Adapter | Makes interfaces compatible |
| Dynamic functionality addition | Decorator | Flexible extension |
| Many similar objects | Flyweight | Reduces memory |
| Algorithm varies | Strategy | Interchangeable algorithms |
| One-to-many notifications | Observer | Loose coupling |
| Behavior changes with state | State | Clean state transitions |
| Encapsulate requests | Command | Supports undo/redo |
| Process request in chain | Chain of Responsibility | Flexible handling |

---

## Common Combinations

Patterns often work together:

### 1. **Strategy + Factory**
```java
PaymentStrategy strategy = PaymentFactory.create(type);
```
Factory creates appropriate strategy

### 2. **Observer + Singleton**
```java
NotificationService.getInstance().notifyObservers();
```
Single notification service with many observers

### 3. **State + Strategy**
```java
// State defines what to do
// Strategy defines how to do it
orderState.process(pricingStrategy);
```

### 4. **Decorator + Factory**
```java
Component component = ComponentFactory.create();
component = new DecoratorA(component);
component = new DecoratorB(component);
```

### 5. **Command + Chain of Responsibility**
```java
// Commands queued and processed by chain
commandQueue.add(command);
chainHandler.process(commandQueue);
```

---

## Anti-Patterns to Avoid

### ❌ 1. God Object
One class doing everything
```java
// Bad
class System {
    void createUser() { }
    void processPayment() { }
    void sendEmail() { }
    void generateReport() { }
    // 100 more methods...
}
```

### ❌ 2. Spaghetti Code
Tangled dependencies, no structure

### ❌ 3. Golden Hammer
Using same pattern for everything (overusing Singleton)

### ❌ 4. Copy-Paste Programming
Duplicating code instead of abstracting

### ❌ 5. Magic Numbers/Strings
Hardcoded values everywhere
```java
if (status == 5) { } // What is 5?
```

### ❌ 6. Premature Optimization
Over-engineering before needed

---

## Pattern Cheat Sheet

```
CREATIONAL: How objects are created
├── Singleton      → One instance
├── Factory        → Creation logic
├── Builder        → Step-by-step
├── Prototype      → Cloning
└── Abstract Factory → Families

STRUCTURAL: How objects are composed
├── Adapter        → Interface bridge
├── Decorator      → Add functionality
├── Flyweight      → Share data
├── Facade         → Simplified interface
├── Proxy          → Controlled access
└── Composite      → Tree structure

BEHAVIORAL: How objects communicate
├── Strategy       → Algorithms
├── Observer       → Notifications
├── State          → State-based behavior
├── Command        → Encapsulate requests
├── Chain of Resp. → Handler chain
├── Template Method→ Algorithm skeleton
└── Iterator       → Sequential access
```

---

## Quick Decision Tree

```
Need to create objects?
├─ Only one instance? → Singleton
├─ Complex construction? → Builder
├─ Different types? → Factory
└─ Expensive creation? → Prototype

Need to structure objects?
├─ Incompatible interface? → Adapter
├─ Add functionality? → Decorator
└─ Save memory? → Flyweight

Need objects to communicate?
├─ Algorithm varies? → Strategy
├─ State-based behavior? → State
├─ Notify many objects? → Observer
└─ Queue operations? → Command
```

---

**Remember:** Patterns are tools, not rules. Use them when they solve a problem, not just because they exist!

Happy Coding! 🚀

