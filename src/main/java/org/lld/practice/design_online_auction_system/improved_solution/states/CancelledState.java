package org.lld.practice.design_online_auction_system.improved_solution.states;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;

public class CancelledState implements AuctionState {
    @Override
    public void start(Auction auction) {
        System.out.println("Cannot start auction " + auction.getAuctionId() + " - already cancelled");
    }

    @Override
    public void end(Auction auction) {
        System.out.println("Cannot end auction " + auction.getAuctionId() + " - already cancelled");
    }

    @Override
    public void cancel(Auction auction) {
        System.out.println("Auction " + auction.getAuctionId() + " is already cancelled");
    }

    @Override
    public Bid placeBid(Auction auction, String userId, double amount) {
        throw new IllegalStateException("Cannot place bid - auction has been cancelled");
    }

    @Override
    public void printStatus(Auction auction) {
        System.out.println("Auction " + auction.getAuctionId() + " is CANCELLED");
    }
}

