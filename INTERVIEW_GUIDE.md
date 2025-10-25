# LLD Interview Preparation Guide 🎯

## Table of Contents
- [Introduction](#introduction)
- [Interview Format](#interview-format)
- [Step-by-Step Approach](#step-by-step-approach)
- [Common Mistakes](#common-mistakes)
- [Evaluation Criteria](#evaluation-criteria)
- [Practice Strategy](#practice-strategy)
- [Company-Specific Tips](#company-specific-tips)
- [Sample Interview Questions](#sample-interview-questions)

---

## Introduction

Low-Level Design (LLD) interviews test your ability to:
- Design maintainable, extensible systems
- Apply OOP principles and design patterns
- Make informed trade-off decisions
- Write clean, production-ready code

**Duration:** Typically 45-60 minutes  
**Format:** Coding on whiteboard/shared editor  
**Focus:** Code structure, not algorithms

---

## Interview Format

### Phase 1: Requirements Gathering (5-10 mins)
- **Don't jump to coding immediately!**
- Ask clarifying questions
- Identify functional and non-functional requirements
- Confirm assumptions with interviewer

### Phase 2: Design Discussion (15-20 mins)
- Identify core entities (classes)
- Define relationships between entities
- Discuss design patterns to use
- Draw rough class diagram (optional)

### Phase 3: Implementation (20-25 mins)
- Start with core classes
- Define interfaces first
- Implement key methods
- Show extensibility

### Phase 4: Discussion & Extensions (5-10 mins)
- Discuss trade-offs
- Talk about scalability
- Suggest improvements
- Answer follow-up questions

---

## Step-by-Step Approach

### Step 1: Understand the Problem
**Questions to Ask:**
- "What are the core features needed?"
- "How many users/transactions do we expect?" (Scale)
- "Are there any specific constraints?"
- "Should we prioritize X over Y?"

**Example:** Design a Parking Lot
- "How many floors?"
- "Types of vehicles supported?"
- "Payment methods?"
- "Multiple entry/exit points?"

### Step 2: Identify Core Entities
**Technique:** Look for **nouns** in the problem statement

**Example:** Parking Lot
- ParkingLot, ParkingSpot, Vehicle, Ticket, Payment, Gate

**Tips:**
- Start with 5-7 core classes
- Don't over-engineer initially
- Focus on main flow first

### Step 3: Define Relationships
**Types:**
- **Association:** `Order` has a `Customer`
- **Composition:** `Car` has an `Engine` (strong ownership)
- **Inheritance:** `Car` extends `Vehicle`
- **Aggregation:** `Library` has `Books` (weak ownership)

### Step 4: Identify Design Patterns
**Common Patterns by Use Case:**

| Use Case | Pattern | Example |
|----------|---------|---------|
| Need single instance | Singleton | ParkingLot, DBConnection |
| Varying object creation | Factory | Vehicle types |
| Complex construction | Builder | Car with many options |
| Varying algorithm | Strategy | Pricing, Sorting |
| Behavior changes with state | State | Order status, Ride status |
| Notify multiple objects | Observer | Order updates |
| Add functionality | Decorator | Pizza toppings |

### Step 5: Apply SOLID Principles

**Single Responsibility:**
```java
// Bad
class User {
    void saveToDatabase() {
    }  // DB logic

    void sendEmail() {
    }       // Email logic
}

// Good
class User {
}

class UserRepository {
    void save(User user) {
    }
}

class EmailService {
    void send(User user) {
    }
}
```

**Open/Closed:**
```java
// Bad - need to modify for new payment types
class PaymentProcessor {
    void process(String type) {
        if (type.equals("credit")) { }
        else if (type.equals("debit")) { }
    }
}

// Good - extend without modifying
interface PaymentMethod {
    void process();
}
class CreditCardPayment implements PaymentMethod { }
class DebitCardPayment implements PaymentMethod { }
```

### Step 6: Code Structure
```java
// Start with interfaces
interface PaymentMethod {
    boolean process(double amount);
}

// Core entities
class Order {
    private String orderId;
    private Customer customer;
    private OrderState state;
    // ... getters, setters
}

// Services
class OrderService {
    public Order placeOrder(Customer customer, List<Item> items) {
        // Business logic
    }
}
```

---

## Common Mistakes

### ❌ Mistake 1: Jumping to Code
**Problem:** Start coding without understanding requirements  
**Solution:** Spend 5-10 mins discussing requirements first

### ❌ Mistake 2: God Classes
**Problem:** One class doing everything
```java
class Uber {
    void matchDriver() { }
    void calculateFare() { }
    void processPayment() { }
    void sendNotification() { }
    // 50 more methods...
}
```
**Solution:** Separate concerns into focused classes

### ❌ Mistake 3: Ignoring Extensibility
**Problem:** Hardcoding everything
```java
double fare = 10 + distance * 2;  // What if pricing changes?
```
**Solution:** Use Strategy pattern
```java
interface PricingStrategy {
    double calculate(Ride ride);
}
```

### ❌ Mistake 4: Over-Engineering
**Problem:** Using 10 design patterns for simple problem  
**Solution:** Start simple, add patterns only when needed

### ❌ Mistake 5: No Error Handling
**Problem:** Assuming happy path always
```java
void borrowBook(Member member, Book book) {
    // What if book is unavailable?
    // What if member has overdue books?
}
```
**Solution:** Handle edge cases

### ❌ Mistake 6: Poor Naming
**Problem:** `a`, `data`, `manager`, `handler` everywhere  
**Solution:** Use descriptive names - `OrderService`, `PaymentProcessor`

---

## Evaluation Criteria

### ⭐ Strong Candidate (Hire)
- ✅ Asks clarifying questions
- ✅ Identifies core entities correctly
- ✅ Uses appropriate design patterns
- ✅ Writes clean, readable code
- ✅ Discusses trade-offs
- ✅ Handles edge cases
- ✅ Code is extensible

### ⚠️ Average Candidate (Maybe)
- ⚠️ Jumps to coding quickly
- ⚠️ Misses some edge cases
- ⚠️ Some design pattern usage
- ⚠️ Code works but not clean
- ⚠️ Limited discussion of trade-offs

### ❌ Weak Candidate (No Hire)
- ❌ No clarifying questions
- ❌ Wrong entity identification
- ❌ No design patterns
- ❌ God classes everywhere
- ❌ Hardcoded values
- ❌ Doesn't complete in time

---

## Practice Strategy

### Week 1-2: Foundations
- [ ] Review OOP concepts (4 pillars)
- [ ] Master SOLID principles (with examples)
- [ ] Learn 5 key patterns: Singleton, Factory, Strategy, Observer, State
- [ ] Practice 2 simple problems (Vending Machine, Snake & Ladder)

### Week 3-4: Pattern Mastery
- [ ] Learn remaining patterns
- [ ] Understand when to use each pattern
- [ ] Practice 3 medium problems (Parking Lot, Library, Elevator)
- [ ] **Important:** Implement from scratch without looking

### Week 5-6: Advanced Practice
- [ ] Practice 3 complex problems (ATM, Food Delivery, Ride-Sharing)
- [ ] Time yourself (45 mins per problem)
- [ ] Practice explaining your design out loud
- [ ] Review and optimize previous solutions

### Week 7-8: Mock Interviews
- [ ] Do mock interviews with peers
- [ ] Record yourself explaining designs
- [ ] Practice on whiteboard (not IDE)
- [ ] Review common interview questions

### Daily Routine
**30-60 minutes:**
- Day 1: Learn new pattern + implement simple example
- Day 2: Apply pattern to practice problem
- Day 3: Solve complete system design
- Day 4: Review and optimize
- Day 5: Mock interview or replay

---

## Company-Specific Tips

### Amazon
**Focus:** Scalability, availability, maintainability  
**Common Questions:** Parking Lot, Library System, Elevator  
**What they value:** SOLID principles, clean code, thinking aloud

### Google
**Focus:** Efficiency, optimal solutions  
**Common Questions:** File System, LRU Cache, Design Search  
**What they value:** Time/space complexity, algorithms in design

### Microsoft
**Focus:** OOP, design patterns, extensibility  
**Common Questions:** Elevator, Hotel Booking, Meeting Scheduler  
**What they value:** Clear communication, pattern knowledge

### Uber/Lyft
**Focus:** Real-time systems, matching algorithms  
**Common Questions:** Ride-sharing, Food delivery, Maps  
**What they value:** State management, strategy patterns

### Startup/Product Companies
**Focus:** Practical solutions, MVP approach  
**Common Questions:** Domain-specific (e.g., ecommerce, social media)  
**What they value:** Pragmatism over perfection, quick thinking

---

## Sample Interview Questions

### Easy (30 mins)
1. Design a Vending Machine
2. Design a Parking Meter
3. Design a Simple Calculator
4. Design a Traffic Light System
5. Design a Tic-Tac-Toe Game

### Medium (45 mins)
1. **Design a Parking Lot System** ⭐ Most Common
2. Design an Elevator System
3. Design a Library Management System
4. Design a Hotel Booking System
5. Design a Movie Ticket Booking System
6. Design a Car Rental System
7. Design an ATM
8. Design a Snake and Ladder Game
9. Design a Chess Game
10. Design a Restaurant Management System

### Hard (60 mins)
1. Design a Food Delivery System (Uber Eats)
2. Design a Ride-Sharing System (Uber)
3. Design an Online Shopping System (Amazon)
4. Design a Social Media Feed
5. Design a Meeting Scheduler
6. Design a File Storage System (Dropbox)
7. Design a Notification Service
8. Design a Messaging System (WhatsApp)
9. Design a Payment Gateway
10. Design a Stock Trading System

---

## Interview Day Checklist

### Before Interview
- [ ] Review key design patterns
- [ ] Practice 1-2 problems
- [ ] Prepare clarifying questions
- [ ] Rest well

### During Interview
- [ ] Listen carefully to the problem
- [ ] Ask clarifying questions (don't assume!)
- [ ] Think aloud - communicate your thought process
- [ ] Start with high-level design
- [ ] Write clean, readable code
- [ ] Handle edge cases
- [ ] Discuss trade-offs
- [ ] Be open to feedback

### After Interview
- [ ] Note down questions asked
- [ ] Identify areas for improvement
- [ ] Practice weak areas
- [ ] Stay positive!

---

## Key Takeaways

### ✅ Do's
1. **Ask questions** before coding
2. **Think aloud** - explain your reasoning
3. **Start simple** - don't over-engineer initially
4. **Use proper naming** - clear, descriptive names
5. **Apply SOLID** - especially SRP and OCP
6. **Discuss trade-offs** - show you understand pros/cons
7. **Handle errors** - don't ignore edge cases
8. **Be flexible** - adapt based on interviewer hints

### ❌ Don'ts
1. **Don't rush** to code immediately
2. **Don't create God classes** - separate concerns
3. **Don't hardcode** - use patterns for flexibility
4. **Don't ignore** interviewer's hints
5. **Don't argue** - be open to feedback
6. **Don't give up** - think through problems
7. **Don't forget** to test your logic mentally
8. **Don't over-complicate** - keep it simple

---

## Final Tips

1. **Practice consistently** - 1 hour daily is better than 7 hours once
2. **Implement from scratch** - don't just read solutions
3. **Explain out loud** - pretend you're in an interview
4. **Time yourself** - get comfortable with time pressure
5. **Review mistakes** - learn from each practice session
6. **Stay calm** - it's okay not to have perfect solution immediately
7. **Ask for hints** - interviewers appreciate candidates who collaborate

---

## Resources for Practice

### In This Repo:
- [Design Patterns](./src/main/java/org/lld/patterns/)
- [Practice Problems](./src/main/java/org/lld/practice/)
- [SOLID Principles](./src/main/resources/docs/basic/solid.md)
- [OOP Concepts](./src/main/resources/docs/basic/oop.md)

### External:
- [LeetCode](https://leetcode.com) - For algorithm practice
- [Refactoring Guru](https://refactoring.guru) - Visual pattern guide
- [System Design Primer](https://github.com/donnemartin/system-design-primer)
- [Educative.io](https://www.educative.io/courses/grokking-the-object-oriented-design-interview)

---

**Remember:** The goal is not to memorize solutions, but to understand principles and apply them flexibly!

**Good luck with your interviews! 🚀**

