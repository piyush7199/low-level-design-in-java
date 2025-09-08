package org.lld.practice.design_online_hotel_booking_system.improved_solution.models;

import java.util.UUID;

public class User {
    private final String userId;
    private final String name;

    public User(String name) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
