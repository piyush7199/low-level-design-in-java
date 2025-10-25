# Software Design Best Practices 🌟

## Table of Contents
- [Code Organization](#code-organization)
- [Naming Conventions](#naming-conventions)
- [SOLID Principles in Practice](#solid-principles-in-practice)
- [Error Handling](#error-handling)
- [Testing Strategies](#testing-strategies)
- [Performance Considerations](#performance-considerations)
- [Documentation](#documentation)
- [Code Review Checklist](#code-review-checklist)

---

## Code Organization

### Package Structure
```
com.company.project/
├── models/          # Domain entities
├── services/        # Business logic
├── repositories/    # Data access
├── controllers/     # Entry points
├── strategies/      # Strategy implementations
├── factories/       # Object creation
├── utils/           # Helper classes
└── exceptions/      # Custom exceptions
```

### Class Organization
```java
public class UserService {
    // 1. Constants
    private static final int MAX_RETRY = 3;
    
    // 2. Static variables
    private static UserService instance;
    
    // 3. Instance variables
    private final UserRepository repository;
    private final EmailService emailService;
    
    // 4. Constructors
    public UserService(UserRepository repo, EmailService email) {
        this.repository = repo;
        this.emailService = email;
    }
    
    // 5. Public methods
    public User createUser(String name, String email) {
        // implementation
    }
    
    // 6. Private methods
    private void validateUser(User user) {
        // implementation
    }
    
    // 7. Getters/Setters (if needed)
    // 8. Inner classes (if needed)
}
```

---

## Naming Conventions

### Classes
```java
// ✅ Good - Noun, PascalCase
public class OrderService { }
public class PaymentProcessor { }
public class UserRepository { }

// ❌ Bad
public class order { }              // lowercase
public class ProcessPaymentClass { } // redundant "Class"
public class DoStuff { }            // vague verb
```

### Methods
```java
// ✅ Good - Verb, camelCase, descriptive
public void calculateTotalPrice() { }
public boolean isEligibleForDiscount() { }
public User findUserById(String id) { }

// ❌ Bad
public void calc() { }              // abbreviated
public void DoSomething() { }       // PascalCase
public void x() { }                 // meaningless
```

### Variables
```java
// ✅ Good - Descriptive, camelCase
String customerName;
int totalOrderCount;
boolean isActiveUser;

// ❌ Bad
String n;                           // too short
String customer_name;               // snake_case
boolean flag;                       // vague
```

### Constants
```java
// ✅ Good - UPPER_SNAKE_CASE
public static final int MAX_CONNECTIONS = 100;
public static final String DEFAULT_CURRENCY = "USD";

// ❌ Bad
public static final int maxConnections = 100;  // camelCase
public static final String dCurr = "USD";      // abbreviated
```

### Interfaces
```java
// ✅ Good - Adjective or Noun
interface Comparable { }
interface Serializable { }
interface PaymentProcessor { }

// ❌ Bad
interface IPayment { }              // Hungarian notation
interface PaymentInterface { }      // redundant "Interface"
```

---

## SOLID Principles in Practice

### Single Responsibility Principle (SRP)

**❌ Bad - Multiple Responsibilities**
```java
class User {
    private String name;
    private String email;
    
    // Database operation
    public void save() {
        // DB code here
    }
    
    // Email operation
    public void sendWelcomeEmail() {
        // Email code here
    }
    
    // Validation
    public boolean validate() {
        // Validation code here
    }
}
```

**✅ Good - Single Responsibility**
```java
// Model - just data
class User {
    private String name;
    private String email;
    // getters, setters
}

// Repository - data access
class UserRepository {
    public void save(User user) {
        // DB code here
    }
}

// Service - business logic
class UserService {
    private UserRepository repository;
    private EmailService emailService;
    
    public void registerUser(User user) {
        repository.save(user);
        emailService.sendWelcomeEmail(user);
    }
}

// Validator - validation
class UserValidator {
    public boolean validate(User user) {
        // Validation code here
    }
}
```

### Open/Closed Principle (OCP)

**❌ Bad - Modification Required**
```java
class DiscountCalculator {
    public double calculate(String customerType, double amount) {
        if (customerType.equals("REGULAR")) {
            return amount * 0.95;
        } else if (customerType.equals("PREMIUM")) {
            return amount * 0.90;
        } else if (customerType.equals("VIP")) {
            return amount * 0.85;
        }
        return amount;
    }
}
```

**✅ Good - Extension Without Modification**
```java
interface DiscountStrategy {
    double calculate(double amount);
}

class RegularDiscount implements DiscountStrategy {
    public double calculate(double amount) {
        return amount * 0.95;
    }
}

class PremiumDiscount implements DiscountStrategy {
    public double calculate(double amount) {
        return amount * 0.90;
    }
}

class DiscountCalculator {
    public double calculate(DiscountStrategy strategy, double amount) {
        return strategy.calculate(amount);
    }
}
```

### Liskov Substitution Principle (LSP)

**❌ Bad - Violates LSP**
```java
class Rectangle {
    protected int width;
    protected int height;
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
}

class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width;  // Breaks parent behavior!
    }
}
```

**✅ Good - Follows LSP**
```java
interface Shape {
    double area();
}

class Rectangle implements Shape {
    private int width;
    private int height;
    
    public double area() {
        return width * height;
    }
}

class Square implements Shape {
    private int side;
    
    public double area() {
        return side * side;
    }
}
```

### Interface Segregation Principle (ISP)

**❌ Bad - Fat Interface**
```java
interface Worker {
    void work();
    void eat();
    void sleep();
    void getSalary();
}

// Robot doesn't eat or sleep!
class Robot implements Worker {
    public void work() { /* work */ }
    public void eat() { /* ? */ }      // Not applicable
    public void sleep() { /* ? */ }    // Not applicable
    public void getSalary() { /* ? */ }// Not applicable
}
```

**✅ Good - Segregated Interfaces**
```java
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class Human implements Workable, Eatable, Sleepable {
    public void work() { /* work */ }
    public void eat() { /* eat */ }
    public void sleep() { /* sleep */ }
}

class Robot implements Workable {
    public void work() { /* work */ }
}
```

### Dependency Inversion Principle (DIP)

**❌ Bad - Depends on Concrete Class**
```java
class MySQLDatabase {
    public void save(String data) {
        // MySQL specific code
    }
}

class UserService {
    private MySQLDatabase database = new MySQLDatabase();
    
    public void saveUser(User user) {
        database.save(user.toString());
    }
}
```

**✅ Good - Depends on Abstraction**
```java
interface Database {
    void save(String data);
}

class MySQLDatabase implements Database {
    public void save(String data) {
        // MySQL specific code
    }
}

class MongoDatabase implements Database {
    public void save(String data) {
        // MongoDB specific code
    }
}

class UserService {
    private Database database;
    
    public UserService(Database database) {
        this.database = database;
    }
    
    public void saveUser(User user) {
        database.save(user.toString());
    }
}
```

---

## Error Handling

### Use Specific Exceptions
```java
// ❌ Bad
public User findUser(String id) {
    if (id == null) {
        throw new Exception("Bad input");
    }
    // ...
}

// ✅ Good
public User findUser(String id) {
    if (id == null || id.isEmpty()) {
        throw new IllegalArgumentException("User ID cannot be null or empty");
    }
    
    User user = repository.findById(id);
    if (user == null) {
        throw new UserNotFoundException("User not found with ID: " + id);
    }
    return user;
}
```

### Custom Exceptions
```java
public class OrderException extends RuntimeException {
    private final String orderid;
    private final ErrorCode errorCode;
    
    public OrderException(String message, String orderId, ErrorCode code) {
        super(message);
        this.orderId = orderId;
        this.errorCode = code;
    }
    
    // getters
}

enum ErrorCode {
    INSUFFICIENT_INVENTORY,
    INVALID_PAYMENT,
    DELIVERY_UNAVAILABLE
}
```

### Fail Fast
```java
// ✅ Good - Validate early
public void processOrder(Order order) {
    Objects.requireNonNull(order, "Order cannot be null");
    
    if (order.getItems().isEmpty()) {
        throw new IllegalStateException("Order must have at least one item");
    }
    
    if (order.getCustomer() == null) {
        throw new IllegalStateException("Order must have a customer");
    }
    
    // Process order
}
```

---

## Testing Strategies

### Unit Test Structure
```java
@Test
public void shouldCalculateDiscountForPremiumCustomer() {
    // Arrange
    Customer customer = new Customer("John", CustomerType.PREMIUM);
    DiscountCalculator calculator = new DiscountCalculator();
    double originalPrice = 100.0;
    
    // Act
    double discountedPrice = calculator.calculate(customer, originalPrice);
    
    // Assert
    assertEquals(90.0, discountedPrice, 0.01);
}
```

### Test Naming Conventions
```java
// ✅ Good
shouldReturnTrueWhenUserIsActive()
shouldThrowExceptionWhenEmailIsInvalid()
shouldCalculateCorrectTotalForMultipleItems()

// ❌ Bad
test1()
testUser()
checkSomething()
```

### Mock Dependencies
```java
@Test
public void shouldSendEmailAfterUserRegistration() {
    // Arrange
    EmailService emailService = mock(EmailService.class);
    UserRepository repository = mock(UserRepository.class);
    UserService service = new UserService(repository, emailService);
    
    User user = new User("John", "john@example.com");
    
    // Act
    service.registerUser(user);
    
    // Assert
    verify(emailService, times(1)).sendWelcomeEmail(user);
    verify(repository, times(1)).save(user);
}
```

---

## Performance Considerations

### Use Appropriate Data Structures
```java
// ❌ Bad - O(n) lookup
List<User> users = new ArrayList<>();
// Finding user requires iteration

// ✅ Good - O(1) lookup
Map<String, User> userMap = new HashMap<>();
User user = userMap.get(userId);  // Fast lookup
```

### Avoid Premature Optimization
```java
// ❌ Bad - Over-optimized, unreadable
int r = (a & b) | ((~a) & c);

// ✅ Good - Clear, readable
int result = condition ? option1 : option2;
```

### Use Lazy Loading When Appropriate
```java
class HeavyObject {
    private ExpensiveResource resource;
    
    // ✅ Good - Load only when needed
    public ExpensiveResource getResource() {
        if (resource == null) {
            resource = new ExpensiveResource();
        }
        return resource;
    }
}
```

### Caching
```java
class UserService {
    private final Map<String, User> cache = new ConcurrentHashMap<>();
    
    public User getUser(String id) {
        return cache.computeIfAbsent(id, this::loadFromDatabase);
    }
    
    private User loadFromDatabase(String id) {
        // Database call
    }
}
```

---

## Documentation

### Class Documentation
```java
/**
 * Service for managing user-related operations.
 * 
 * This service handles user registration, authentication,
 * and profile management. All methods in this class are
 * thread-safe.
 * 
 * @author John Doe
 * @version 1.0
 * @since 2024-01-01
 */
public class UserService {
    // implementation
}
```

### Method Documentation
```java
/**
 * Calculates the total price including tax and discounts.
 * 
 * @param items List of items in the order (must not be null or empty)
 * @param taxRate Tax rate as decimal (e.g., 0.08 for 8%)
 * @param discountCode Optional discount code (can be null)
 * @return Total price after tax and discounts
 * @throws IllegalArgumentException if items is null or empty
 * @throws InvalidDiscountException if discount code is invalid
 */
public double calculateTotal(List<Item> items, double taxRate, String discountCode) {
    // implementation
}
```

### Comment Best Practices
```java
// ✅ Good - Explains WHY
// Using TreeMap to maintain sorted order of transactions
// This is crucial for generating chronological reports
Map<LocalDateTime, Transaction> transactions = new TreeMap<>();

// ❌ Bad - Explains WHAT (obvious from code)
// Increment i by 1
i++;

// ✅ Good - Warning about edge case
// Note: This method is not thread-safe. Use synchronization
// if called from multiple threads
public void updateBalance(double amount) {
    // implementation
}
```

---

## Code Review Checklist

### Functionality
- [ ] Does the code work as expected?
- [ ] Are all requirements met?
- [ ] Are edge cases handled?
- [ ] Is error handling appropriate?

### Design
- [ ] Is SOLID principles followed?
- [ ] Are design patterns used appropriately?
- [ ] Is code modular and reusable?
- [ ] Is coupling minimized?
- [ ] Is cohesion maximized?

### Readability
- [ ] Are names descriptive?
- [ ] Is code self-documenting?
- [ ] Are functions small and focused?
- [ ] Is nesting minimized?
- [ ] Are magic numbers/strings avoided?

### Performance
- [ ] Are appropriate data structures used?
- [ ] Are there any obvious bottlenecks?
- [ ] Is database access optimized?
- [ ] Are resources properly released?

### Testing
- [ ] Are unit tests included?
- [ ] Do tests cover edge cases?
- [ ] Are tests clear and maintainable?
- [ ] Is code coverage adequate?

### Security
- [ ] Is input validated?
- [ ] Are SQL injections prevented?
- [ ] Are passwords encrypted?
- [ ] Are sensitive data protected?

### Maintainability
- [ ] Is code easy to understand?
- [ ] Is code well-documented?
- [ ] Can code be easily modified?
- [ ] Are dependencies minimal?

---

## Additional Tips

### DRY (Don't Repeat Yourself)
```java
// ❌ Bad
if (user.getAge() > 18 && user.hasValidLicense() && user.hasInsurance()) {
    // ...
}
if (user.getAge() > 18 && user.hasValidLicense() && user.hasInsurance()) {
    // ...
}

// ✅ Good
private boolean isEligibleDriver(User user) {
    return user.getAge() > 18 
        && user.hasValidLicense() 
        && user.hasInsurance();
}

if (isEligibleDriver(user)) {
    // ...
}
```

### KISS (Keep It Simple, Stupid)
```java
// ❌ Bad - Overcomplex
public boolean check(int x) {
    return (x & (x - 1)) == 0 && x != 0;
}

// ✅ Good - Simple and clear
public boolean isPowerOfTwo(int number) {
    return number > 0 && (number & (number - 1)) == 0;
}
```

### YAGNI (You Aren't Gonna Need It)
Don't implement features "just in case" - implement when actually needed.

---

**Remember:** Writing good code is not about being clever, it's about being clear!

Happy Coding! 🚀

