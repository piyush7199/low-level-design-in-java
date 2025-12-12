package org.lld.practice.design_ecommerce_cart.improved_solution;

import org.lld.practice.design_ecommerce_cart.improved_solution.discounts.*;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.*;
import org.lld.practice.design_ecommerce_cart.improved_solution.services.*;

/**
 * Demo application for the E-commerce Cart System.
 * 
 * Demonstrates:
 * - Product catalog and inventory
 * - Cart management (add, remove, update)
 * - Multiple discount types
 * - Coupon codes
 * - Tax calculation
 * - Order checkout
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     🛒 E-COMMERCE CART SYSTEM - DEMO                           ║");
        System.out.println("║     Features: Discounts, Coupons, Inventory, Checkout         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // Initialize services
        InventoryService inventoryService = new InventoryService();
        CartService cartService = new CartService(inventoryService);
        PricingService pricingService = new PricingService();
        
        // Setup
        setupProducts(inventoryService);
        setupDiscounts(pricingService);
        
        // Demo 1: Basic cart operations
        demoCartOperations(cartService, inventoryService);
        
        // Demo 2: Discounts
        demoDiscounts(cartService, pricingService, inventoryService);
        
        // Demo 3: Coupons
        demoCoupons(cartService, pricingService, inventoryService);
        
        // Demo 4: Checkout
        demoCheckout(cartService, pricingService, inventoryService);
        
        printSummary();
    }
    
    private static void setupProducts(InventoryService inventory) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("SETUP: Product Catalog");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        inventory.addProduct(new Product("P001", "MacBook Pro", Category.ELECTRONICS, 
                Money.of(1299.99), 10));
        inventory.addProduct(new Product("P002", "Wireless Mouse", Category.ELECTRONICS, 
                Money.of(29.99), 50));
        inventory.addProduct(new Product("P003", "USB-C Hub", Category.ELECTRONICS, 
                Money.of(49.99), 30));
        inventory.addProduct(new Product("P004", "Mechanical Keyboard", Category.ELECTRONICS, 
                Money.of(89.99), 25));
        inventory.addProduct(new Product("P005", "Monitor Stand", Category.HOME, 
                Money.of(39.99), 40));
        inventory.addProduct(new Product("P006", "Desk Lamp", Category.HOME, 
                Money.of(24.99), 35));
        
        System.out.println("📦 Added 6 products to catalog");
        System.out.println();
    }
    
    private static void setupDiscounts(PricingService pricingService) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("SETUP: Discount Rules");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // Automatic discounts
        pricingService.addAutomaticDiscount(new BulkDiscount(3, 5));  // 5% off for 3+ items
        
        // Coupon discounts
        pricingService.registerCoupon(new PercentageDiscount("SAVE10", 10, Money.of(50)));
        pricingService.registerCoupon(new PercentageDiscount("SAVE20", 20, Money.of(200)));
        pricingService.registerCoupon(new FixedAmountDiscount("FLAT25", Money.of(25), Money.of(100)));
        
        System.out.println();
    }
    
    private static void demoCartOperations(CartService cartService, InventoryService inventory) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 1: Cart Operations");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        String userId = "alice";
        
        // Add items to cart
        cartService.addToCart(userId, inventory.getProductOrThrow("P002"), 2);  // 2 mice
        cartService.addToCart(userId, inventory.getProductOrThrow("P003"), 1);  // 1 hub
        cartService.addToCart(userId, inventory.getProductOrThrow("P006"), 1);  // 1 lamp
        
        cartService.printCart(userId);
        
        // Update quantity
        System.out.println("\nUpdating mouse quantity to 3...");
        cartService.updateQuantity(userId, "P002", 3);
        
        // Remove item
        System.out.println("Removing desk lamp...");
        cartService.removeFromCart(userId, "P006");
        
        cartService.printCart(userId);
        cartService.clearCart(userId);
        System.out.println();
    }
    
    private static void demoDiscounts(CartService cartService, PricingService pricingService, 
                                      InventoryService inventory) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 2: Automatic Discounts (Bulk Discount)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        String userId = "bob";
        
        // Add items (less than 3 - no bulk discount)
        cartService.addToCart(userId, inventory.getProductOrThrow("P002"), 1);
        cartService.addToCart(userId, inventory.getProductOrThrow("P003"), 1);
        
        cartService.printCart(userId);
        
        Cart cart = cartService.getCart(userId).orElseThrow();
        PricingService.PricingResult result1 = pricingService.calculateTotal(cart);
        result1.print();
        
        // Add more items to trigger bulk discount
        System.out.println("\nAdding more items to trigger bulk discount (3+ items = 5% off)...");
        cartService.addToCart(userId, inventory.getProductOrThrow("P004"), 1);
        cartService.addToCart(userId, inventory.getProductOrThrow("P005"), 1);
        
        cartService.printCart(userId);
        
        PricingService.PricingResult result2 = pricingService.calculateTotal(cart);
        result2.print();
        
        cartService.clearCart(userId);
        System.out.println();
    }
    
    private static void demoCoupons(CartService cartService, PricingService pricingService, 
                                    InventoryService inventory) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 3: Coupon Codes");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        String userId = "charlie";
        
        // Add items
        cartService.addToCart(userId, inventory.getProductOrThrow("P001"), 1);  // MacBook
        cartService.addToCart(userId, inventory.getProductOrThrow("P002"), 2);  // 2 mice
        
        cartService.printCart(userId);
        
        // Calculate without coupon
        Cart cart = cartService.getCart(userId).orElseThrow();
        System.out.println("Without coupon:");
        pricingService.calculateTotal(cart).print();
        
        // Apply coupon
        System.out.println("\nApplying coupon SAVE20 (20% off orders over $200)...");
        cartService.applyCoupon(userId, "SAVE20");
        
        PricingService.PricingResult result = pricingService.calculateTotal(cart);
        result.print();
        
        cartService.clearCart(userId);
        System.out.println();
    }
    
    private static void demoCheckout(CartService cartService, PricingService pricingService, 
                                     InventoryService inventory) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO 4: Checkout and Order Creation");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        String userId = "diana";
        
        // Add items
        cartService.addToCart(userId, inventory.getProductOrThrow("P003"), 2);  // 2 hubs
        cartService.addToCart(userId, inventory.getProductOrThrow("P004"), 1);  // 1 keyboard
        cartService.addToCart(userId, inventory.getProductOrThrow("P005"), 1);  // 1 stand
        
        // Apply coupon
        cartService.applyCoupon(userId, "FLAT25");
        
        cartService.printCart(userId);
        
        // Checkout
        Cart cart = cartService.getCart(userId).orElseThrow();
        System.out.println("\nProcessing checkout...");
        
        Order order = pricingService.checkout(cart);
        order.printReceipt();
        
        System.out.printf("✅ Order %s created successfully!%n", order.getOrderId());
        System.out.printf("   Status: %s%n", order.getStatus());
    }
    
    private static void printSummary() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("DEMO COMPLETE - KEY CONCEPTS DEMONSTRATED:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("✅ Money Class: BigDecimal for precision");
        System.out.println("✅ Cart Management: Add, remove, update quantities");
        System.out.println("✅ Strategy Pattern: Different discount types");
        System.out.println("   - PercentageDiscount (e.g., 20% off)");
        System.out.println("   - FixedAmountDiscount (e.g., $25 off)");
        System.out.println("   - BulkDiscount (e.g., 5% off 3+ items)");
        System.out.println("✅ Coupon System: Promo code validation");
        System.out.println("✅ Tax Calculation: Configurable tax rate");
        System.out.println("✅ Order Builder: Complex order construction");
        System.out.println();
        System.out.println("🎯 Interview Discussion Points:");
        System.out.println("   - Concurrent cart access handling");
        System.out.println("   - Flash sale with inventory locking");
        System.out.println("   - Distributed cart (Redis)");
        System.out.println("   - Dynamic pricing and A/B testing");
    }
}

