package org.lld.practice.design_stock_trading_system.naive_solution;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Stock Trading System ===\n");

        SimpleTradingSystem system = new SimpleTradingSystem();

        // Place some orders
        system.placeOrder(new Order("O1", "BUY", "AAPL", 150.0, 100));
        system.placeOrder(new Order("O2", "SELL", "AAPL", 149.0, 50));
        system.placeOrder(new Order("O3", "BUY", "AAPL", 151.0, 200));
        system.placeOrder(new Order("O4", "SELL", "AAPL", 150.5, 75));

        System.out.println("\nOrder Book:");
        system.printOrderBook();

        System.out.println("\nLimitations:");
        System.out.println("- O(n²) matching complexity");
        System.out.println("- Not thread-safe");
        System.out.println("- No order states");
        System.out.println("- No order types (market vs limit)");
    }
}

