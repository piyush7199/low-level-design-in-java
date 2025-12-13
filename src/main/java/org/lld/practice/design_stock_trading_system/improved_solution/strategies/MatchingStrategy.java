package org.lld.practice.design_stock_trading_system.improved_solution.strategies;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;

import java.util.List;

/**
 * Strategy interface for order matching algorithms.
 */
public interface MatchingStrategy {
    /**
     * Find matching orders for the given order.
     * @param order The order to match
     * @param oppositeOrders List of opposite side orders (buy orders if order is sell, vice versa)
     * @return List of matching orders with quantities to fill
     */
    List<MatchResult> findMatches(Order order, List<Order> oppositeOrders);
    
    class MatchResult {
        private final Order order;
        private final int quantity;
        
        public MatchResult(Order order, int quantity) {
            this.order = order;
            this.quantity = quantity;
        }
        
        public Order getOrder() {
            return order;
        }
        
        public int getQuantity() {
            return quantity;
        }
    }
}

