package org.lld.practice.design_ecommerce_cart.improved_solution.models;

import java.util.Objects;

/**
 * Represents a product in the catalog.
 */
public class Product {
    
    private final String productId;
    private final String name;
    private final String description;
    private final Category category;
    private final Money price;
    private int stockQuantity;

    public Product(String productId, String name, Category category, Money price, int stockQuantity) {
        this.productId = Objects.requireNonNull(productId);
        this.name = Objects.requireNonNull(name);
        this.description = "";
        this.category = Objects.requireNonNull(category);
        this.price = Objects.requireNonNull(price);
        this.stockQuantity = stockQuantity;
    }

    public Product(String productId, String name, String description, 
                   Category category, Money price, int stockQuantity) {
        this.productId = Objects.requireNonNull(productId);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.category = Objects.requireNonNull(category);
        this.price = Objects.requireNonNull(price);
        this.stockQuantity = stockQuantity;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public boolean hasStock(int quantity) {
        return stockQuantity >= quantity;
    }

    public void reduceStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new IllegalStateException("Insufficient stock for " + name);
        }
        stockQuantity -= quantity;
    }

    public void addStock(int quantity) {
        stockQuantity += quantity;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Money getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%s, stock=%d}",
                productId, name, price, stockQuantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}

