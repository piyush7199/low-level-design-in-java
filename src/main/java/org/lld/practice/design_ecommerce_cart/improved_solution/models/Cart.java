package org.lld.practice.design_ecommerce_cart.improved_solution.models;

import java.time.Instant;
import java.util.*;

/**
 * Represents a shopping cart.
 */
public class Cart {
    
    private final String cartId;
    private final String userId;
    private final Map<String, CartItem> items;
    private final List<String> appliedCouponCodes;
    private final Instant createdAt;
    private Instant updatedAt;

    public Cart(String userId) {
        this.cartId = "CART-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.userId = Objects.requireNonNull(userId);
        this.items = new LinkedHashMap<>();  // Maintain insertion order
        this.appliedCouponCodes = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    // ========== Item Operations ==========

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        String productId = product.getProductId();
        if (items.containsKey(productId)) {
            items.get(productId).incrementQuantity(quantity);
        } else {
            items.put(productId, new CartItem(product, quantity));
        }
        updatedAt = Instant.now();
    }

    public void removeItem(String productId) {
        items.remove(productId);
        updatedAt = Instant.now();
    }

    public void updateQuantity(String productId, int quantity) {
        CartItem item = items.get(productId);
        if (item != null) {
            if (quantity <= 0) {
                items.remove(productId);
            } else {
                item.setQuantity(quantity);
            }
            updatedAt = Instant.now();
        }
    }

    public void clear() {
        items.clear();
        appliedCouponCodes.clear();
        updatedAt = Instant.now();
    }

    // ========== Coupon Operations ==========

    public void applyCoupon(String couponCode) {
        if (!appliedCouponCodes.contains(couponCode)) {
            appliedCouponCodes.add(couponCode);
            updatedAt = Instant.now();
        }
    }

    public void removeCoupon(String couponCode) {
        appliedCouponCodes.remove(couponCode);
        updatedAt = Instant.now();
    }

    // ========== Calculations ==========

    public Money getSubtotal() {
        return items.values().stream()
                .map(CartItem::getLineTotal)
                .reduce(Money.ZERO, Money::add);
    }

    public int getTotalItemCount() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public int getUniqueItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // ========== Getters ==========

    public String getCartId() {
        return cartId;
    }

    public String getUserId() {
        return userId;
    }

    public Collection<CartItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public Optional<CartItem> getItem(String productId) {
        return Optional.ofNullable(items.get(productId));
    }

    public List<String> getAppliedCouponCodes() {
        return Collections.unmodifiableList(appliedCouponCodes);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return String.format("Cart{id='%s', user='%s', items=%d, subtotal=%s}",
                cartId, userId, getTotalItemCount(), getSubtotal());
    }
}

