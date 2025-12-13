package org.lld.practice.design_stock_trading_system.improved_solution;

import org.lld.practice.design_stock_trading_system.improved_solution.factories.OrderFactory;
import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderType;
import org.lld.practice.design_stock_trading_system.improved_solution.observers.UserNotificationObserver;
import org.lld.practice.design_stock_trading_system.improved_solution.services.PortfolioService;
import org.lld.practice.design_stock_trading_system.improved_solution.services.TradingEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Stock Trading System Demo ===\n");

        TradingEngine engine = TradingEngine.getInstance();
        PortfolioService portfolioService = engine.getPortfolioService();
        
        // Add observer
        engine.addObserver(new UserNotificationObserver());
        
        // Create portfolios for users
        portfolioService.getOrCreatePortfolio("user1", 10000.0);
        portfolioService.getOrCreatePortfolio("user2", 10000.0);
        portfolioService.getOrCreatePortfolio("user3", 10000.0);
        
        System.out.println("1. Initial Portfolios:");
        System.out.println(portfolioService.getPortfolio("user1"));
        System.out.println(portfolioService.getPortfolio("user2"));
        System.out.println(portfolioService.getPortfolio("user3"));
        System.out.println();
        
        // Place buy orders using Factory
        System.out.println("2. Placing Buy Orders (using Factory):");
        Order buy1 = OrderFactory.createLimitOrder("user1", "AAPL", OrderType.BUY, 150.0, 100);
        Order buy2 = OrderFactory.createLimitOrder("user2", "AAPL", OrderType.BUY, 151.0, 50);
        engine.placeOrder(buy1);
        engine.placeOrder(buy2);
        System.out.println();
        
        // Place sell orders (should match)
        System.out.println("3. Placing Sell Orders (will match):");
        Order sell1 = OrderFactory.createLimitOrder("user3", "AAPL", OrderType.SELL, 149.0, 75);
        engine.placeOrder(sell1);
        System.out.println();
        
        // Check order status
        System.out.println("4. Order Status:");
        System.out.println(buy1);
        System.out.println(buy2);
        System.out.println(sell1);
        System.out.println();
        
        // Check updated portfolios
        System.out.println("5. Updated Portfolios:");
        System.out.println(portfolioService.getPortfolio("user1"));
        System.out.println(portfolioService.getPortfolio("user2"));
        System.out.println(portfolioService.getPortfolio("user3"));
        System.out.println();
        
        // Place market order using Factory
        System.out.println("6. Placing Market Order (using Factory):");
        Order marketBuy = OrderFactory.createMarketOrder("user1", "AAPL", OrderType.BUY, 25);
        engine.placeOrder(marketBuy);
        System.out.println();
        
        System.out.println("7. Final Portfolios:");
        System.out.println(portfolioService.getPortfolio("user1"));
        System.out.println(portfolioService.getPortfolio("user2"));
        System.out.println(portfolioService.getPortfolio("user3"));
    }
}

