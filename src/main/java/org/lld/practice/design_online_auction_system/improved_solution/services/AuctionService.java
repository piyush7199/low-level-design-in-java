package org.lld.practice.design_online_auction_system.improved_solution.services;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.AuctionStatus;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;
import org.lld.practice.design_online_auction_system.improved_solution.observers.AuctionObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton service managing auctions and bids.
 */
public class AuctionService {
    private static AuctionService instance;
    private final ConcurrentHashMap<String, Auction> auctions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> auctionLocks = new ConcurrentHashMap<>();
    private final List<AuctionObserver> observers = new ArrayList<>();
    private int bidCounter = 1;

    private AuctionService() {
    }

    public static synchronized AuctionService getInstance() {
        if (instance == null) {
            instance = new AuctionService();
        }
        return instance;
    }

    public Auction createAuction(String auctionId, String item, String sellerId,
                                  double startingPrice, LocalDateTime startTime, LocalDateTime endTime) {
        Auction auction = new Auction(auctionId, item, sellerId, startingPrice, startTime, endTime);
        auctions.put(auctionId, auction);
        auctionLocks.put(auctionId, new ReentrantLock());
        return auction;
    }

    public Bid placeBid(String auctionId, String userId, double amount) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            throw new IllegalArgumentException("Auction not found: " + auctionId);
        }

        ReentrantLock lock = auctionLocks.get(auctionId);
        lock.lock();
        try {
            // Validate auction status
            if (!auction.isActive()) {
                throw new IllegalStateException("Auction is not active: " + auction.getStatus());
            }

            // Validate bid amount
            if (amount <= auction.getCurrentBid()) {
                throw new IllegalArgumentException("Bid must be higher than current bid: $" + auction.getCurrentBid());
            }

            // Create and add bid
            String bidId = "B" + bidCounter++;
            Bid bid = new Bid(bidId, auctionId, userId, amount);
            auction.addBid(bid);

            // Notify observers
            notifyObservers(auction, bid);

            return bid;
        } finally {
            lock.unlock();
        }
    }

    public void endAuction(String auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction != null) {
            auction.setStatus(AuctionStatus.ENDED);
            notifyAuctionEnded(auction);
        }
    }

    public Auction getAuction(String auctionId) {
        return auctions.get(auctionId);
    }

    public void addObserver(AuctionObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(Auction auction, Bid bid) {
        for (AuctionObserver observer : observers) {
            observer.onBidPlaced(auction, bid);
        }
    }

    private void notifyAuctionEnded(Auction auction) {
        for (AuctionObserver observer : observers) {
            observer.onAuctionEnded(auction);
        }
    }
}

