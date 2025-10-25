# LLD Interview Cheatsheet 📝

*Quick reference for last-minute review before interviews*

---

## 🎯 Interview Flow (45-60 mins)

1. **Requirements (5-10 min)** → Ask questions, clarify scope
2. **Design (15-20 min)** → Identify entities, relationships, patterns
3. **Code (20-25 min)** → Implement core classes and methods
4. **Discussion (5-10 min)** → Trade-offs, extensions, improvements

---

## ❓ Questions to Ask

- "What are the main use cases?"
- "Expected scale (users, requests/sec)?"
- "Any specific constraints or requirements?"
- "Should I focus on X or Y feature first?"
- "What's more important: performance or simplicity?"

---

## 🏗️ Design Checklist

- [ ] Identify core entities (5-7 main classes)
- [ ] Define relationships (has-a, is-a)
- [ ] Choose design patterns
- [ ] Apply SOLID principles
- [ ] Handle edge cases
- [ ] Think about extensibility

---

## 🔄 SOLID Principles (Quick)

| Principle | Means | Example |
|-----------|-------|---------|
| **S**ingle Responsibility | One reason to change | User class ≠ DB logic |
| **O**pen/Closed | Extend without modify | Use Strategy, not if-else |
| **L**iskov Substitution | Subtypes replaceable | Square ≠ extends Rectangle |
| **I**nterface Segregation | Many specific interfaces | Not one fat interface |
| **D**ependency Inversion | Depend on abstractions | Interface, not concrete |

---

## 🎨 Design Patterns (Quick Reference)

### Creational (Object Creation)
```
Singleton     → One instance only           → ParkingLot, DBConnection
Factory       → Create different types      → Vehicle types
Builder       → Complex construction        → Car with options
Prototype     → Clone objects              → Template copying
Abstract Factory → Families of objects     → UI components
```

### Structural (Object Composition)
```
Adapter       → Interface compatibility     → Legacy system
Decorator     → Add functionality          → Coffee with add-ons
Flyweight     → Share common data          → Text characters
Facade        → Simplified interface       → Complex subsystem
Proxy         → Controlled access          → Virtual proxy
```

### Behavioral (Object Communication)
```
Strategy      → Interchangeable algorithms → Payment methods
State         → Behavior by state          → Order status
Observer      → One-to-many notifications  → Event listeners
Command       → Encapsulate requests       → Undo/redo
Chain of Resp → Handler chain             → Approval workflow
Template Method → Algorithm skeleton       → Data processing
```

---

## 🚦 Pattern Selection

**Ask yourself:**
- Need only one instance? → **Singleton**
- Algorithm varies? → **Strategy**
- Behavior changes with state? → **State**
- Need to notify many objects? → **Observer**
- Complex object creation? → **Builder** or **Factory**
- Add functionality dynamically? → **Decorator**
- Incompatible interfaces? → **Adapter**

---

## 💻 Code Structure Template

```java
// 1. Define interfaces first
interface PaymentMethod {
    boolean process(double amount);
}

// 2. Models (data classes)
class Order {
    private String orderId;
    private Customer customer;
    private OrderState state;
    private List<Item> items;
    // constructor, getters, setters
}

// 3. Services (business logic)
class OrderService {
    private PaymentService paymentService;
    private NotificationService notificationService;
    
    public Order placeOrder(Customer customer, List<Item> items) {
        // Validate
        // Create order
        // Process payment
        // Notify
        return order;
    }
}

// 4. States (if using State pattern)
interface OrderState {
    void next(Order order);
}

class PlacedState implements OrderState {
    public void next(Order order) {
        order.setState(new ConfirmedState());
    }
}

// 5. Strategies (if using Strategy pattern)
class CreditCardPayment implements PaymentMethod {
    public boolean process(double amount) {
        // Implementation
    }
}
```

---

## ⚠️ Common Mistakes to Avoid

1. ❌ **Jumping to code** → Take time to design first
2. ❌ **God classes** → Separate concerns
3. ❌ **Hardcoding** → Use patterns for flexibility
4. ❌ **No error handling** → Validate inputs
5. ❌ **Poor naming** → Use descriptive names
6. ❌ **Over-engineering** → Keep it simple
7. ❌ **Ignoring hints** → Listen to interviewer
8. ❌ **Not discussing trade-offs** → Show you understand pros/cons

---

## 📊 Complexity Quick Reference

```
O(1)      → Array[i], HashMap.get()
O(log n)  → Binary search, TreeMap
O(n)      → Single loop, List iteration
O(n log n)→ Sorting (MergeSort, QuickSort)
O(n²)     → Nested loops
```

