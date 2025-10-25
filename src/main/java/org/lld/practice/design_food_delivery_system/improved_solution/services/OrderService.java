package org.lld.practice.design_food_delivery_system.improved_solution.services;

import org.lld.practice.design_food_delivery_system.improved_solution.models.*;
import org.lld.practice.design_food_delivery_system.improved_solution.strategies.CreditCardPayment;

import java.util.*;

public class OrderService {
    private final Map<String, Order> orders;
    private final RestaurantService restaurantService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private int orderCounter = 1;

    public OrderService(RestaurantService restaurantService, DeliveryService deliveryService,
                        PaymentService paymentService, NotificationService notificationService) {
        this.orders = new HashMap<>();
        this.restaurantService = restaurantService;
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public Order placeOrder(String customerId, String restaurantId, List<MenuItem> items) {
        // This is a simplified version - in reality, we'd fetch customer from UserService
        Customer customer = new Customer(customerId, "Customer-" + customerId, 
                                        "customer@example.com", "1234567890", 
                                        new Location(37.7749, -122.4194, "Customer Address"));
        
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        if (restaurant == null) {
            System.out.println("[OrderService] Restaurant not found: " + restaurantId);
            return null;
        }

        if (!restaurant.isOpen()) {
            System.out.println("[OrderService] Restaurant is closed: " + restaurant.getName());
            return null;
        }

        double totalAmount = items.stream().mapToDouble(MenuItem::getPrice).sum();
        String orderId = "ORD" + String.format("%05d", orderCounter++);
        
        Order order = new Order(orderId, customer, restaurant, items, totalAmount);
        
        // Process payment
        boolean paymentSuccess = paymentService.processPayment(order, new CreditCardPayment());
        if (!paymentSuccess) {
            System.out.println("[OrderService] Payment failed for order: " + orderId);
            return null;
        }

        orders.put(orderId, order);
        customer.addOrderToHistory(orderId);
        
        System.out.println("[OrderService] Order placed successfully: " + orderId);
        System.out.println("Restaurant: " + restaurant.getName());
        System.out.println("Total: $" + totalAmount);
        
        // Assign delivery partner
        DeliveryPartner partner = deliveryService.assignDeliveryPartner(restaurant.getLocation(), customer.getAddress());
        if (partner != null) {
            order.setDeliveryPartner(partner);
            notificationService.notifyDeliveryAssignment(order, partner);
        }
        
        // Notify stakeholders
        notificationService.notifyOrderPlaced(order);
        
        return order;
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("[OrderService] Order not found: " + orderId);
            return;
        }

        order.updateStatus(newStatus);
        notificationService.notifyStatusChange(order);
        
        // If delivered, mark delivery partner as available
        if (newStatus == OrderStatus.DELIVERED && order.getDeliveryPartner() != null) {
            order.getDeliveryPartner().setAvailable(true);
            order.getDeliveryPartner().incrementDeliveries();
        }
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
}

