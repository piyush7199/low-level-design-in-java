package org.lld.practice.design_stock_trading_system.improved_solution.services;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderStatus;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderType;
import org.lld.practice.design_stock_trading_system.improved_solution.models.Trade;
import org.lld.practice.design_stock_trading_system.improved_solution.observers.TradeObserver;
import org.lld.practice.design_stock_trading_system.improved_solution.strategies.MatchingStrategy;
import org.lld.practice.design_stock_trading_system.improved_solution.strategies.PriceTimePriorityStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton Trading Engine that manages order matching and execution.
 */
public class TradingEngine {
    private static TradingEngine instance;
    private final OrderBookService orderBookService;
    private final PortfolioService portfolioService;
    private final MatchingStrategy matchingStrategy;
    private final List<TradeObserver> observers = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private int tradeCounter = 1;
    
    private TradingEngine() {
        this.orderBookService = new OrderBookService();
        this.portfolioService = new PortfolioService();
        this.matchingStrategy = new PriceTimePriorityStrategy();
    }
    
    public static synchronized TradingEngine getInstance() {
        if (instance == null) {
            instance = new TradingEngine();
        }
        return instance;
    }
    
    public void placeOrder(Order order) {
        lock.lock();
        try {
            // Validate order
            if (!portfolioService.canAfford(order)) {
                throw new IllegalArgumentException("Insufficient funds or holdings for order: " + order.getOrderId());
            }
            
            // Add to order book
            orderBookService.addOrder(order);
            
            // Try to match
            matchAndExecute(order);
        } finally {
            lock.unlock();
        }
    }
    
    private void matchAndExecute(Order order) {
        List<Order> oppositeOrders = getOppositeOrders(order);
        List<MatchingStrategy.MatchResult> matches = matchingStrategy.findMatches(order, oppositeOrders);
        
        for (MatchingStrategy.MatchResult match : matches) {
            Order oppositeOrder = match.getOrder();
            int quantity = match.getQuantity();
            
            // Execute trade
            Trade trade = executeTrade(order, oppositeOrder, quantity);
            
            // Notify observers
            notifyObservers(trade, order, oppositeOrder);
        }
    }
    
    private List<Order> getOppositeOrders(Order order) {
        if (order.getType() == OrderType.BUY) {
            return orderBookService.getSellOrders(order.getStockSymbol());
        } else {
            return orderBookService.getBuyOrders(order.getStockSymbol());
        }
    }
    
    private Trade executeTrade(Order buyOrder, Order sellOrder, int quantity) {
        // Determine execution price (use limit price if available, else market price)
        double executionPrice = determineExecutionPrice(buyOrder, sellOrder);
        
        // Create trade
        String tradeId = "T" + tradeCounter++;
        Trade trade = new Trade(tradeId, buyOrder.getStockSymbol(), 
                buyOrder.getOrderId(), sellOrder.getOrderId(), executionPrice, quantity);
        
        // Update orders
        buyOrder.fill(quantity);
        sellOrder.fill(quantity);
        
        // Update portfolios
        portfolioService.executeTrade(trade, buyOrder, sellOrder);
        
        System.out.println("Executed: " + trade);
        
        return trade;
    }
    
    private double determineExecutionPrice(Order buyOrder, Order sellOrder) {
        // Use the limit price if available, otherwise use market price
        if (buyOrder.getSide() == org.lld.practice.design_stock_trading_system.improved_solution.models.OrderSide.LIMIT &&
            sellOrder.getSide() == org.lld.practice.design_stock_trading_system.improved_solution.models.OrderSide.LIMIT) {
            // Both are limit orders - use the price that was in the market first
            return sellOrder.getPrice(); // Typically use the price of the order already in the book
        } else if (buyOrder.getSide() == org.lld.practice.design_stock_trading_system.improved_solution.models.OrderSide.MARKET) {
            return sellOrder.getPrice();
        } else {
            return buyOrder.getPrice();
        }
    }
    
    public void cancelOrder(String orderId, String userId) {
        lock.lock();
        try {
            // Find order in order book
            // In real system, would maintain order lookup map
            // For simplicity, we'll search through order books
            // This is simplified - in production, use a HashMap for O(1) lookup
            List<Order> allOrders = new ArrayList<>();
            orderBookService.getBuyOrders("AAPL").forEach(allOrders::add);
            orderBookService.getSellOrders("AAPL").forEach(allOrders::add);
            
            for (Order order : allOrders) {
                if (order.getOrderId().equals(orderId) && order.getUserId().equals(userId)) {
                    if (order.getStatus() == OrderStatus.PENDING || 
                        order.getStatus() == OrderStatus.PARTIALLY_FILLED) {
                        order.setStatus(OrderStatus.CANCELLED);
                        System.out.println("Order " + orderId + " cancelled");
                        return;
                    }
                }
            }
            throw new IllegalArgumentException("Order not found or cannot be cancelled: " + orderId);
        } finally {
            lock.unlock();
        }
    }
    
    public void addObserver(TradeObserver observer) {
        observers.add(observer);
    }
    
    private void notifyObservers(Trade trade, Order buyOrder, Order sellOrder) {
        for (TradeObserver observer : observers) {
            observer.onTradeExecuted(trade, buyOrder, sellOrder);
        }
    }
    
    public OrderBookService getOrderBookService() {
        return orderBookService;
    }
    
    public PortfolioService getPortfolioService() {
        return portfolioService;
    }
}