---

## 🎯 Common Interview Problems

### Easy (Warm-up)
- Vending Machine
- Parking Meter
- Traffic Light
- Tic-Tac-Toe

### Medium (Most Common) ⭐
- **Parking Lot System** ← Asked frequently
- Elevator System
- Library Management
- Hotel Booking
- Movie Ticket Booking
- ATM System
- Car Rental

### Hard (Advanced)
- Ride-Sharing (Uber)
- Food Delivery (Uber Eats)
- E-commerce Platform
- Social Media Feed
- Meeting Scheduler
- File Storage System

---

## 💡 Problem-Solving Steps

### 1. Understand (2-3 min)
- Read problem carefully
- Ask clarifying questions
- Identify functional requirements
- Note constraints

### 2. Design (5-7 min)
- List entities (nouns in problem)
- Define relationships
- Choose patterns
- Draw rough diagram

### 3. Code (20-25 min)
- Start with interfaces
- Implement core classes
- Write key methods
- Handle edge cases

### 4. Test (2-3 min)
- Walk through example
- Check edge cases
- Verify logic

### 5. Discuss (5 min)
- Complexity analysis
- Trade-offs
- Possible improvements
- Scalability

---

## 🔑 Key Phrases to Use

- "Let me clarify the requirements..."
- "I'm thinking of using [pattern] because..."
- "This approach has time complexity O(n)..."
- "The trade-off here is..."
- "To extend this, we could..."
- "An alternative would be..."
- "For production, I'd also consider..."

---

## 📋 Pre-Interview Checklist

**Night Before:**
- [ ] Review 2-3 practice problems
- [ ] Review SOLID principles
- [ ] Review common patterns
- [ ] Get good sleep

**30 Minutes Before:**
- [ ] Review this cheatsheet
- [ ] Practice explaining a design out loud
- [ ] Relax and be confident

**During Interview:**
- [ ] Think aloud
- [ ] Ask questions
- [ ] Start simple
- [ ] Be receptive to feedback
- [ ] Manage time wisely

---

## 🎨 Parking Lot Template (Most Common Question)

```java
// Core Entities
class ParkingLot {
    List<ParkingSpot> spots;
    List<Gate> gates;
}

class ParkingSpot {
    String spotId;
    SpotSize size;
    Vehicle vehicle;
    SpotStatus status;
}

class Vehicle {
    String licensePlate;
    VehicleType type;
}

class Ticket {
    String ticketId;
    Vehicle vehicle;
    ParkingSpot spot;
    LocalDateTime entryTime;
}

// Patterns Used
- Singleton: ParkingLot
- Strategy: PricingStrategy, SpotFindingStrategy
- Factory: VehicleFactory

// Key Methods
- parkVehicle(Vehicle v): Ticket
- removeVehicle(Ticket t): Payment
- findAvailableSpot(VehicleType): Spot
- calculateFee(Ticket): double
```

---

## 🏃 Speed Tips

1. **Have templates ready** → Standard class structure
2. **Know your patterns** → When to use each
3. **Practice timing** → 45 min complete solution
4. **Use shorthand** → Write interfaces first, implement later
5. **Communicate constantly** → Explain as you code

---

## 🌟 What Makes a Strong Candidate

✅ **Asks good questions** before coding  
✅ **Thinks aloud** - explains reasoning  
✅ **Uses patterns appropriately**  
✅ **Writes clean, organized code**  
✅ **Handles edge cases**  
✅ **Discusses trade-offs**  
✅ **Open to feedback**  
✅ **Completes in time**  

---

## 🎯 Final Reminders

1. **Clarify first, code later**
2. **Start simple, add complexity**
3. **Think aloud, communicate**
4. **Use proper naming**
5. **Handle errors**
6. **Discuss trade-offs**
7. **Be flexible**
8. **Stay calm**

---

## 🔗 Quick Links (In This Repo)

- [Full Interview Guide](./INTERVIEW_GUIDE.md)
- [Patterns Guide](./PATTERNS_GUIDE.md)
- [Best Practices](./BEST_PRACTICES.md)
- [Complexity Analysis](./COMPLEXITY_ANALYSIS.md)
- [Practice Problems](./src/main/java/org/lld/practice/)
- [Design Patterns](./src/main/java/org/lld/patterns/)

---

**You've got this! Trust your preparation and stay confident! 🚀**

*Remember: Perfect is the enemy of good. A working, clean solution beats a perfect, incomplete one!*

