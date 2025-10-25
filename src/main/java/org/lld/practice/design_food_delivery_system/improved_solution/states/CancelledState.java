package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public class CancelledState implements OrderState {
    private final Order order;

    public CancelledState(Order order) {
        this.order = order;
    }

    @Override
    public void updateStatus(OrderStatus newStatus) {
        System.out.println("[Order " + order.getOrderId() + "] Order is cancelled. No further status changes allowed.");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}

