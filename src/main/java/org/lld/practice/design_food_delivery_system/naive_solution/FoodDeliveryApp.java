package org.lld.practice.design_food_delivery_system.naive_solution;

import java.util.*;

public class FoodDeliveryApp {
    private Map<String, Customer> customers = new HashMap<>();
    private Map<String, Restaurant> restaurants = new HashMap<>();
    private Map<String, DeliveryPartner> deliveryPartners = new HashMap<>();
    private List<Order> orders = new ArrayList<>();
    
    public void registerCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        System.out.println("Customer registered: " + customer.getName());
    }
    
    public void registerRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
        System.out.println("Restaurant registered: " + restaurant.getName());
    }
    
    public void registerDeliveryPartner(DeliveryPartner partner) {
        deliveryPartners.put(partner.getId(), partner);
        System.out.println("Delivery partner registered: " + partner.getName());
    }
    
    public Order placeOrder(String customerId, String restaurantId, List<String> items) {
        Customer customer = customers.get(customerId);
        Restaurant restaurant = restaurants.get(restaurantId);
        
        if (customer == null || restaurant == null) {
            System.out.println("Invalid customer or restaurant");
            return null;
        }
        
        Order order = new Order("ORD" + orders.size(), customer, restaurant, items);
        orders.add(order);
        
        // Process payment
        System.out.println("Payment processed for order: " + order.getId());
        
        // Assign delivery partner
        DeliveryPartner partner = findAvailablePartner();
        if (partner != null) {
            order.setDeliveryPartner(partner);
            partner.setAvailable(false);
            System.out.println("Assigned delivery partner: " + partner.getName());
        }
        
        System.out.println("Order placed successfully: " + order.getId());
        return order;
    }
    
    public void updateOrderStatus(String orderId, String status) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                order.setStatus(status);
                System.out.println("Order " + orderId + " status updated to: " + status);
                return;
            }
        }
    }
    
    private DeliveryPartner findAvailablePartner() {
        for (DeliveryPartner partner : deliveryPartners.values()) {
            if (partner.isAvailable()) {
                return partner;
            }
        }
        return null;
    }
}

class Customer {
    private String id;
    private String name;
    
    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
}

class Restaurant {
    private String id;
    private String name;
    
    public Restaurant(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
}

class DeliveryPartner {
    private String id;
    private String name;
    private boolean available;
    
    public DeliveryPartner(String id, String name) {
        this.id = id;
        this.name = name;
        this.available = true;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

class Order {
    private String id;
    private Customer customer;
    private Restaurant restaurant;
    private List<String> items;
    private DeliveryPartner deliveryPartner;
    private String status;
    
    public Order(String id, Customer customer, Restaurant restaurant, List<String> items) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.status = "PLACED";
    }
    
    public String getId() { return id; }
    public void setDeliveryPartner(DeliveryPartner partner) { this.deliveryPartner = partner; }
    public void setStatus(String status) { this.status = status; }
}

