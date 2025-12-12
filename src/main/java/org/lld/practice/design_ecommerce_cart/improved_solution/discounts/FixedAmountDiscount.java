package org.lld.practice.design_ecommerce_cart.improved_solution.discounts;

import org.lld.practice.design_ecommerce_cart.improved_solution.models.Cart;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Money;

/**
 * Fixed amount discount (e.g., $10 off).
 */
public class FixedAmountDiscount implements Discount {
    
    private final String code;
    private final Money amount;
    private final Money minimumOrder;
    private final String description;

    public FixedAmountDiscount(String code, Money amount, Money minimumOrder) {
        this.code = code;
        this.amount = amount;
        this.minimumOrder = minimumOrder;
        this.description = String.format("%s off orders over %s", amount, minimumOrder);
    }

    public FixedAmountDiscount(Money amount, Money minimumOrder) {
        this(null, amount, minimumOrder);
    }

    @Override
    public Money calculate(Cart cart) {
        if (!isApplicable(cart)) {
            return Money.ZERO;
        }
        // Don't exceed cart subtotal
        Money subtotal = cart.getSubtotal();
        return amount.isGreaterThan(subtotal) ? subtotal : amount;
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

