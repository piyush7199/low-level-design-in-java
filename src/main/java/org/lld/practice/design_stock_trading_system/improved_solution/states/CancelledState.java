package org.lld.practice.design_stock_trading_system.improved_solution.states;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;

public class CancelledState implements OrderState {
    @Override
    public void fill(Order order, int quantity) {
        System.out.println("Cannot fill order " + order.getOrderId() + " - already CANCELLED");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Order " + order.getOrderId() + " is already CANCELLED");
    }

    @Override
    public void printStatus(Order order) {
        System.out.println("Order " + order.getOrderId() + " is CANCELLED");
    }
}

