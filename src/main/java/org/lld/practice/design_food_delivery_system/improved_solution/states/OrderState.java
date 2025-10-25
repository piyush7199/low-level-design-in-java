package org.lld.practice.design_food_delivery_system.improved_solution.states;

import org.lld.practice.design_food_delivery_system.improved_solution.models.OrderStatus;

public interface OrderState {
    void updateStatus(OrderStatus newStatus);
    String getStateName();
}

