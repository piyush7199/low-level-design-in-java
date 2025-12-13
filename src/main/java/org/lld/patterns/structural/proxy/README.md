# 🎭 Proxy Design Pattern

The **Proxy Design Pattern** is a structural design pattern that provides a surrogate or placeholder for another object to control access to it. It acts as an intermediary between the client and the real object.

---

## 📑 Table of Contents

1. [✅ Definition](#-definition)
2. [🤔 Intuition](#-intuition)
3. [📌 Use Cases](#-use-cases)
4. [🧠 Key Concepts](#-key-concepts)
5. [📊 UML Diagram](#-uml-diagram)
6. [🎯 Advantages & Disadvantages](#-advantages--disadvantages)
7. [🔍 Types of Proxies](#-types-of-proxies)

---

## ✅ Definition

The Proxy Pattern provides a placeholder for another object to control access to it. The proxy object has the same interface as the real object, allowing it to be used interchangeably.

- **Category**: Structural Pattern
- **Purpose**: Control access to an object, add functionality, or optimize performance.

---

## 🤔 Intuition

Think of a proxy as a **middleman** or **representative**. Just like a real estate agent represents a property owner, a proxy represents another object. The proxy can:

- **Delay expensive operations** (lazy loading)
- **Control access** (protection proxy)
- **Add functionality** (logging, caching)
- **Provide remote access** (remote proxy)

**Real-world analogy**: When you want to access a website, your browser might use a proxy server. The proxy can cache content, filter requests, or provide security, all while appearing transparent to you.

---

## 📌 Use Cases

The Proxy Pattern is ideal when:

- **Lazy Loading**: Delay expensive object creation until it's actually needed
- **Access Control**: Restrict access to sensitive objects (protection proxy)
- **Remote Access**: Represent objects in different address spaces (remote proxy)
- **Caching**: Cache results of expensive operations
- **Logging**: Add logging or monitoring without modifying the real object
- **Virtual Proxy**: Create expensive objects on demand

**Examples:**
- **Image Loading**: Load images only when displayed (lazy loading)
- **Database Connections**: Create connections only when needed
- **Security**: Control access to sensitive resources
- **API Wrappers**: Add rate limiting, caching, or authentication

---

## 🧠 Key Concepts

1. **Subject Interface**:
   - Defines the common interface for both RealSubject and Proxy
   - Allows proxy to be used interchangeably with real object

2. **Real Subject**:
   - The actual object that performs the real work
   - May be expensive to create or access

3. **Proxy**:
   - Maintains a reference to the real subject
   - Controls access to the real subject
   - Implements the same interface as the real subject

4. **Client**:
   - Interacts with the proxy as if it were the real subject
   - Unaware of the proxy's existence

---

## 📊 UML Diagram

```
┌─────────────┐
│   Subject   │
│  (Image)    │
├─────────────┤
│ +display()  │
└──────┬──────┘
       │
       │ implements
       │
       ├──────────────────┐
       │                  │
┌──────▼──────┐    ┌──────▼──────┐
│ RealSubject │    │    Proxy    │
│(RealImage)  │    │(ImageProxy)  │
├─────────────┤    ├─────────────┤
│ +display()  │    │ -realImage  │
└─────────────┘    │ +display()  │
                   └──────┬──────┘
                          │
                          │ uses
                          │
                   ┌──────▼──────┐
                   │ RealImage   │
                   └─────────────┘
```

---

## 🎯 Advantages & Disadvantages

#### Advantages

- ✅ **Lazy Loading**: Defer expensive object creation until needed
- ✅ **Access Control**: Add security or permission checks
- ✅ **Performance**: Cache results, reduce object creation
- ✅ **Separation of Concerns**: Add functionality without modifying real object
- ✅ **Remote Access**: Represent objects in different address spaces

#### Disadvantages

- ❌ **Complexity**: Adds an extra layer of indirection
- ❌ **Performance Overhead**: Additional method calls
- ❌ **Debugging**: Can make debugging more difficult

---

## 🔍 Types of Proxies

### 1. **Virtual Proxy** (Lazy Loading)
Delays creation of expensive objects until needed.

```java
// Example: Image proxy loads image only when displayed
Image image = new ImageProxy("large-photo.jpg");  // No loading yet
image.display();  // Now loads the image
```

### 2. **Protection Proxy**
Controls access to sensitive objects based on permissions.

```java
// Example: Bank account proxy checks permissions
Account account = new ProtectedAccount(user);
account.withdraw(1000);  // Checks if user has permission
```

### 3. **Remote Proxy**
Represents an object in a different address space (network, JVM).

```java
// Example: Remote service proxy
UserService userService = new RemoteUserService("http://api.example.com");
User user = userService.getUser(123);  // Network call hidden
```

### 4. **Caching Proxy**
Caches results of expensive operations.

```java
// Example: Database query proxy with caching
UserService userService = new CachedUserService();
User user = userService.getUser(123);  // First call: database
User user2 = userService.getUser(123); // Second call: cache
```

### 5. **Smart Reference Proxy**
Adds additional functionality like reference counting, logging.

```java
// Example: Logging proxy
FileService fileService = new LoggingFileService();
fileService.read("file.txt");  // Logs the operation
```

---

## 💡 Code Example

### Implementation in This Repository

**Location**: `src/main/java/org/lld/patterns/structural/proxy/`

**Key Classes**:
- `Image` - Subject interface
- `RealImage` - Real subject (expensive to create)
- `ImageProxy` - Proxy (lazy loading)

**Usage**:
```java
// Create proxy (no loading yet)
Image image = new ImageProxy("photo.jpg");

// Load and display (lazy loading)
image.display();
```

---

## 🔄 Related Patterns

- **Adapter**: Changes interface; Proxy maintains same interface
- **Decorator**: Adds behavior; Proxy controls access
- **Facade**: Simplifies interface; Proxy provides same interface

---

## 📚 Further Reading

- [Refactoring Guru - Proxy Pattern](https://refactoring.guru/design-patterns/proxy)
- [SourceMaking - Proxy Pattern](https://sourcemaking.com/design_patterns/proxy)
- [Gang of Four Design Patterns Book](https://en.wikipedia.org/wiki/Design_Patterns)

---

**Remember**: Use Proxy when you need to control access to an object, add functionality, or optimize performance without changing the client code!

