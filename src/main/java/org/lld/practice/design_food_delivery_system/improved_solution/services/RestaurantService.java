package org.lld.practice.design_food_delivery_system.improved_solution.services;

import org.lld.practice.design_food_delivery_system.improved_solution.models.Restaurant;

import java.util.*;
import java.util.stream.Collectors;

public class RestaurantService {
    private final Map<String, Restaurant> restaurants;

    public RestaurantService() {
        this.restaurants = new HashMap<>();
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getRestaurantId(), restaurant);
        System.out.println("[RestaurantService] Restaurant added: " + restaurant.getName());
    }

    public Restaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }

    public List<Restaurant> searchRestaurants(String query) {
        return restaurants.values().stream()
                .filter(r -> r.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants.values());
    }
}

