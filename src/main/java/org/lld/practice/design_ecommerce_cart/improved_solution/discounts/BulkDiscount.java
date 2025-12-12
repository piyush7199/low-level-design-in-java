package org.lld.practice.design_ecommerce_cart.improved_solution.discounts;

import org.lld.practice.design_ecommerce_cart.improved_solution.models.Cart;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Money;

/**
 * Bulk quantity discount (e.g., 10% off for 5+ items).
 */
public class BulkDiscount implements Discount {
    
    private final int minimumQuantity;
    private final double percentage;
    private final String description;

    public BulkDiscount(int minimumQuantity, double percentage) {
        this.minimumQuantity = minimumQuantity;
        this.percentage = percentage;
        this.description = String.format("%.0f%% off when buying %d+ items", 
                percentage, minimumQuantity);
    }

    @Override
    public Money calculate(Cart cart) {
        if (!isApplicable(cart)) {
            return Money.ZERO;
        }
        return cart.getSubtotal().percentage(percentage);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getTotalItemCount() >= minimumQuantity;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

