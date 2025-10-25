package org.lld.practice.design_food_delivery_system.improved_solution.models;

public abstract class User {
    protected final String userId;
    protected final String name;
    protected final String email;
    protected final String phone;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}

