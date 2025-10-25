package org.lld.practice.design_food_delivery_system.improved_solution.services;

import org.lld.practice.design_food_delivery_system.improved_solution.models.DeliveryPartner;
import org.lld.practice.design_food_delivery_system.improved_solution.models.Order;

public class NotificationService {
    
    public void notifyOrderPlaced(Order order) {
        System.out.println("[Notification] To Customer " + order.getCustomer().getName() + 
                         ": Your order #" + order.getOrderId() + " has been placed!");
        System.out.println("[Notification] To Restaurant " + order.getRestaurant().getName() + 
                         ": New order received #" + order.getOrderId());
    }

    public void notifyDeliveryAssignment(Order order, DeliveryPartner partner) {
        System.out.println("[Notification] To Partner " + partner.getName() + 
                         ": New delivery assigned #" + order.getOrderId());
    }

    public void notifyStatusChange(Order order) {
        System.out.println("[Notification] Order #" + order.getOrderId() + 
                         " status updated to: " + order.getStatus());
    }
}

