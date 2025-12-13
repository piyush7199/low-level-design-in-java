package org.lld.practice.design_online_auction_system.improved_solution.states;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;

public class EndedState implements AuctionState {
    @Override
    public void start(Auction auction) {
        System.out.println("Cannot start auction " + auction.getAuctionId() + " - already ended");
    }

    @Override
    public void end(Auction auction) {
        System.out.println("Auction " + auction.getAuctionId() + " is already ended");
    }

    @Override
    public void cancel(Auction auction) {
        System.out.println("Cannot cancel auction " + auction.getAuctionId() + " - already ended");
    }

    @Override
    public Bid placeBid(Auction auction, String userId, double amount) {
        throw new IllegalStateException("Cannot place bid - auction has ended");
    }

    @Override
    public void printStatus(Auction auction) {
        System.out.println("Auction " + auction.getAuctionId() + " is ENDED - Winner: " + auction.getCurrentWinner());
    }
}

