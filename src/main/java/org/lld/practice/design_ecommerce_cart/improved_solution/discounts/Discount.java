package org.lld.practice.design_ecommerce_cart.improved_solution.discounts;

import org.lld.practice.design_ecommerce_cart.improved_solution.models.Cart;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Money;

/**
 * Interface for discount strategies.
 */
public interface Discount {
    
    /**
     * Calculate the discount amount for the cart.
     */
    Money calculate(Cart cart);
    
    /**
     * Check if this discount is applicable to the cart.
     */
    boolean isApplicable(Cart cart);
    
    /**
     * Get description of the discount.
     */
    String getDescription();
    
    /**
     * Get the discount code (for coupon-based discounts).
     */
    default String getCode() {
        return null;
    }
}

