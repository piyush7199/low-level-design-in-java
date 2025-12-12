# Design Payment Wallet / Ledger System

## 1. Problem Statement and Requirements

Design a Digital Payment Wallet system (like PayPal, Venmo, or Paytm Wallet) that allows users to store money, make payments, and transfer funds. The system should maintain an accurate ledger of all transactions following accounting principles.

### Functional Requirements:

- **Wallet Management**: Create wallets, check balance, view transaction history
- **Credit Operations**: Add money to wallet (top-up, refunds)
- **Debit Operations**: Withdraw money, make payments
- **Transfers**: P2P transfers between wallets
- **Transaction History**: Complete audit trail of all operations
- **Idempotency**: Prevent duplicate transactions with idempotency keys
- **Ledger**: Double-entry bookkeeping for accounting accuracy

### Non-Functional Requirements:

- **Consistency**: Balance must always be accurate (ACID properties)
- **Atomicity**: Transfers must be all-or-nothing
- **Auditability**: Complete transaction trail for compliance
- **Concurrency**: Handle concurrent transactions on same wallet
- **Scalability**: Support millions of wallets and transactions

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might store just the balance and update it directly:

```java
class SimpleWallet {
    private String walletId;
    private double balance;
    private List<String> transactions = new ArrayList<>();
    
    public void credit(double amount) {
        balance += amount;
        transactions.add("CREDIT: " + amount);
    }
    
    public void debit(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add("DEBIT: " + amount);
        }
    }
    
    public void transfer(SimpleWallet to, double amount) {
        this.debit(amount);
        to.credit(amount);
    }
}
```

### Limitations and Design Flaws:

1. **No Atomicity**:
   - Transfer can fail midway (debited but not credited)
   - No rollback mechanism
   - Inconsistent state possible

2. **Race Conditions**:
   - Concurrent transactions can corrupt balance
   - No locking or synchronization
   - Double-spending possible

3. **Floating Point for Money**:
   - Never use `double` for currency!
   - Precision loss leads to accounting errors
   - Should use `BigDecimal` or store as cents (long)

4. **No Audit Trail**:
   - Simple string logs are not sufficient
   - No transaction IDs, timestamps, or status
   - Cannot trace issues or disputes

5. **No Idempotency**:
   - Duplicate requests can cause double charges
   - No mechanism to detect retries

6. **Violation of Accounting Principles**:
   - No double-entry bookkeeping
   - Cannot reconcile or verify balances
   - Balance derived, not validated

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Transaction processors | Different processing for credit/debit/transfer |
| **Command** | Transaction execution | Encapsulate transaction as object for undo/redo |
| **Observer** | Notifications | Notify on transaction events |
| **Factory** | Transaction creation | Create different transaction types |
| **Singleton** | Ledger service | Single source of truth |

### Double-Entry Bookkeeping:

```
┌─────────────────────────────────────────────────────────────┐
│                 DOUBLE-ENTRY ACCOUNTING                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Every transaction creates TWO ledger entries:              │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ TRANSFER: Alice → Bob ($100)                        │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ Entry 1: DEBIT  Alice's Wallet  -$100              │   │
│  │ Entry 2: CREDIT Bob's Wallet    +$100              │   │
│  │                                                     │   │
│  │ Net Effect: $0 (balanced books!)                   │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ TOP-UP: User adds $500 from bank                    │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ Entry 1: DEBIT  Bank/External Source   -$500       │   │
│  │ Entry 2: CREDIT User's Wallet          +$500       │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  Why? Sum of all debits = Sum of all credits              │
│       Can detect errors and reconcile!                     │
└─────────────────────────────────────────────────────────────┘
```

### Transaction States:

```
┌──────────┐     ┌───────────┐     ┌───────────┐
│ PENDING  │────>│ PROCESSING│────>│ COMPLETED │
└──────────┘     └───────────┘     └───────────┘
     │                │                   
     │                │                   
     ▼                ▼                   
┌──────────┐     ┌───────────┐            
│ CANCELLED│     │  FAILED   │            
└──────────┘     └───────────┘            
```

### Core Classes:

#### 1. Models Layer (`models/`)
- `Wallet` - Wallet entity with balance, status
- `WalletStatus` - Enum: ACTIVE, FROZEN, CLOSED
- `Transaction` - Transaction with amount, type, status
- `TransactionType` - Enum: CREDIT, DEBIT, TRANSFER, REFUND
- `TransactionStatus` - Enum: PENDING, COMPLETED, FAILED, CANCELLED
- `LedgerEntry` - Individual ledger entry (debit or credit)
- `EntryType` - Enum: DEBIT, CREDIT
- `Money` - Value object for currency (BigDecimal wrapper)

