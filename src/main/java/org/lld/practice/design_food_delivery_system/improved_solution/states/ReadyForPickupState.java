package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public class ReadyForPickupState implements OrderState {
    private final Order order;

    public ReadyForPickupState(Order order) {
        this.order = order;
    }

    @Override
    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.PICKED_UP) {
            order.setStatus(OrderStatus.PICKED_UP);
            order.setState(new PickedUpState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: READY_FOR_PICKUP → PICKED_UP");
        } else {
            System.out.println("[Order " + order.getOrderId() + "] Invalid status transition from READY_FOR_PICKUP to " + newStatus);
        }
    }

    @Override
    public String getStateName() {
        return "READY_FOR_PICKUP";
    }
}

