package org.lld.practice.design_food_delivery_system.improved_solution.models;

import org.lld.practice.design_food_delivery_system.improved_solution.states.OrderState;
import org.lld.practice.design_food_delivery_system.improved_solution.states.PlacedState;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final String orderId;
    private final Customer customer;
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private final double totalAmount;
    private final LocalDateTime orderTime;
    private OrderState currentState;
    private DeliveryPartner deliveryPartner;
    private OrderStatus status;

    public Order(String orderId, Customer customer, Restaurant restaurant, List<MenuItem> items, double totalAmount) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderTime = LocalDateTime.now();
        this.currentState = new PlacedState(this);
        this.status = OrderStatus.PLACED;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    public void setState(OrderState state) {
        this.currentState = state;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public void updateStatus(OrderStatus newStatus) {
        currentState.updateStatus(newStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customer=" + customer.getName() +
                ", restaurant=" + restaurant.getName() +
                ", items=" + items.size() +
                ", total=$" + totalAmount +
                ", status=" + status +
                '}';
    }
}

