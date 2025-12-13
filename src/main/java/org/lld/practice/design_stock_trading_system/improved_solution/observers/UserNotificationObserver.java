package org.lld.practice.design_stock_trading_system.improved_solution.observers;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.Trade;

/**
 * Notifies users about trade execution.
 */
public class UserNotificationObserver implements TradeObserver {
    @Override
    public void onTradeExecuted(Trade trade, Order buyOrder, Order sellOrder) {
        System.out.println("Notification: Trade executed for user " + buyOrder.getUserId() + 
                " - " + trade.getQuantity() + " shares @ $" + trade.getPrice());
        System.out.println("Notification: Trade executed for user " + sellOrder.getUserId() + 
                " - " + trade.getQuantity() + " shares @ $" + trade.getPrice());
    }
}

