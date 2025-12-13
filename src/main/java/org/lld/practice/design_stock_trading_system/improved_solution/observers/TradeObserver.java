package org.lld.practice.design_stock_trading_system.improved_solution.observers;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.Trade;

/**
 * Observer interface for trade execution notifications.
 */
public interface TradeObserver {
    void onTradeExecuted(Trade trade, Order buyOrder, Order sellOrder);
}

