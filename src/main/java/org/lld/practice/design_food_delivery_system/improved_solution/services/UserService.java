package org.lld.practice.design_food_delivery_system.improved_solution.services;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Customer;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, Customer> customers;

    public UserService() {
        this.customers = new HashMap<>();
    }

    public void registerCustomer(Customer customer) {
        customers.put(customer.getUserId(), customer);
        System.out.println("[UserService] Customer registered: " + customer.getName());
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }
}

