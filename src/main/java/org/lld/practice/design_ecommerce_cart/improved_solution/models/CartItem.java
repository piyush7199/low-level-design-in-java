package org.lld.practice.design_ecommerce_cart.improved_solution.models;

import java.util.Objects;

/**
 * Represents an item in the shopping cart.
 */
public class CartItem {
    
    private final Product product;
    private int quantity;
    private final Money unitPrice;  // Price at time of adding (price lock)

    public CartItem(Product product, int quantity) {
        this.product = Objects.requireNonNull(product);
        this.quantity = quantity;
        this.unitPrice = product.getPrice();  // Lock the price
    }

    public void incrementQuantity(int amount) {
        this.quantity += amount;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public Money getLineTotal() {
        return unitPrice.multiply(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public String getProductId() {
        return product.getProductId();
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return String.format("CartItem{product='%s', qty=%d, unit=%s, total=%s}",
                product.getName(), quantity, unitPrice, getLineTotal());
    }
}

