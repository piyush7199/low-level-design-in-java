package org.lld.practice.design_queue_management_system.improved_solution.models;

import java.util.Objects;

/**
 * Represents a customer in the queue management system.
 * Immutable class following best practices.
 */
public class Customer {
    private final String customerId;
    private final String name;
    private final String phoneNumber;
    private final String email;

    public Customer(String customerId, String name, String phoneNumber) {
        this(customerId, name, phoneNumber, null);
    }

    public Customer(String customerId, String name, String phoneNumber, String email) {
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s'}", customerId, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}

