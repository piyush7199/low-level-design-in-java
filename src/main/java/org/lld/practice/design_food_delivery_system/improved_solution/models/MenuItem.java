package org.lld.practice.design_food_delivery_system.improved_solution.models;

public class MenuItem {
    private final String itemId;
    private final String name;
    private final String description;
    private final double price;
    private boolean available;

    public MenuItem(String itemId, String name, String description, double price) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = true;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return name + " - $" + price;
    }
}

