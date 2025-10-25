package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;
import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public class OutForDeliveryState implements OrderState {
    private final Order order;

    public OutForDeliveryState(Order order) {
        this.order = order;
    }

    @Override
    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.DELIVERED) {
            order.setStatus(OrderStatus.DELIVERED);
            order.setState(new DeliveredState(order));
            System.out.println("[Order " + order.getOrderId() + "] Status: OUT_FOR_DELIVERY → DELIVERED");
        } else {
            System.out.println("[Order " + order.getOrderId() + "] Invalid status transition from OUT_FOR_DELIVERY to " + newStatus);
        }
    }

    @Override
    public String getStateName() {
        return "OUT_FOR_DELIVERY";
    }
}

