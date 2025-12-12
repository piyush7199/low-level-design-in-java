package org.lld.practice.design_ecommerce_cart.improved_solution.services;

import org.lld.practice.design_ecommerce_cart.improved_solution.models.Cart;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.CartItem;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing shopping carts.
 */
public class CartService {
    
    private final Map<String, Cart> carts;  // userId -> Cart
    private final InventoryService inventoryService;

    public CartService(InventoryService inventoryService) {
        this.carts = new HashMap<>();
        this.inventoryService = inventoryService;
    }

    public Cart getOrCreateCart(String userId) {
        return carts.computeIfAbsent(userId, Cart::new);
    }

    public Optional<Cart> getCart(String userId) {
        return Optional.ofNullable(carts.get(userId));
    }

    public void addToCart(String userId, Product product, int quantity) {
        Cart cart = getOrCreateCart(userId);
        
        // Check inventory
        int currentInCart = cart.getItem(product.getProductId())
                .map(CartItem::getQuantity)
                .orElse(0);
        
        if (!inventoryService.hasStock(product.getProductId(), currentInCart + quantity)) {
            throw new IllegalStateException("Insufficient stock for " + product.getName());
        }
        
        cart.addItem(product, quantity);
        System.out.printf("🛒 Added to cart: %s × %d%n", product.getName(), quantity);
    }

    public void removeFromCart(String userId, String productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItem(productId).ifPresent(item -> {
            cart.removeItem(productId);
            System.out.printf("🗑️ Removed from cart: %s%n", item.getProduct().getName());
        });
    }

    public void updateQuantity(String userId, String productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        
        if (quantity > 0 && !inventoryService.hasStock(productId, quantity)) {
            throw new IllegalStateException("Insufficient stock");
        }
        
        cart.updateQuantity(productId, quantity);
        System.out.printf("✏️ Updated quantity: %d%n", quantity);
    }

    public boolean applyCoupon(String userId, String couponCode) {
        Cart cart = getOrCreateCart(userId);
        cart.applyCoupon(couponCode);
        System.out.printf("🎟️ Applied coupon: %s%n", couponCode);
        return true;
    }

    public void clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.clear();
        System.out.println("🗑️ Cart cleared");
    }

    public void printCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf("║                    SHOPPING CART                      ║%n");
        System.out.printf("║  User: %-47s ║%n", userId);
        System.out.println("╠══════════════════════════════════════════════════════╣");
        
        if (cart.isEmpty()) {
            System.out.println("║  (empty)                                             ║");
        } else {
            for (CartItem item : cart.getItems()) {
                System.out.printf("║  %-25s %3d × %-8s = %8s ║%n",
                        truncate(item.getProduct().getName(), 25),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal());
            }
            System.out.println("╠══════════════════════════════════════════════════════╣");
            System.out.printf("║  Subtotal: %42s ║%n", cart.getSubtotal());
            
            if (!cart.getAppliedCouponCodes().isEmpty()) {
                System.out.printf("║  Coupons: %-43s ║%n", 
                        String.join(", ", cart.getAppliedCouponCodes()));
            }
        }
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 2) + "..";
    }
}

