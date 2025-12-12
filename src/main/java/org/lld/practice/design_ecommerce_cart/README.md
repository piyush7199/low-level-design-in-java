# Design E-commerce Cart System

## 1. Problem Statement and Requirements

Design an E-commerce Shopping Cart system that manages products, shopping carts, pricing, discounts, and checkout. The system should support multiple pricing strategies and flexible discount rules.

### Functional Requirements:

- **Product Catalog**: Manage products with prices, categories, inventory
- **Cart Management**: Add, remove, update items in cart
- **Pricing Strategies**: Support different pricing (standard, bulk, membership)
- **Discounts & Coupons**: Apply percentage/fixed discounts, promo codes
- **Inventory Check**: Validate stock before checkout
- **Tax Calculation**: Apply tax based on region/category
- **Order Creation**: Convert cart to order at checkout

### Non-Functional Requirements:

- **Consistency**: Cart totals always accurate
- **Concurrent Access**: Handle multiple users on same cart
- **Extensibility**: Easy to add new discount rules
- **Performance**: Fast price calculations

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might store items and calculate total inline:

```java
class SimpleCart {
    private List<Product> items = new ArrayList<>();
    
    public void addItem(Product product) {
        items.add(product);
    }
    
    public double getTotal() {
        double total = 0;
        for (Product p : items) {
            total += p.getPrice();
            // Apply 10% discount if > $100
            if (total > 100) {
                total = total * 0.9;
            }
        }
        return total;
    }
}
```

### Limitations and Design Flaws:

1. **Hard-coded Discount Logic**:
   - Discount rules embedded in code
   - Cannot add new rules without code changes
   - Violates Open/Closed Principle

2. **No Quantity Tracking**:
   - Same product added multiple times
   - Inefficient storage
   - Hard to update quantities

3. **No Inventory Validation**:
   - Can add items beyond stock
   - No stock reservation

4. **Single Pricing Strategy**:
   - Cannot support bulk pricing
   - No membership discounts

5. **No Coupon Support**:
   - No promo codes
   - Cannot stack discounts

6. **Not Thread-Safe**:
   - Race conditions on concurrent cart updates

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Pricing algorithms | Different pricing for different customers |
| **Decorator** | Discount stacking | Layer multiple discounts |
| **Factory** | Discount creation | Create discounts from rules |
| **Observer** | Cart updates | Notify on cart changes |
| **Builder** | Order creation | Complex order construction |

