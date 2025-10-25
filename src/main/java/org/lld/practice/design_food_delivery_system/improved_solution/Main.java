package org.lld.practice.design_food_delivery_system.improved_solution;

import org.lld.practice.design_food_delivery_system.improved_solution.models.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        FoodDeliverySystem system = new FoodDeliverySystem();

        // Setup: Register users and restaurants
        System.out.println("=== Setting Up System ===\n");
        
        Customer customer = new Customer("C001", "Alice Johnson", "alice@example.com", 
                                        "1234567890", new Location(37.7749, -122.4194, "123 Main St"));
        system.registerCustomer(customer);

        Restaurant restaurant = new Restaurant("R001", "Pizza Palace", 
                                              new Location(37.7849, -122.4294, "456 Restaurant Ave"));
        restaurant.addMenuItem(new MenuItem("I001", "Margherita Pizza", "Classic pizza", 12.99));
        restaurant.addMenuItem(new MenuItem("I002", "Pepperoni Pizza", "Spicy pepperoni", 14.99));
        restaurant.addMenuItem(new MenuItem("I003", "Coke", "Soft drink", 2.99));
        system.registerRestaurant(restaurant);

        DeliveryPartner partner1 = new DeliveryPartner("D001", "John Delivery", 
                                                       "john@example.com", "9876543210",
                                                       new Location(37.7799, -122.4244, "Nearby"));
        DeliveryPartner partner2 = new DeliveryPartner("D002", "Sarah Express", 
                                                       "sarah@example.com", "5555555555",
                                                       new Location(37.7899, -122.4344, "Far away"));
        system.registerDeliveryPartner(partner1);
        system.registerDeliveryPartner(partner2);

        // Scenario 1: Place an order
        System.out.println("\n=== Scenario 1: Placing Order ===\n");
        Order order = system.placeOrder("C001", "R001", Arrays.asList(
            restaurant.getMenu().get(0),
            restaurant.getMenu().get(2)
        ));

        // Scenario 2: Track order lifecycle
        System.out.println("\n=== Scenario 2: Order Lifecycle ===\n");
        if (order != null) {
            system.updateOrderStatus(order.getOrderId(), OrderStatus.CONFIRMED);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.updateOrderStatus(order.getOrderId(), OrderStatus.PREPARING);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.updateOrderStatus(order.getOrderId(), OrderStatus.READY_FOR_PICKUP);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.updateOrderStatus(order.getOrderId(), OrderStatus.PICKED_UP);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.updateOrderStatus(order.getOrderId(), OrderStatus.OUT_FOR_DELIVERY);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            system.updateOrderStatus(order.getOrderId(), OrderStatus.DELIVERED);
        }

        // Scenario 3: Search restaurants
        System.out.println("\n=== Scenario 3: Restaurant Search ===\n");
        System.out.println("Searching for 'Pizza': " + system.searchRestaurants("Pizza"));

        System.out.println("\n=== Order Complete ===");
    }
}

