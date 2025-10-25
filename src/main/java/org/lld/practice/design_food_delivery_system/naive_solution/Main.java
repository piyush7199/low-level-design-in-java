package org.lld.practice.design_food_delivery_system.naive_solution;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        FoodDeliveryApp app = new FoodDeliveryApp();
        
        // Register users
        app.registerCustomer(new Customer("C1", "Alice"));
        app.registerRestaurant(new Restaurant("R1", "Pizza Palace"));
        app.registerDeliveryPartner(new DeliveryPartner("D1", "John"));
        
        // Place order
        Order order = app.placeOrder("C1", "R1", Arrays.asList("Margherita Pizza", "Coke"));
        
        // Update status
        if (order != null) {
            app.updateOrderStatus(order.getId(), "PREPARING");
            app.updateOrderStatus(order.getId(), "DELIVERED");
        }
    }
}

