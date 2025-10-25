package org.lld.practice.design_ride_sharing_system.improved_solution.models;

import java.util.ArrayList;
import java.util.List;

public class Rider extends User {
    private final List<String> rideHistory;

    public Rider(String userId, String name, String phone) {
        super(userId, name, phone);
        this.rideHistory = new ArrayList<>();
    }

    public void addRideToHistory(String rideId) {
        rideHistory.add(rideId);
    }

    public List<String> getRideHistory() {
        return new ArrayList<>(rideHistory);
    }

    @Override
    public String toString() {
        return "Rider{" +
                "id='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + String.format("%.2f", rating) +
                '}';
    }
}

