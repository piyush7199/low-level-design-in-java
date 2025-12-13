package org.lld.practice.design_online_auction_system.improved_solution.states;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.AuctionStatus;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;

public class ActiveState implements AuctionState {
    @Override
    public void start(Auction auction) {
        System.out.println("Auction " + auction.getAuctionId() + " is already active");
    }

    @Override
    public void end(Auction auction) {
        auction.setStatus(AuctionStatus.ENDED);
        System.out.println("Auction " + auction.getAuctionId() + " ended");
    }

    @Override
    public void cancel(Auction auction) {
        auction.setStatus(AuctionStatus.CANCELLED);
        System.out.println("Auction " + auction.getAuctionId() + " cancelled");
    }

    @Override
    public Bid placeBid(Auction auction, String userId, double amount) {
        // Bid placement logic handled by AuctionService
        // This is a placeholder - actual bid creation happens in service
        return null;
    }

    @Override
    public void printStatus(Auction auction) {
        System.out.println("Auction " + auction.getAuctionId() + " is ACTIVE - Current bid: $" + auction.getCurrentBid());
    }
}

