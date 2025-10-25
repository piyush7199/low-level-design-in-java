package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public class ConfirmedState implements OrderState {
    private final Order order;

    public ConfirmedState(Order order) {
        this.order = order;
    }

    @Override
    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.PREPARING) {
            order.setStatus(OrderStatus.PREPARING);
            order.setState(new PreparingState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: CONFIRMED → PREPARING");
        } else if (newStatus == OrderStatus.CANCELLED) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setState(new CancelledState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: CONFIRMED → CANCELLED");
        } else {
            System.out.println("[Order " + order.getOrderId() + "] Invalid status transition from CONFIRMED to " + newStatus);
        }
    }

    @Override
    public String getStateName() {
        return "CONFIRMED";
    }
}

