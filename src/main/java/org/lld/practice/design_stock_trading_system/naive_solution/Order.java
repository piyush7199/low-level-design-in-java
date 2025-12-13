package org.lld.practice.design_stock_trading_system.naive_solution;

public class Order {
    private String orderId;
    private String type; // "BUY" or "SELL"
    private String stockSymbol;
    private double price;
    private int quantity;

    public Order(String orderId, String type, String stockSymbol, double price, int quantity) {
        this.orderId = orderId;
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getType() {
        return type;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Order[%s: %s %d @ $%.2f]", orderId, type, quantity, price);
    }
}

