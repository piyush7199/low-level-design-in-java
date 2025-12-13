package org.lld.practice.design_online_auction_system.improved_solution.strategies;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;

/**
 * Strategy interface for bidding rules and validation.
 */
public interface BiddingStrategy {
    /**
     * Validate if a bid can be placed.
     * @param auction The auction
     * @param amount The bid amount
     * @return true if bid is valid
     */
    boolean canPlaceBid(Auction auction, double amount);
    
    /**
     * Get the effective bid amount (for proxy bidding).
     * @param auction The auction
     * @param maxAmount Maximum amount user is willing to pay
     * @return Effective bid amount
     */
    double getEffectiveBidAmount(Auction auction, double maxAmount);
}

