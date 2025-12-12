package org.lld.practice.design_ecommerce_cart.naive_solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive implementation of an E-commerce Cart.
 * 
 * This demonstrates common anti-patterns:
 * - Hard-coded discount logic
 * - No quantity tracking (duplicate items)
 * - No inventory validation
 * - Using double for money
 * - Not thread-safe
 * 
 * DO NOT use this pattern in production!
 */
public class SimpleCart {
    
    private final List<Product> items = new ArrayList<>();
    
    /**
     * Add a product to cart.
     * 
     * Problems:
     * - No quantity tracking - same product added multiple times
     * - No inventory check
     * - Not thread-safe
     */
    public void addItem(Product product) {
        items.add(product);
        System.out.printf("✅ Added: %s ($%.2f)%n", product.name, product.price);
    }
    
    /**
     * Calculate total with hard-coded discount.
     * 
     * Problems:
     * - Discount logic embedded in code
     * - Cannot add new discount rules easily
     * - Uses double for money (precision issues)
     * - No coupon support
     */
    public double getTotal() {
        double subtotal = 0;
        for (Product p : items) {
            subtotal += p.price;
        }
        
        // Hard-coded discount rules - violates OCP!
        double discount = 0;
        if (subtotal > 100) {
            discount = subtotal * 0.1;  // 10% off orders over $100
            System.out.printf("   💰 Discount applied: -$%.2f%n", discount);
        }
        
        double total = subtotal - discount;
        
        // Hard-coded tax
        double tax = total * 0.08;
        
        return total + tax;
    }
    
    /**
     * Get item count.
     */
    public int getItemCount() {
        return items.size();
    }
    
    /**
     * Clear cart.
     */
    public void clear() {
        items.clear();
    }
    
    // Simple Product class
    static class Product {
        final String id;
        final String name;
        final double price;  // ❌ Never use double for money!
        
        Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }
    
    // ========== Demo ==========
    
    public static void main(String[] args) {
        System.out.println("=== Naive E-commerce Cart Demo ===\n");
        System.out.println("⚠️ This demonstrates ANTI-PATTERNS. See improved_solution for proper design.\n");
        
        SimpleCart cart = new SimpleCart();
        
        // Add items
        cart.addItem(new Product("P1", "Laptop", 899.99));
        cart.addItem(new Product("P2", "Mouse", 29.99));
        cart.addItem(new Product("P2", "Mouse", 29.99));  // Duplicate!
        cart.addItem(new Product("P3", "Keyboard", 79.99));
        
        System.out.println();
        System.out.printf("Item Count: %d%n", cart.getItemCount());
        System.out.printf("Total (with tax): $%.2f%n", cart.getTotal());
        
        System.out.println("\n⚠️ Problems demonstrated:");
        System.out.println("1. Mouse added twice as separate items (no quantity tracking)");
        System.out.println("2. Discount logic hard-coded (10% off if > $100)");
        System.out.println("3. Tax rate hard-coded (8%)");
        System.out.println("4. Using double for money (precision issues)");
        System.out.println("5. No coupon/promo code support");
        System.out.println("6. No inventory validation");
        System.out.println("7. Cannot easily add new discount rules");
    }
}

