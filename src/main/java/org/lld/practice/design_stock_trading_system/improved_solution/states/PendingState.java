package org.lld.practice.design_stock_trading_system.improved_solution.states;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderStatus;

public class PendingState implements OrderState {
    @Override
    public void fill(Order order, int quantity) {
        order.fill(quantity);
        if (order.getFilledQuantity() < order.getQuantity()) {
            // Transition to PartiallyFilledState handled by Order.fill()
        }
    }

    @Override
    public void cancel(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Order " + order.getOrderId() + " cancelled");
    }

    @Override
    public void printStatus(Order order) {
        System.out.println("Order " + order.getOrderId() + " is PENDING");
    }
}

