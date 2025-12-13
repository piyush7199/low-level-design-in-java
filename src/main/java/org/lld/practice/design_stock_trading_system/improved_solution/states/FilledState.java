package org.lld.practice.design_stock_trading_system.improved_solution.states;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;

public class FilledState implements OrderState {
    @Override
    public void fill(Order order, int quantity) {
        System.out.println("Order " + order.getOrderId() + " is already FILLED");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Cannot cancel order " + order.getOrderId() + " - already FILLED");
    }

    @Override
    public void printStatus(Order order) {
        System.out.println("Order " + order.getOrderId() + " is FILLED");
    }
}

