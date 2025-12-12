package org.lld.practice.design_ecommerce_cart.improved_solution.services;

import org.lld.practice.design_ecommerce_cart.improved_solution.discounts.Discount;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Cart;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Money;
import org.lld.practice.design_ecommerce_cart.improved_solution.models.Order;

import java.util.*;

/**
 * Service for calculating prices, discounts, and taxes.
 */
public class PricingService {
    
    private static final double DEFAULT_TAX_RATE = 0.08;  // 8% tax
    
    private final Map<String, Discount> couponDiscounts;  // code -> discount
    private final List<Discount> automaticDiscounts;      // Applied automatically
    private double taxRate;

    public PricingService() {
        this.couponDiscounts = new HashMap<>();
        this.automaticDiscounts = new ArrayList<>();
        this.taxRate = DEFAULT_TAX_RATE;
    }

    // ========== Discount Management ==========

    public void registerCoupon(Discount discount) {
        if (discount.getCode() != null) {
            couponDiscounts.put(discount.getCode().toUpperCase(), discount);
            System.out.printf("🎟️ Registered coupon: %s - %s%n", 
                    discount.getCode(), discount.getDescription());
        }
    }

    public void addAutomaticDiscount(Discount discount) {
        automaticDiscounts.add(discount);
        System.out.printf("🏷️ Added automatic discount: %s%n", discount.getDescription());
    }

    public Optional<Discount> getCoupon(String code) {
        return Optional.ofNullable(couponDiscounts.get(code.toUpperCase()));
    }

    // ========== Price Calculation ==========

    public PricingResult calculateTotal(Cart cart) {
        Money subtotal = cart.getSubtotal();
        Money totalDiscount = Money.ZERO;
        List<String> appliedDiscounts = new ArrayList<>();
        
        // Apply automatic discounts
        for (Discount discount : automaticDiscounts) {
            if (discount.isApplicable(cart)) {
                Money discountAmount = discount.calculate(cart);
                totalDiscount = totalDiscount.add(discountAmount);
                appliedDiscounts.add(discount.getDescription() + ": -" + discountAmount);
            }
        }
        
        // Apply coupon discounts
        for (String couponCode : cart.getAppliedCouponCodes()) {
            Discount discount = couponDiscounts.get(couponCode.toUpperCase());
            if (discount != null && discount.isApplicable(cart)) {
                Money discountAmount = discount.calculate(cart);
                totalDiscount = totalDiscount.add(discountAmount);
                appliedDiscounts.add(couponCode + ": -" + discountAmount);
            }
        }
        
        // Calculate after discount
        Money afterDiscount = subtotal.subtract(totalDiscount);
        if (afterDiscount.isLessThan(Money.ZERO)) {
            afterDiscount = Money.ZERO;
            totalDiscount = subtotal;
        }
        
        // Calculate tax
        Money tax = afterDiscount.multiply(taxRate);
        
        // Calculate total
        Money total = afterDiscount.add(tax);
        
        return new PricingResult(subtotal, totalDiscount, tax, total, appliedDiscounts);
    }

    public Order checkout(Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout empty cart");
        }
        
        PricingResult pricing = calculateTotal(cart);
        
        Order order = Order.builder(cart.getUserId())
                .items(cart.getItems())
                .subtotal(pricing.subtotal())
                .discount(pricing.discount())
                .tax(pricing.tax())
                .total(pricing.total())
                .appliedCoupons(cart.getAppliedCouponCodes())
                .build();
        
        order.confirm();
        return order;
    }

    // ========== Configuration ==========

    public void setTaxRate(double rate) {
        this.taxRate = rate;
    }

    // ========== Result Record ==========

    public record PricingResult(
            Money subtotal,
            Money discount,
            Money tax,
            Money total,
            List<String> appliedDiscounts
    ) {
        public void print() {
            System.out.println("\n📊 Pricing Breakdown:");
            System.out.printf("   Subtotal:  %s%n", subtotal);
            
            if (!appliedDiscounts.isEmpty()) {
                System.out.println("   Discounts:");
                for (String desc : appliedDiscounts) {
                    System.out.printf("     • %s%n", desc);
                }
                System.out.printf("   After Discount: %s%n", subtotal.subtract(discount));
            }
            
            System.out.printf("   Tax (%.0f%%): +%s%n", 8.0, tax);
            System.out.printf("   ────────────────────%n");
            System.out.printf("   TOTAL:     %s%n", total);
        }
    }
}

