package org.lld.practice.design_online_auction_system.naive_solution;

import java.util.HashMap;
import java.util.Map;

/**
 * Naive implementation showing common pitfalls:
 * - Race conditions
 * - No auction states
 * - No bid history
 * - No time management
 */
public class SimpleAuctionSystem {
    private final Map<String, Auction> auctions = new HashMap<>();

    public void createAuction(String auctionId, String item, double startingPrice) {
        auctions.put(auctionId, new Auction(auctionId, item, startingPrice));
    }

    public void placeBid(String auctionId, String userId, double amount) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            System.out.println("Auction not found");
            return;
        }

        // Race condition: Not thread-safe!
        if (amount > auction.getCurrentBid()) {
            auction.setCurrentBid(amount);
            auction.setWinner(userId);
            System.out.println("Bid placed: " + userId + " bid $" + amount);
        } else {
            System.out.println("Bid too low");
        }
    }

    public Auction getAuction(String auctionId) {
        return auctions.get(auctionId);
    }
}

