package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public class PlacedState implements OrderState {
    private final Order order;

    public PlacedState(Order order) {
        this.order = order;
    }

    @Override
    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.CONFIRMED);
            order.setState(new ConfirmedState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: PLACED → CONFIRMED");
        } else if (newStatus == OrderStatus.CANCELLED) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setState(new CancelledState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: PLACED → CANCELLED");
        } else {
            System.out.println("[Order " + order.getOrderId() + "] Invalid status transition from PLACED to " + newStatus);
        }
    }

    @Override
    public String getStateName() {
        return "PLACED";
    }
}

