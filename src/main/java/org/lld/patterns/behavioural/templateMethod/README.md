# 📋 Template Method Design Pattern

The **Template Method Design Pattern** is a behavioral design pattern that defines the skeleton of an algorithm in a base class, allowing subclasses to override specific steps of the algorithm without changing its structure.

---

## 📑 Table of Contents

1. [✅ Definition](#-definition)
2. [🤔 Intuition](#-intuition)
3. [📌 Use Cases](#-use-cases)
4. [🧠 Key Concepts](#-key-concepts)
5. [📊 UML Diagram](#-uml-diagram)
6. [🎯 Advantages & Disadvantages](#-advantages--disadvantages)

---

## ✅ Definition

The Template Method Pattern defines the structure of an algorithm in a method, deferring some steps to subclasses. It lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.

- **Category**: Behavioral Pattern
- **Purpose**: Define algorithm skeleton, let subclasses fill in the details.

---

## 🤔 Intuition

Think of a **template** as a recipe. The recipe (template method) defines the steps: "Mix ingredients, bake, cool, serve." But each chef (subclass) can customize how they mix or bake, while following the same overall process.

**Real-world analogy**: 
- **Building Construction**: The blueprint (template) defines the steps: foundation, walls, roof, interior. But different builders can use different materials or techniques for each step.
- **Coffee Making**: The process is: grind beans, boil water, brew, serve. But you can customize the brewing method (espresso, drip, French press).

---

## 📌 Use Cases

The Template Method Pattern is ideal when:

- **Algorithm Structure**: Multiple classes share the same algorithm structure but differ in specific steps
- **Code Reuse**: Avoid code duplication by extracting common algorithm structure
- **Framework Design**: Define framework behavior, let users customize specific parts
- **Controlled Extensibility**: Want to control which parts of algorithm can be customized

**Examples:**
- **Data Processing**: Same pipeline (read, process, save) but different processing logic
- **Game Development**: Game loop structure (init, update, render) with different game logic
- **Framework Hooks**: Framework defines lifecycle, developers implement hooks
- **Builders**: Build process (compile, test, package) with different implementations

---

## 🧠 Key Concepts

1. **Abstract Class**:
   - Defines the template method (algorithm skeleton)
   - Contains both concrete and abstract methods
   - Template method is usually `final` to prevent modification

2. **Template Method**:
   - Defines the algorithm structure
   - Calls abstract methods (hooks) that subclasses implement
   - Calls concrete methods for common steps

3. **Hook Methods**:
   - Abstract methods that subclasses must implement
   - Represent variable parts of the algorithm
   - Also called "primitive operations"

4. **Concrete Classes**:
   - Implement the hook methods
   - Can optionally override concrete methods for customization

---

## 📊 UML Diagram

```
┌──────────────────────┐
│  AbstractClass       │
│  (DataProcessor)     │
├──────────────────────┤
│ +templateMethod()    │ ← Final method
│ -step1()             │ ← Concrete
│ #step2()             │ ← Abstract (hook)
│ -step3()             │ ← Concrete
│ #step4()             │ ← Optional hook
└──────────┬───────────┘
           │
           │ extends
           │
    ┌──────┴──────┬──────────────┐
    │             │              │
┌───▼────┐  ┌─────▼─────┐  ┌─────▼─────┐
│Concrete│  │Concrete   │  │Concrete   │
│Class A │  │Class B    │  │Class C    │
├────────┤  ├───────────┤  ├───────────┤
│#step2()│  │#step2()   │  │#step2()   │
│#step4()│  │#step4()   │  │           │
└────────┘  └───────────┘  └───────────┘
```

---

## 🎯 Advantages & Disadvantages

#### Advantages

- ✅ **Code Reuse**: Eliminates duplicate code by extracting common algorithm
- ✅ **Control Structure**: Base class controls algorithm flow
- ✅ **Extensibility**: Easy to add new algorithm variations
- ✅ **Inversion of Control**: Framework controls flow, developers fill in details
- ✅ **Consistency**: Ensures all subclasses follow same algorithm structure

#### Disadvantages

- ❌ **Inheritance Limitation**: Requires inheritance (can't use composition)
- ❌ **Rigid Structure**: Algorithm structure is fixed (hard to change)
- ❌ **Liskov Substitution**: Must ensure all subclasses can substitute base class
- ❌ **Limited Flexibility**: Can't easily change algorithm order

---

## 💡 Code Example

### Implementation in This Repository

**Location**: `src/main/java/org/lld/patterns/behavioural/templateMethod/`

**Key Classes**:
- `DataProcessor` - Abstract class with template method
- `CSVDataProcessor`, `JSONDataProcessor`, `XMLDataProcessor` - Concrete implementations

**Template Method Structure**:
```java
public final void processData() {
    readData();              // Concrete step
    processDataInternal();    // Abstract hook
    saveData();              // Concrete step
    notifyCompletion();      // Optional hook
}
```

**Usage**:
```java
DataProcessor processor = new CSVDataProcessor();
processor.processData();  // Follows template, customizes processing
```

---

## 🔄 Template Method vs Strategy

| Aspect | Template Method | Strategy |
|--------|-----------------|----------|
| **Structure** | Inheritance-based | Composition-based |
| **Algorithm** | Fixed structure, variable steps | Completely variable |
| **Flexibility** | Less flexible (inheritance) | More flexible (composition) |
| **Use When** | Algorithm structure is fixed | Algorithm varies completely |

---

## 🔄 Related Patterns

- **Strategy**: Template Method uses inheritance; Strategy uses composition
- **Factory Method**: Template Method often uses Factory Method for object creation
- **Hook Method**: Template Method uses hooks for customization

---

## 📚 Further Reading

- [Refactoring Guru - Template Method Pattern](https://refactoring.guru/design-patterns/template-method)
- [SourceMaking - Template Method Pattern](https://sourcemaking.com/design_patterns/template_method)
- [Gang of Four Design Patterns Book](https://en.wikipedia.org/wiki/Design_Patterns)

---

**Remember**: Use Template Method when you have an algorithm with fixed structure but variable steps!

