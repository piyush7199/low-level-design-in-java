# 🧱 Object-Oriented Programming (OOP)

OOP models real-world entities as objects with properties (data) and behaviors (methods), forming the foundation of LLD.

### Core Concepts

OOP organizes code into reusable, modular units, mimicking real-world relationships.

### 🔑 Four Pillars of OOP

**1. Encapsulation**
→ Hides internal details, exposing only what’s needed—like a bank vault protecting its contents.

```java
class BankAccount {
    private double balance; // Hidden from outside

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
```

**2. Abstraction**
→ Shows essential details, hiding complexity—like a car’s dashboard concealing engine mechanics.

```java
abstract class Vehicle {
    abstract void move(); // Hides implementation
}

class Car extends Vehicle {
    void move() {
        System.out.println("Car moves on four wheels");
    }
}
```

**3. Inheritance**
→ Allows a class to inherit properties and behaviors (IS-A relationship).

```java
class Animal {
    void sound() {
        System.out.println("Some sound");
    }
}

class Dog extends Animal {
    void sound() {
        System.out.println("Bark");
    }
}
```

**4. Polymorphism**
→ Enables one interface to represent multiple forms, like a remote controlling different devices.

```java
public class Main {
    public static void main(String[] args) {
        Animal animal = new Dog(); // Dog behaves as Animal
        animal.sound(); // Outputs: Bark (runtime polymorphism)
    }
}
```

### Code Examples

See above for practical implementations of each pillar.

### Common Pitfalls to Avoid

- **Overusing Inheritance:** Leads to tight coupling; prefer composition.
- **Ignoring Encapsulation:** Exposing internals breaks abstraction.
