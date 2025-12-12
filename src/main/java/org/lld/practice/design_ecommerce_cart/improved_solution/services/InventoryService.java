package org.lld.practice.design_ecommerce_cart.improved_solution.services;

import org.lld.practice.design_ecommerce_cart.improved_solution.models.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing product inventory.
 */
public class InventoryService {
    
    private final Map<String, Product> products;

    public InventoryService() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    public Optional<Product> getProduct(String productId) {
        return Optional.ofNullable(products.get(productId));
    }

    public Product getProductOrThrow(String productId) {
        return getProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }

    public boolean hasStock(String productId, int quantity) {
        return getProduct(productId)
                .map(p -> p.hasStock(quantity))
                .orElse(false);
    }

    public void reserveStock(String productId, int quantity) {
        Product product = getProductOrThrow(productId);
        product.reduceStock(quantity);
    }

    public void releaseStock(String productId, int quantity) {
        Product product = getProductOrThrow(productId);
        product.addStock(quantity);
    }

    public int getStockLevel(String productId) {
        return getProduct(productId)
                .map(Product::getStockQuantity)
                .orElse(0);
    }
}

