# Design A Food Delivery System (like Uber Eats, DoorDash)

## 1. Problem Statement and Requirements

Our goal is to design a software system for a food delivery platform connecting customers, restaurants, and delivery partners.

### Functional Requirements:

- **User Management:** Register and manage customers, restaurants, and delivery partners.
- **Restaurant & Menu:** Restaurants can add/update menus, manage availability, and set operating hours.
- **Order Placement:** Customers can browse restaurants, add items to cart, and place orders.
- **Order Tracking:** Real-time tracking of order status (placed, preparing, ready, picked up, delivered).
- **Delivery Assignment:** Automatically assign delivery partners based on availability and proximity.
- **Payment Processing:** Handle payments with multiple methods (credit card, wallet, cash on delivery).
- **Ratings & Reviews:** Customers can rate restaurants and delivery partners.
- **Notifications:** Notify users about order status changes.
- **Promotions:** Apply discount codes and offers.

### Non-Functional Requirements:

- **Scalability:** Handle thousands of concurrent orders across multiple cities.
- **Performance:** Fast restaurant search and real-time order tracking.
- **Reliability:** Ensure orders are not lost even if system components fail.
- **Availability:** 99.9% uptime for core services.
- **Maintainability:** Easy to add new payment methods, delivery strategies, or restaurant features.

---

## 2. Naive Solution: The "Starting Point"

A beginner might create a single `FoodDeliveryApp` class handling everything.

### The Thought Process:

"The app manages customers, restaurants, and deliveries. Let's put it all in one place."

```java
class FoodDeliveryApp {
    private List<Customer> customers;
    private List<Restaurant> restaurants;
    private List<DeliveryPartner> deliveryPartners;
    private List<Order> orders;
    
    public void placeOrder(Customer customer, Restaurant restaurant, List<Item> items) {
        // ... create order, find delivery partner, process payment ...
    }
    
    public void updateOrderStatus(Order order, String status) {
        // ... update status, notify customer ...
    }
    
    public void assignDeliveryPartner(Order order) {
        // ... find available partner, assign ...
    }
    
    // ... many more methods ...
}
```

### Limitations and Design Flaws:

- **Violation of SOLID Principles:** One class handles user management, order processing, delivery assignment, and payments.
- **Poor Scalability:** A single class can't handle thousands of concurrent operations efficiently.
- **Tight Coupling:** Payment logic is tightly coupled with order management and delivery assignment.
- **Hard to Test:** Can't test delivery assignment logic without setting up the entire application.
- **No Flexibility:** Adding a new payment method or delivery strategy requires modifying core code.

---

## 3. Improved Solution: The "Mentor's Guidance"

The key is to model the system as independent, loosely coupled services.

### The "Why": Key Design Patterns

- **Strategy Pattern:** Different delivery assignment strategies (nearest partner, load balancing). Different payment methods (card, wallet, COD).
- **Observer Pattern:** Notify customers, restaurants, and delivery partners about order status changes.
- **State Pattern:** Order goes through different states (placed, confirmed, preparing, ready, picked up, delivered).
- **Factory Pattern:** Create different user types (customer, restaurant owner, delivery partner) and order types.
- **Facade Pattern:** Provide a simple interface to the complex subsystems.

### Core Classes and Their Interactions:

We'll create a service-oriented architecture:

1. **`User` Hierarchy (The Actors):**
    - `User` (abstract base)
    - `Customer`: Can place orders, track deliveries
    - `RestaurantOwner`: Manages restaurant and menu
    - `DeliveryPartner`: Accepts and delivers orders

2. **`Restaurant` (The Service Provider):**
    - Contains menu items, operating hours, location
    - Manages item availability

3. **`MenuItem` (The Product):**
    - Represents food items with name, price, description
    - Can have customizations (add-ons, size, spice level)

4. **`Order` (The Transaction):**
    - Contains ordered items, customer, restaurant, delivery partner
    - Tracks order state using State pattern
    - Has unique order ID

5. **`OrderState` Interface (The State):**
    - Implementations: `PlacedState`, `ConfirmedState`, `PreparingState`, `ReadyState`, `PickedUpState`, `DeliveredState`, `CancelledState`

6. **`Cart` (The Shopping Basket):**
    - Temporary storage for items before checkout
    - Calculates subtotal, taxes, delivery fees

7. **`OrderService` (The Business Logic):**
    - Handles order placement, validation, and processing
    - Manages order lifecycle

8. **`DeliveryService` (The Assignment Manager):**
    - Assigns delivery partners to orders
    - Uses delivery assignment strategies
    - Tracks delivery partner availability

9. **`DeliveryStrategy` Interface:**
    - `NearestPartnerStrategy`: Assigns closest available partner
    - `LoadBalancingStrategy`: Distributes orders evenly
    - `PriorityBasedStrategy`: Based on partner ratings

10. **`PaymentService` (The Payment Processor):**
    - Processes payments using different methods
    - Uses Strategy pattern for payment methods

11. **`PaymentMethod` Interface:**
    - Implementations: `CreditCardPayment`, `WalletPayment`, `CashOnDelivery`

12. **`NotificationService` (The Observer):**
    - Sends notifications to users about order updates
    - Implements Observer pattern

13. **`Location` (The Geo Data):**
    - Represents latitude/longitude
    - Calculates distance between locations

14. **`RestaurantService` (The Catalog Manager):**
    - Manages restaurant listings and search
    - Provides filtering and sorting

---

## 4. Final Design Overview

Our final design is a microservice-inspired, modular system:

* The system is divided into independent services (Order, Delivery, Payment, Notification, Restaurant).
* Each service has a single, well-defined responsibility.
* The `OrderState` pattern manages order lifecycle clearly and safely.
* The `DeliveryStrategy` allows for flexible delivery assignment algorithms.
* The `PaymentMethod` strategy makes it easy to add new payment options.
* The `NotificationService` observer keeps all stakeholders informed.

This design is:

- **Highly Scalable:** Each service can be scaled independently based on load.
- **Maintainable:** Adding a new payment method only requires implementing `PaymentMethod` interface.
- **Testable:** Each service can be tested in isolation with mocked dependencies.
- **Flexible:** Easy to add new features like restaurant recommendations, loyalty programs, or surge pricing.
- **Loosely Coupled:** Services communicate through well-defined interfaces.
- **Extensible:** New user types, order types, or delivery strategies can be added without modifying existing code.

### Key Design Patterns Used:

- **Strategy Pattern:** Payment methods, delivery assignment strategies
- **State Pattern:** Order state management
- **Observer Pattern:** Notification system
- **Factory Pattern:** User creation, order creation
- **Facade Pattern:** Simplified interface to complex subsystems
- **Singleton Pattern:** Service instances (OrderService, DeliveryService)
- **Repository Pattern:** Data access for users, restaurants, orders

### System Flow:

1. **Customer places order** → OrderService validates and creates order
2. **Order created** → Payment is processed via PaymentService
3. **Payment successful** → DeliveryService assigns a delivery partner
4. **Assignment complete** → NotificationService notifies all parties
5. **Order state changes** → All observers are notified automatically
6. **Delivery complete** → Final state transition, ratings enabled

