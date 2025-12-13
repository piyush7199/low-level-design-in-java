package org.lld.practice.design_stock_trading_system.improved_solution.services;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderType;
import org.lld.practice.design_stock_trading_system.improved_solution.models.Portfolio;
import org.lld.practice.design_stock_trading_system.improved_solution.models.Trade;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages user portfolios and holdings.
 */
public class PortfolioService {
    private final ConcurrentHashMap<String, Portfolio> portfolios = new ConcurrentHashMap<>();
    
    public Portfolio getOrCreatePortfolio(String userId, double initialBalance) {
        return portfolios.computeIfAbsent(userId, k -> new Portfolio(userId, initialBalance));
    }
    
    public Portfolio getPortfolio(String userId) {
        Portfolio portfolio = portfolios.get(userId);
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio not found for user: " + userId);
        }
        return portfolio;
    }
    
    public void executeTrade(Trade trade, Order buyOrder, Order sellOrder) {
        Portfolio buyPortfolio = getPortfolio(buyOrder.getUserId());
        Portfolio sellPortfolio = getPortfolio(sellOrder.getUserId());
        
        double totalValue = trade.getPrice() * trade.getQuantity();
        
        // Update buyer portfolio
        buyPortfolio.deductCash(totalValue);
        buyPortfolio.addHolding(trade.getStockSymbol(), trade.getQuantity());
        
        // Update seller portfolio
        sellPortfolio.addCash(totalValue);
        sellPortfolio.deductHolding(trade.getStockSymbol(), trade.getQuantity());
    }
    
    public boolean canAfford(Order order) {
        Portfolio portfolio = getPortfolio(order.getUserId());
        if (order.getType() == OrderType.BUY) {
            double requiredCash = order.getPrice() * order.getQuantity();
            return portfolio.getCashBalance() >= requiredCash;
        } else {
            int availableQuantity = portfolio.getHolding(order.getStockSymbol());
            return availableQuantity >= order.getQuantity();
        }
    }
}

