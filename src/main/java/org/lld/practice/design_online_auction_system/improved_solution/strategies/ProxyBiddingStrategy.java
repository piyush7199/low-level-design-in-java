package org.lld.practice.design_online_auction_system.improved_solution.strategies;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;

/**
 * Proxy bidding strategy: automatically bid up to maximum amount.
 */
public class ProxyBiddingStrategy implements BiddingStrategy {
    private static final double MIN_INCREMENT = 1.0;

    @Override
    public boolean canPlaceBid(Auction auction, double maxAmount) {
        return maxAmount > auction.getCurrentBid() + MIN_INCREMENT;
    }

    @Override
    public double getEffectiveBidAmount(Auction auction, double maxAmount) {
        // For proxy bidding, bid minimum increment above current bid, up to max
        double nextBid = auction.getCurrentBid() + MIN_INCREMENT;
        return Math.min(nextBid, maxAmount);
    }
}

