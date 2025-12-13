package org.lld.practice.design_stock_trading_system.improved_solution.models;

import java.time.LocalDateTime;

public class Trade {
    private final String tradeId;
    private final String stockSymbol;
    private final String buyOrderId;
    private final String sellOrderId;
    private final double price;
    private final int quantity;
    private final LocalDateTime executedAt;

    public Trade(String tradeId, String stockSymbol, String buyOrderId, 
                 String sellOrderId, double price, int quantity) {
        this.tradeId = tradeId;
        this.stockSymbol = stockSymbol;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
        this.executedAt = LocalDateTime.now();
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    @Override
    public String toString() {
        return String.format("Trade[id=%s, %s: %d @ $%.2f, buy=%s, sell=%s]",
                tradeId, stockSymbol, quantity, price, buyOrderId, sellOrderId);
    }
}