#### 2. Services (`services/`)
- `WalletService` - Wallet CRUD and balance operations
- `TransactionService` - Transaction processing
- `LedgerService` - Ledger management and reconciliation

#### 3. Exceptions
- `InsufficientBalanceException`
- `WalletNotFoundException`
- `DuplicateTransactionException`

### Class Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                        Wallet                               │
├─────────────────────────────────────────────────────────────┤
│ - walletId: String                                          │
│ - userId: String                                            │
│ - balance: Money                                            │
│ - status: WalletStatus                                      │
│ - createdAt: Instant                                        │
├─────────────────────────────────────────────────────────────┤
│ + credit(amount: Money): void                               │
│ + debit(amount: Money): void                                │
│ + getBalance(): Money                                       │
│ + freeze(): void                                            │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ has many
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Transaction                            │
├─────────────────────────────────────────────────────────────┤
│ - transactionId: String                                     │
│ - idempotencyKey: String                                    │
│ - type: TransactionType                                     │
│ - amount: Money                                             │
│ - sourceWalletId: String                                    │
│ - targetWalletId: String                                    │
│ - status: TransactionStatus                                 │
│ - createdAt: Instant                                        │
├─────────────────────────────────────────────────────────────┤
│ + complete(): void                                          │
│ + fail(reason: String): void                                │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ creates
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      LedgerEntry                            │
├─────────────────────────────────────────────────────────────┤
│ - entryId: String                                           │
│ - transactionId: String                                     │
│ - walletId: String                                          │
│ - entryType: EntryType (DEBIT/CREDIT)                       │
│ - amount: Money                                             │
│ - balanceAfter: Money                                       │
│ - createdAt: Instant                                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Final Design Overview

### Transaction Flow:

```
┌──────────┐    ┌───────────────┐    ┌──────────────────┐
│  Client  │───>│ TransactionSvc│───>│ Validate Request │
│ (API)    │    │               │    │ - Idempotency    │
└──────────┘    └───────────────┘    │ - Balance check  │
                                      └────────┬─────────┘
                                               │
                                               ▼
                                      ┌──────────────────┐
                                      │ Create Tx Record │
                                      │ Status: PENDING  │
                                      └────────┬─────────┘
                                               │
                                               ▼
                                      ┌──────────────────┐
                                      │ Execute Transfer │
                                      │ - Lock wallets   │
                                      │ - Debit source   │
                                      │ - Credit target  │
                                      └────────┬─────────┘
                                               │
                                               ▼
                                      ┌──────────────────┐
                                      │ Create Ledger    │
                                      │ Entries (2)      │
                                      └────────┬─────────┘
                                               │
                                               ▼
                                      ┌──────────────────┐
                                      │ Update Tx Status │
                                      │ Status: COMPLETED│
                                      └──────────────────┘
```

### Idempotency:

```
┌─────────────────────────────────────────────────────────────┐
│                     IDEMPOTENCY KEY                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Client generates unique key per transaction intent:        │
│                                                             │
│  Request 1: { idempotencyKey: "pay-123", amount: 100 }     │
│  → Creates transaction, returns TX-001                      │
│                                                             │
│  Request 2 (retry): { idempotencyKey: "pay-123", ...}      │
│  → Returns existing TX-001 (no duplicate charge!)          │
│                                                             │
│  Implementation:                                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ Map<String, Transaction> idempotencyCache;          │   │
│  │                                                     │   │
│  │ if (cache.contains(key)) {                          │   │
│  │     return cache.get(key);  // Return existing      │   │
│  │ }                                                   │   │
│  │ Transaction tx = createNew(...);                    │   │
│  │ cache.put(key, tx);                                 │   │
│  │ return tx;                                          │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Interview Discussion Points:

1. **How to handle concurrent transactions?**
   - Optimistic locking with version numbers
   - Pessimistic locking for high contention
   - Database-level row locks

2. **How to ensure atomicity in transfers?**
   - Database transactions (ACID)
   - Two-phase commit for distributed systems
   - Saga pattern with compensation

3. **How to scale to millions of wallets?**
   - Horizontal partitioning by user ID
   - Read replicas for balance queries
   - Event sourcing for transaction history

4. **How to handle failed transactions?**
   - Retry with exponential backoff
   - Dead letter queue for manual review
   - Compensation transactions for rollback

5. **Compliance and Auditing?**
   - Immutable ledger entries
   - Event log for all operations
   - Reconciliation reports

