package org.lld.practice.design_stock_trading_system.improved_solution.strategies;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderSide;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Price-Time Priority Strategy:
 * - For buy orders: Match with lowest price first, then earliest time
 * - For sell orders: Match with highest price first, then earliest time
 */
public class PriceTimePriorityStrategy implements MatchingStrategy {
    @Override
    public List<MatchResult> findMatches(Order order, List<Order> oppositeOrders) {
        List<MatchResult> matches = new ArrayList<>();
        int remainingQuantity = order.getRemainingQuantity();
        
        if (remainingQuantity <= 0) {
            return matches;
        }
        
        // Filter and sort opposite orders based on price-time priority
        List<Order> eligibleOrders = oppositeOrders.stream()
                .filter(o -> o.getStockSymbol().equals(order.getStockSymbol()))
                .filter(o -> o.getRemainingQuantity() > 0)
                .filter(o -> canMatch(order, o))
                .sorted(getComparator(order.getType()))
                .collect(Collectors.toList());
        
        // Match orders until order is fully filled or no more matches
        for (Order oppositeOrder : eligibleOrders) {
            if (remainingQuantity <= 0) {
                break;
            }
            
            int matchQuantity = Math.min(remainingQuantity, oppositeOrder.getRemainingQuantity());
            matches.add(new MatchResult(oppositeOrder, matchQuantity));
            remainingQuantity -= matchQuantity;
        }
        
        return matches;
    }
    
    private boolean canMatch(Order order, Order oppositeOrder) {
        if (order.getSide() == OrderSide.MARKET) {
            return true; // Market orders match at any price
        }
        
        if (oppositeOrder.getSide() == OrderSide.MARKET) {
            return true; // Market orders match at any price
        }
        
        // Limit orders: buy price must be >= sell price
        if (order.getType() == OrderType.BUY) {
            return order.getPrice() >= oppositeOrder.getPrice();
        } else {
            return oppositeOrder.getPrice() >= order.getPrice();
        }
    }
    
    private Comparator<Order> getComparator(OrderType orderType) {
        if (orderType == OrderType.BUY) {
            // For buy orders: prefer lowest price (best for buyer), then earliest time
            return Comparator.comparing(Order::getPrice)
                    .thenComparing(Order::getCreatedAt);
        } else {
            // For sell orders: prefer highest price (best for seller), then earliest time
            return Comparator.comparing(Order::getPrice, Comparator.reverseOrder())
                    .thenComparing(Order::getCreatedAt);
        }
    }
}

