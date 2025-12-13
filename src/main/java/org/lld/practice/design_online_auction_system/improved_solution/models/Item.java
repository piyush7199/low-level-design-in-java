package org.lld.practice.design_online_auction_system.improved_solution.models;

public class Item {
    private final String itemId;
    private final String name;
    private final String description;
    private final String category;

    public Item(String itemId, String name, String description, String category) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("Item[id=%s, name=%s, category=%s]", itemId, name, category);
    }
}

