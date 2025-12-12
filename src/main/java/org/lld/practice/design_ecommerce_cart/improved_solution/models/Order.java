package org.lld.practice.design_ecommerce_cart.improved_solution.models;

import java.time.Instant;
import java.util.*;

/**
 * Represents a completed order.
 */
public class Order {
    
    private final String orderId;
    private final String userId;
    private final List<CartItem> items;
    private final Money subtotal;
    private final Money discount;
    private final Money tax;
    private final Money total;
    private final List<String> appliedCoupons;
    private final Instant createdAt;
    
    private OrderStatus status;

    private Order(Builder builder) {
        this.orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.userId = builder.userId;
        this.items = new ArrayList<>(builder.items);
        this.subtotal = builder.subtotal;
        this.discount = builder.discount;
        this.tax = builder.tax;
        this.total = builder.total;
        this.appliedCoupons = new ArrayList<>(builder.appliedCoupons);
        this.createdAt = Instant.now();
        this.status = OrderStatus.PENDING;
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public List<CartItem> getItems() { return Collections.unmodifiableList(items); }
    public Money getSubtotal() { return subtotal; }
    public Money getDiscount() { return discount; }
    public Money getTax() { return tax; }
    public Money getTotal() { return total; }
    public List<String> getAppliedCoupons() { return Collections.unmodifiableList(appliedCoupons); }
    public Instant getCreatedAt() { return createdAt; }
    public OrderStatus getStatus() { return status; }

    public void printReceipt() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf("║           ORDER RECEIPT - %s                 ║%n", orderId);
        System.out.println("╠══════════════════════════════════════════════════════╣");
        
        for (CartItem item : items) {
            System.out.printf("║  %-25s %3d × %-8s = %8s ║%n",
                    truncate(item.getProduct().getName(), 25),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getLineTotal());
        }
        
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.printf("║  Subtotal:                              %12s ║%n", subtotal);
        
        if (discount.isPositive()) {
            System.out.printf("║  Discount:                             -%12s ║%n", discount);
        }
        
        System.out.printf("║  Tax:                                   +%12s ║%n", tax);
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.printf("║  TOTAL:                                  %12s ║%n", total);
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 2) + "..";
    }

    // ========== Builder ==========

    public static Builder builder(String userId) {
        return new Builder(userId);
    }

    public static class Builder {
        private final String userId;
        private List<CartItem> items = new ArrayList<>();
        private Money subtotal = Money.ZERO;
        private Money discount = Money.ZERO;
        private Money tax = Money.ZERO;
        private Money total = Money.ZERO;
        private List<String> appliedCoupons = new ArrayList<>();

        public Builder(String userId) {
            this.userId = userId;
        }

        public Builder items(Collection<CartItem> items) {
            this.items = new ArrayList<>(items);
            return this;
        }

        public Builder subtotal(Money subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public Builder discount(Money discount) {
            this.discount = discount;
            return this;
        }

        public Builder tax(Money tax) {
            this.tax = tax;
            return this;
        }

        public Builder total(Money total) {
            this.total = total;
            return this;
        }

        public Builder appliedCoupons(List<String> coupons) {
            this.appliedCoupons = new ArrayList<>(coupons);
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}

