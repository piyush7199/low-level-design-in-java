package org.lld.practice.design_stock_trading_system.improved_solution.factories;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderSide;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderType;

import java.util.UUID;

/**
 * Factory for creating different types of orders.
 */
public class OrderFactory {
    public static Order createMarketOrder(String userId, String stockSymbol, OrderType type, int quantity) {
        String orderId = "O" + UUID.randomUUID().toString().substring(0, 8);
        return new Order(orderId, userId, stockSymbol, type, OrderSide.MARKET, 0, quantity);
    }

    public static Order createLimitOrder(String userId, String stockSymbol, OrderType type, 
                                         double price, int quantity) {
        String orderId = "O" + UUID.randomUUID().toString().substring(0, 8);
        return new Order(orderId, userId, stockSymbol, type, OrderSide.LIMIT, price, quantity);
    }
}

