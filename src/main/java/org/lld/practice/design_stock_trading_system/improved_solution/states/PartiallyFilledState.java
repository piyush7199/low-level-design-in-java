package org.lld.practice.design_stock_trading_system.improved_solution.states;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderStatus;

public class PartiallyFilledState implements OrderState {
    @Override
    public void fill(Order order, int quantity) {
        order.fill(quantity);
        // Order.fill() will update status to FILLED if complete
    }

    @Override
    public void cancel(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Order " + order.getOrderId() + " cancelled (was partially filled)");
    }

    @Override
    public void printStatus(Order order) {
        System.out.println("Order " + order.getOrderId() + " is PARTIALLY FILLED: " +
                order.getFilledQuantity() + "/" + order.getQuantity());
    }
}

