# Design An ATM System

## 1. Problem Statement and Requirements

Our goal is to design the software for an Automated Teller Machine (ATM) that customers use for banking transactions.

### Functional Requirements:

- **Authentication:** The system must authenticate users using their card number and PIN.
- **Balance Inquiry:** Users should be able to check their account balance.
- **Cash Withdrawal:** Users should be able to withdraw cash. The ATM must validate sufficient balance and cash availability.
- **Cash Deposit:** Users should be able to deposit cash into their account.
- **Transaction History:** Users should be able to view recent transactions.
- **Cash Management:** The ATM must track its cash inventory and dispense the correct denominations.
- **Card Management:** The system must handle card insertion, validation, and ejection.
- **Security:** The system should lock accounts after multiple failed PIN attempts.

### Non-Functional Requirements:

- **Security:** All operations must be secure, with encrypted communication and proper authentication.
- **Reliability:** The system must be highly reliable and handle edge cases (insufficient funds, network issues).
- **Maintainability:** It should be easy to add new transaction types or change business rules.
- **Concurrency:** The system must handle one transaction at a time but be prepared for high frequency usage.
- **Scalability:** The design should support multiple ATMs connecting to a central banking system.

---

## 2. Naive Solution: The "Starting Point"

A beginner might create a single monolithic `ATM` class that handles everything.

### The Thought Process:

"The ATM is the main component. It needs to handle authentication, transactions, cash management, and card operations." This leads to a single class containing all logic.

```java
class ATM {
    private Map<String, Account> accounts;
    private Map<Integer, Integer> cashInventory; // denomination -> count
    private double currentBalance;
    
    public boolean authenticateUser(String cardNumber, String pin) {
        // ... validation logic ...
    }
    
    public double getBalance(String cardNumber) {
        // ... fetch and return balance ...
    }
    
    public boolean withdrawCash(String cardNumber, double amount) {
        // ... check balance, check cash availability, dispense cash ...
    }
    
    public void depositCash(String cardNumber, Map<Integer, Integer> cash) {
        // ... update account, update cash inventory ...
    }
    
    // ... many more methods ...
}
```

### Limitations and Design Flaws:

This "God Class" design has several critical issues:

- **Violation of SOLID Principles:**
    - **Single Responsibility Principle (SRP):** The `ATM` class is responsible for authentication, transaction processing, cash management, and card operations. A change in any one area affects the entire class.
    - **Open/Closed Principle (OCP):** Adding a new transaction type (e.g., fund transfer) requires modifying the `ATM` class directly.
- **Tight Coupling:** Authentication logic is tightly coupled to transaction logic and cash management.
- **Poor Testability:** You can't test cash withdrawal logic without setting up the entire ATM state including authentication and cash inventory.
- **State Management Issues:** The ATM goes through different states (idle, card inserted, PIN entered, transaction in progress), but this is handled with complex if/else logic.
- **Security Concerns:** Mixing all concerns makes it harder to properly audit and secure the authentication and transaction logic.

---

## 3. Improved Solution: The "Mentor's Guidance"

The key is to model the ATM as a collection of specialized components with clear responsibilities.

### The "Why": The State and Strategy Patterns

- **State Pattern:** An ATM operates in different states (idle, card inserted, authenticated, transaction in progress). The State pattern allows the ATM to change its behavior based on its current state. Operations like `withdrawCash()` are only valid in certain states.
- **Strategy Pattern:** Different transaction types (withdrawal, deposit, balance inquiry) can be modeled as strategies. This makes it easy to add new transaction types without modifying existing code.
- **Command Pattern:** Each transaction can be encapsulated as a Command object, allowing for easy logging, undo operations, and transaction history.

### Core Classes and Their Interactions:

We'll decompose the ATM into specialized, focused components:

1. **`ATM` (The Context):**
    - The main entry point that orchestrates the overall flow.
    - Maintains the current state and delegates operations to the state object.
    - Composes other components like `CardReader`, `CashDispenser`, and `Screen`.

2. **`ATMState` Interface (The State):**
    - Defines the contract for all possible ATM states.
    - Methods like `insertCard()`, `enterPIN()`, `selectTransaction()`, `ejectCard()`.

3. **Concrete State Classes:**
    - `IdleState`: Waiting for a card to be inserted.
    - `CardInsertedState`: Card is inserted, waiting for PIN.
    - `AuthenticatedState`: User is authenticated, can select transactions.
    - `TransactionState`: A transaction is in progress.
    - `BlockedState`: Account is locked due to failed attempts.

4. **`Transaction` Interface (The Strategy):**
    - Defines the contract for all transaction types.
    - Method `execute()` to perform the transaction.

5. **Concrete Transaction Classes:**
    - `WithdrawalTransaction`: Handles cash withdrawal logic.
    - `DepositTransaction`: Handles cash deposit logic.
    - `BalanceInquiryTransaction`: Fetches and displays balance.
    - `TransferTransaction`: Handles fund transfers.

6. **`AuthenticationService` (The Security Layer):**
    - Dedicated to user authentication.
    - Validates card numbers and PINs.
    - Tracks failed attempts and locks accounts if necessary.
    - Communicates with the backend banking system.

7. **`CashDispenser` (The Cash Manager):**
    - Manages the ATM's cash inventory.
    - Determines which denominations to dispense for a withdrawal.
    - Validates if sufficient cash is available.
    - Updates inventory after dispensing.

8. **`CardReader` (The Hardware Interface):**
    - Abstracts the physical card reader hardware.
    - Reads card information.
    - Handles card ejection.

9. **`Screen` and `Keypad` (The UI Components):**
    - Handle user input and display output.
    - Separated to allow for different UI implementations.

10. **`Account` (The Data Model):**
    - Represents a user's bank account.
    - Contains balance, account number, and transaction history.

11. **`BankingService` (The Backend Integration):**
    - Interface to communicate with the central banking system.
    - Validates accounts, processes transactions, updates balances.

---

## 4. Final Design Overview

Our final design is a modular, layered system with clear separation of concerns:

* The `ATM` acts as a context, managing the overall state machine and delegating to specialized components.
* The `ATMState` objects handle behavior specific to each state, preventing invalid operations.
* The `Transaction` strategies encapsulate different transaction types, making it easy to add new ones.
* The `AuthenticationService` handles all security concerns in one place.
* The `CashDispenser` manages the physical cash inventory independently.
* The `BankingService` abstracts backend communication, allowing for easy mocking in tests.

This design is:

- **More Maintainable:** Adding a new transaction type only requires creating a new `Transaction` class. Changing authentication logic only affects the `AuthenticationService`.
- **More Secure:** Security concerns are isolated in dedicated components, making them easier to audit and protect.
- **More Testable:** Each component can be tested independently. You can mock the `BankingService` to test transaction logic without a real backend.
- **More Robust:** The State pattern prevents invalid operations (e.g., withdrawing cash before authentication).
- **Better State Management:** The state machine is explicit and easy to understand, debug, and extend.
- **Scalable:** The design can easily support multiple ATMs connecting to the same backend through the `BankingService` interface.

### Key Design Patterns Used:

- **State Pattern:** For managing ATM states
- **Strategy Pattern:** For different transaction types
- **Command Pattern:** For transaction execution and history
- **Singleton Pattern:** For the `BankingService` (single connection point)
- **Factory Pattern:** For creating different transaction types based on user selection

