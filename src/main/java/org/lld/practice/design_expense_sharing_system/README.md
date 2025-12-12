# Design An Expense Sharing System (Splitwise-like)

## 1. Problem Statement and Requirements

Our goal is to design an expense sharing system that allows users to split expenses among groups, track who owes whom, and simplify debts through settlement.

### Functional Requirements:

- **User Management**: User registration and profile management
- **Group Creation**: Create groups and add/remove members
- **Expense Creation**: Add expenses with amount, description, paid by, and split among participants
- **Split Strategies**: Support different split types (equal, percentage, exact amount, shares)
- **Balance Tracking**: Track how much each user owes or is owed
- **Settlement**: Simplify debts (if A owes B $10 and B owes A $5, simplify to A owes B $5)
- **Transaction History**: View expense history for groups and users
- **Settle Up**: Mark expenses as settled when paid

### Non-Functional Requirements:

- **Accuracy**: Correct balance calculations
- **Performance**: Fast balance queries and debt simplification
- **Scalability**: Support millions of users and groups
- **Consistency**: Ensure data consistency across operations

---

## 2. Naive Solution: The "Starting Point"

A beginner might store all transactions and calculate balances on the fly.

### The Thought Process:

"I'll store all expenses and calculate who owes whom by iterating through all transactions."

```java
class ExpenseSharing {
    private List<Expense> expenses = new ArrayList<>();
    
    public void addExpense(String paidBy, double amount, List<String> participants) {
        double perPerson = amount / participants.size();
        expenses.add(new Expense(paidBy, amount, participants, perPerson));
    }
    
    public Map<String, Double> getBalances() {
        Map<String, Double> balances = new HashMap<>();
        for (Expense expense : expenses) {
            // Calculate balances...
        }
        return balances;
    }
}
```

### Limitations and Design Flaws:

1. **Performance Issues**: Recalculating balances from all transactions is expensive
2. **No Debt Simplification**: Doesn't simplify circular debts
3. **No Split Strategies**: Only supports equal split
4. **No Group Management**: Cannot create groups
5. **No Settlement Tracking**: Cannot mark expenses as settled
6. **Complex Queries**: Finding who owes whom requires full scan

---

## 3. Improved Solution: The "Mentor's Guidance"

Use graph-based debt tracking, strategy pattern for splits, and efficient balance calculation.

### Design Patterns Used:

1. **Strategy Pattern**: For different expense split strategies (equal, percentage, exact, shares)
2. **Factory Pattern**: For creating different types of splits
3. **Graph Algorithm**: For debt simplification and balance calculation
4. **Repository Pattern**: For expense and group storage

### Core Classes and Interactions:

1. **ExpenseSharingService** (Orchestrator):
   - Main entry point for expense operations
   - Manages groups, expenses, and balance calculations

2. **SplitStrategy** (Strategy Interface):
   - Defines how to split expenses
   - Implementations: EqualSplitStrategy, PercentageSplitStrategy, ExactSplitStrategy

3. **Expense** (Model):
   - Represents an expense with all metadata
   - Tracks who paid and how it's split

4. **BalanceGraph** (Graph Algorithm):
   - Maintains debt graph between users
   - Simplifies circular debts

5. **Group** (Model):
   - Represents a group of users
   - Tracks expenses and balances

### Key Design Benefits:

- **Performance**: Efficient balance calculation using graph algorithms
- **Flexibility**: Strategy pattern allows different split types
- **Debt Simplification**: Reduces number of transactions needed
- **Extensibility**: Easy to add new split strategies

---

## 4. Final Design Overview

The improved solution uses Strategy pattern for split calculations, graph algorithms for debt simplification, and efficient data structures to create a scalable expense sharing system.

