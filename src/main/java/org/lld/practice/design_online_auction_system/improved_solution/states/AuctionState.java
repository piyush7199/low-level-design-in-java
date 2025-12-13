package org.lld.practice.design_online_auction_system.improved_solution.states;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;

/**
 * State interface for Auction lifecycle management.
 */
public interface AuctionState {
    void start(Auction auction);
    void end(Auction auction);
    void cancel(Auction auction);
    Bid placeBid(Auction auction, String userId, double amount);
    void printStatus(Auction auction);
}

