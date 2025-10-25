package org.lld.practice.design_ride_sharing_system.improved_solution.models;

public abstract class User {
    protected final String userId;
    protected final String name;
    protected final String phone;
    protected double rating;
    protected int totalRatings;

    public User(String userId, String name, String phone) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.rating = 5.0;
        this.totalRatings = 0;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public double getRating() {
        return rating;
    }

    public void updateRating(double newRating) {
        this.rating = ((this.rating * totalRatings) + newRating) / (totalRatings + 1);
        this.totalRatings++;
    }
}

