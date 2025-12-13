package org.lld.practice.design_online_auction_system.improved_solution.strategies;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;

/**
 * Standard bidding strategy: bid must be higher than current bid.
 */
public class StandardBiddingStrategy implements BiddingStrategy {
    private static final double MIN_INCREMENT = 1.0;

    @Override
    public boolean canPlaceBid(Auction auction, double amount) {
        return amount > auction.getCurrentBid() + MIN_INCREMENT;
    }

    @Override
    public double getEffectiveBidAmount(Auction auction, double maxAmount) {
        // For standard bidding, return the bid amount as-is
        return maxAmount;
    }
}

