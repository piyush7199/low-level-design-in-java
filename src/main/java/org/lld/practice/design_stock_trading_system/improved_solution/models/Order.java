package org.lld.practice.design_stock_trading_system.improved_solution.models;

import java.time.LocalDateTime;

public class Order {
    private final String orderId;
    private final String userId;
    private final String stockSymbol;
    private final OrderType type;
    private final OrderSide side;
    private final double price;
    private final int quantity;
    private int filledQuantity;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(String orderId, String userId, String stockSymbol, OrderType type, 
                 OrderSide side, double price, int quantity) {
        this.orderId = orderId;
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.filledQuantity = 0;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public OrderType getType() {
        return type;
    }

    public OrderSide getSide() {
        return side;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getFilledQuantity() {
        return filledQuantity;
    }

    public int getRemainingQuantity() {
        return quantity - filledQuantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void fill(int quantity) {
        this.filledQuantity += quantity;
        if (this.filledQuantity >= this.quantity) {
            this.status = OrderStatus.FILLED;
        } else {
            this.status = OrderStatus.PARTIALLY_FILLED;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return String.format("Order[id=%s, %s %s %d @ $%.2f, filled=%d/%d, status=%s]",
                orderId, type, side, quantity, price, filledQuantity, quantity, status);
    }
}

