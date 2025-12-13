package org.lld.practice.design_online_auction_system.improved_solution.observers;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;

public interface AuctionObserver {
    void onBidPlaced(Auction auction, Bid bid);
    void onAuctionEnded(Auction auction);
}

