package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public class PreparingState implements OrderState {
    private final Order order;

    public PreparingState(Order order) {
        this.order = order;
    }

    @Override
    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.READY_FOR_PICKUP) {
            order.setStatus(OrderStatus.READY_FOR_PICKUP);
            order.setState(new ReadyForPickupState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: PREPARING → READY_FOR_PICKUP");
        } else {
            System.out.println("[Order " + order.getOrderId() + "] Invalid status transition from PREPARING to " + newStatus);
        }
    }

    @Override
    public String getStateName() {
        return "PREPARING";
    }
}

