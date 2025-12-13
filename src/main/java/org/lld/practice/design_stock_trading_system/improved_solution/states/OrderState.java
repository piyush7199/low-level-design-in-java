package org.lld.practice.design_stock_trading_system.improved_solution.states;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;

/**
 * State interface for Order lifecycle management.
 */
public interface OrderState {
    void fill(Order order, int quantity);
    void cancel(Order order);
    void printStatus(Order order);
}