### Pricing & Discount Architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                   PRICING PIPELINE                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Cart Items                                                 │
│      │                                                      │
│      ▼                                                      │
│  ┌─────────────────┐                                       │
│  │ Base Price      │  $100.00 × 2 = $200.00               │
│  └────────┬────────┘                                       │
│           │                                                 │
│           ▼                                                 │
│  ┌─────────────────┐                                       │
│  │ Pricing Strategy│  Bulk discount: -10% = $180.00       │
│  │ (Standard/Bulk) │                                       │
│  └────────┬────────┘                                       │
│           │                                                 │
│           ▼                                                 │
│  ┌─────────────────┐                                       │
│  │ Coupon Discount │  SAVE20: -$20 = $160.00              │
│  └────────┬────────┘                                       │
│           │                                                 │
│           ▼                                                 │
│  ┌─────────────────┐                                       │
│  │ Membership      │  Gold: -5% = $152.00                 │
│  └────────┬────────┘                                       │
│           │                                                 │
│           ▼                                                 │
│  ┌─────────────────┐                                       │
│  │ Tax Calculation │  +8% tax = $164.16                   │
│  └────────┬────────┘                                       │
│           │                                                 │
│           ▼                                                 │
│     Final Total: $164.16                                   │
└─────────────────────────────────────────────────────────────┘
```

### Discount Strategies:

```
┌─────────────────────────────────────────────────────────────┐
│                   DISCOUNT TYPES                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. PERCENTAGE DISCOUNT                                     │
│     - 20% off entire cart                                  │
│     - 15% off specific category                            │
│                                                             │
│  2. FIXED AMOUNT DISCOUNT                                   │
│     - $10 off order over $50                               │
│     - $25 off first order                                  │
│                                                             │
│  3. BUY X GET Y                                             │
│     - Buy 2 Get 1 Free                                     │
│     - Buy 3 Get 50% off 4th                                │
│                                                             │
│  4. BULK DISCOUNT                                           │
│     - 5+ items: 5% off                                     │
│     - 10+ items: 10% off                                   │
│                                                             │
│  5. MEMBERSHIP DISCOUNT                                     │
│     - Silver: 5% off                                       │
│     - Gold: 10% off                                        │
│     - Platinum: 15% off                                    │
└─────────────────────────────────────────────────────────────┘
```

### Core Classes:

#### 1. Models (`models/`)
- `Product` - Product with price, category, inventory
- `CartItem` - Product with quantity in cart
- `Cart` - Shopping cart with items
- `Order` - Completed order
- `Coupon` - Discount coupon

#### 2. Pricing (`pricing/`)
- `PricingStrategy` - Interface for pricing algorithms
- `StandardPricing` - Regular price
- `BulkPricingStrategy` - Volume discounts
- `MembershipPricingStrategy` - Member discounts

#### 3. Discounts (`discounts/`)
- `Discount` - Interface for discounts
- `PercentageDiscount` - Percentage off
- `FixedAmountDiscount` - Fixed amount off
- `BuyXGetYDiscount` - Buy X get Y free

#### 4. Services (`services/`)
- `CartService` - Cart operations
- `PricingService` - Calculate prices
- `InventoryService` - Stock management

### Class Diagram:

```
┌─────────────────────────────────────────────────────────────┐
│                         Cart                                │
├─────────────────────────────────────────────────────────────┤
│ - cartId: String                                            │
│ - userId: String                                            │
│ - items: Map<String, CartItem>                             │
│ - appliedCoupons: List<Coupon>                             │
├─────────────────────────────────────────────────────────────┤
│ + addItem(product, quantity): void                         │
│ + removeItem(productId): void                              │
│ + updateQuantity(productId, quantity): void                │
│ + getSubtotal(): Money                                     │
│ + applyCoupon(code): boolean                               │
│ + clear(): void                                            │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ contains
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       CartItem                              │
├─────────────────────────────────────────────────────────────┤
│ - product: Product                                          │
│ - quantity: int                                             │
│ - unitPrice: Money                                          │
├─────────────────────────────────────────────────────────────┤
│ + getLineTotal(): Money                                    │
│ + incrementQuantity(n): void                               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                 <<interface>>                               │
│                   Discount                                  │
├─────────────────────────────────────────────────────────────┤
│ + apply(cart): Money   // Returns discount amount          │
│ + isApplicable(cart): boolean                              │
│ + getDescription(): String                                 │
└─────────────────────────────────────────────────────────────┘
                              △
          ┌───────────────────┼───────────────────┐
          │                   │                   │
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│Percentage       │  │FixedAmount      │  │BuyXGetY         │
│Discount         │  │Discount         │  │Discount         │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

---

## 4. Final Design Overview

### Checkout Flow:

```
┌──────────┐    ┌───────────────┐    ┌──────────────────┐
│   Cart   │───>│ Validate Stock│───>│ Apply Discounts  │
│          │    │               │    │ (Strategy chain) │
└──────────┘    └───────────────┘    └────────┬─────────┘
                                              │
                                              ▼
                                     ┌──────────────────┐
                                     │ Calculate Tax    │
                                     └────────┬─────────┘
                                              │
                                              ▼
                                     ┌──────────────────┐
                                     │ Create Order     │
                                     │ (Builder)        │
                                     └────────┬─────────┘
                                              │
                                              ▼
                                     ┌──────────────────┐
                                     │ Reserve Stock    │
                                     │ Clear Cart       │
                                     └──────────────────┘
```

### Interview Discussion Points:

1. **How to handle concurrent cart access?**
   - Optimistic locking with version numbers
   - Database transactions
   - Redis for distributed carts

2. **How to prevent coupon abuse?**
   - One coupon per user validation
   - Usage limit tracking
   - Time-based expiration

3. **How to handle flash sales?**
   - Pre-reserve inventory
   - Queue-based checkout
   - Rate limiting

4. **How to support dynamic pricing?**
   - Price version tracking
   - Real-time price updates
   - Price lock on cart add

5. **How to scale cart operations?**
   - Session-based carts (Redis)
   - Microservice architecture
   - Event-driven inventory updates

