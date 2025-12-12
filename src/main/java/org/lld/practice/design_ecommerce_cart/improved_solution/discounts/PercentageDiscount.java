package org.lld.practice.design_ecommerce_cart.improved_solution.discounts;

import org.lld.practice.design_ecommerce_cart.improved_solution.models.Cart;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Money;

/**
 * Percentage-based discount (e.g., 20% off).
 */
public class PercentageDiscount implements Discount {
    
    private final String code;
    private final double percentage;
    private final Money minimumOrder;
    private final String description;

    public PercentageDiscount(String code, double percentage, Money minimumOrder) {
        this.code = code;
        this.percentage = percentage;
        this.minimumOrder = minimumOrder;
        this.description = String.format("%.0f%% off orders over %s", percentage, minimumOrder);
    }

    public PercentageDiscount(double percentage, Money minimumOrder) {
        this(null, percentage, minimumOrder);
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
        return cart.getSubtotal().isGreaterThan(minimumOrder) || 
               cart.getSubtotal().equals(minimumOrder);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCode() {
        return code;
    }
}

