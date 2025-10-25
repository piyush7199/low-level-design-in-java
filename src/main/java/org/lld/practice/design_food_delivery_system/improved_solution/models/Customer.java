package org.lld.practice.design_food_delivery_system.improved_solution.models;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private final Location address;
    private final List<String> orderHistory;

    public Customer(String userId, String name, String email, String phone, Location address) {
        super(userId, name, email, phone);
        this.address = address;
        this.orderHistory = new ArrayList<>();
    }

    public Location getAddress() {
        return address;
    }

    public void addOrderToHistory(String orderId) {
        orderHistory.add(orderId);
    }

    public List<String> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }
}

