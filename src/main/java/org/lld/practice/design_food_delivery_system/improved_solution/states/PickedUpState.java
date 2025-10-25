package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public class PickedUpState implements OrderState {
    private final Order order;

    public PickedUpState(Order order) {
        this.order = order;
    }

    @Override
    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.OUT_FOR_DELIVERY) {
            order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            order.setState(new OutForDeliveryState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: PICKED_UP → OUT_FOR_DELIVERY");
        } else {
            System.out.println("[Order " + order.getOrderId() + "] Invalid status transition from PICKED_UP to " + newStatus);
        }
    }

    @Override
    public String getStateName() {
        return "PICKED_UP";
    }
}

