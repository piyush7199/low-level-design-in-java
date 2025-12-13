package org.lld.practice.design_stock_trading_system.improved_solution.models;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private final String userId;
    private double cashBalance;
    private final Map<String, Integer> holdings; // stockSymbol -> quantity

    public Portfolio(String userId, double initialBalance) {
        this.userId = userId;
        this.cashBalance = initialBalance;
        this.holdings = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void addCash(double amount) {
        this.cashBalance += amount;
    }

    public void deductCash(double amount) {
        if (this.cashBalance < amount) {
            throw new IllegalArgumentException("Insufficient cash balance");
        }
        this.cashBalance -= amount;
    }

    public int getHolding(String stockSymbol) {
        return holdings.getOrDefault(stockSymbol, 0);
    }

    public void addHolding(String stockSymbol, int quantity) {
        holdings.put(stockSymbol, holdings.getOrDefault(stockSymbol, 0) + quantity);
    }

    public void deductHolding(String stockSymbol, int quantity) {
        int current = holdings.getOrDefault(stockSymbol, 0);
        if (current < quantity) {
            throw new IllegalArgumentException("Insufficient holdings for " + stockSymbol);
        }
        holdings.put(stockSymbol, current - quantity);
        if (holdings.get(stockSymbol) == 0) {
            holdings.remove(stockSymbol);
        }
    }

    public Map<String, Integer> getAllHoldings() {
        return new HashMap<>(holdings);
    }

    @Override
    public String toString() {
        return String.format("Portfolio[userId=%s, cash=$%.2f, holdings=%s]",
                userId, cashBalance, holdings);
    }
}

