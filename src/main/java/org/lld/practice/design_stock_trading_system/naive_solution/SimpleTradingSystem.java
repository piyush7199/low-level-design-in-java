package org.lld.practice.design_stock_trading_system.naive_solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive implementation showing common pitfalls:
 * - O(n²) matching complexity
 * - No order types
 * - Not thread-safe
 * - No order states
 * - No partial fills
 */
public class SimpleTradingSystem {
    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> sellOrders = new ArrayList<>();

    public void placeOrder(Order order) {
        if (order.getType().equals("BUY")) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
        matchOrders();
    }

    private void matchOrders() {
        // O(n²) complexity - very inefficient!
        for (Order buy : buyOrders) {
            for (Order sell : sellOrders) {
                if (buy.getPrice() >= sell.getPrice() && 
                    buy.getQuantity() > 0 && 
                    sell.getQuantity() > 0) {
                    executeTrade(buy, sell);
                }
            }
        }
    }

    private void executeTrade(Order buy, Order sell) {
        int quantity = Math.min(buy.getQuantity(), sell.getQuantity());
        System.out.println("Executed trade: " + quantity + " shares at $" + sell.getPrice());
        
        buy.setQuantity(buy.getQuantity() - quantity);
        sell.setQuantity(sell.getQuantity() - quantity);
        
        // Remove filled orders
        if (buy.getQuantity() == 0) {
            buyOrders.remove(buy);
        }
        if (sell.getQuantity() == 0) {
            sellOrders.remove(sell);
        }
    }

    public void printOrderBook() {
        System.out.println("Buy Orders: " + buyOrders);
        System.out.println("Sell Orders: " + sellOrders);
    }
}

