package org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.User;

import java.util.*;

public class InMemoryUserRepository implements Repository<User, String> {
    private final Map<String, User> users;

    public InMemoryUserRepository() {
        this.users = new HashMap<>();
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
