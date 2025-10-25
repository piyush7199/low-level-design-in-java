package org.lld.practice.design_food_delivery_system.improved_solution.models;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private final String restaurantId;
    private final String name;
    private final Location location;
    private final List<MenuItem> menu;
    private boolean isOpen;
    private double rating;

    public Restaurant(String restaurantId, String name, Location location) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.location = location;
        this.menu = new ArrayList<>();
        this.isOpen = true;
        this.rating = 4.0;
    }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    public void removeMenuItem(String itemId) {
        menu.removeIf(item -> item.getItemId().equals(itemId));
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + restaurantId + '\'' +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", isOpen=" + isOpen +
                '}';
    }
}

