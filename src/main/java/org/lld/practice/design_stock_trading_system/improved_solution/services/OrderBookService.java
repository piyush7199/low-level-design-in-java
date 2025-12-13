package org.lld.practice.design_stock_trading_system.improved_solution.services;

import org.lld.practice.design_stock_trading_system.improved_solution.models.Order;
import org.lld.practice.design_stock_trading_system.improved_solution.models.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manages order book for each stock.
 * Uses priority queues for efficient matching.
 */
public class OrderBookService {
    private final ConcurrentHashMap<String, OrderBook> orderBooks = new ConcurrentHashMap<>();
    
    private static class OrderBook {
        private final List<Order> buyOrders = new ArrayList<>();
        private final List<Order> sellOrders = new ArrayList<>();
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        
        public void addOrder(Order order) {
            lock.writeLock().lock();
            try {
                if (order.getType() == org.lld.practice.design_stock_trading_system.improved_solution.models.OrderType.BUY) {
                    buyOrders.add(order);
                } else {
                    sellOrders.add(order);
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        public List<Order> getBuyOrders() {
            lock.readLock().lock();
            try {
                return new ArrayList<>(buyOrders);
            } finally {
                lock.readLock().unlock();
            }
        }
        
        public List<Order> getSellOrders() {
            lock.readLock().lock();
            try {
                return new ArrayList<>(sellOrders);
            } finally {
                lock.readLock().unlock();
            }
        }
        
        public void removeOrder(Order order) {
            lock.writeLock().lock();
            try {
                if (order.getType() == org.lld.practice.design_stock_trading_system.improved_solution.models.OrderType.BUY) {
                    buyOrders.remove(order);
                } else {
                    sellOrders.remove(order);
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
    
    public void addOrder(Order order) {
        OrderBook book = orderBooks.computeIfAbsent(order.getStockSymbol(), k -> new OrderBook());
        book.addOrder(order);
    }
    
    public List<Order> getBuyOrders(String stockSymbol) {
        OrderBook book = orderBooks.get(stockSymbol);
        return book != null ? book.getBuyOrders() : new ArrayList<>();
    }
    
    public List<Order> getSellOrders(String stockSymbol) {
        OrderBook book = orderBooks.get(stockSymbol);
        return book != null ? book.getSellOrders() : new ArrayList<>();
    }
    
    public void removeFilledOrders(String stockSymbol) {
        OrderBook book = orderBooks.get(stockSymbol);
        if (book != null) {
            List<Order> buyOrders = book.getBuyOrders();
            List<Order> sellOrders = book.getSellOrders();
            
            buyOrders.removeIf(o -> o.getStatus() == OrderStatus.FILLED || o.getStatus() == OrderStatus.CANCELLED);
            sellOrders.removeIf(o -> o.getStatus() == OrderStatus.FILLED || o.getStatus() == OrderStatus.CANCELLED);
        }
    }
}

