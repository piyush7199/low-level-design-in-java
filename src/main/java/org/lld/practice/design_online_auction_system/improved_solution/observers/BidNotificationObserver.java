package org.lld.practice.design_online_auction_system.improved_solution.observers;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;

/**
 * Notifies users about bid updates and auction end.
 */
public class BidNotificationObserver implements AuctionObserver {
    @Override
    public void onBidPlaced(Auction auction, Bid bid) {
        System.out.println("Notification: New bid on " + auction.getItem() + 
                " - $" + bid.getAmount() + " by " + bid.getUserId());
        if (auction.getCurrentWinner() != null && !auction.getCurrentWinner().equals(bid.getUserId())) {
            System.out.println("Notification: You've been outbid on " + auction.getItem());
        }
    }

    @Override
    public void onAuctionEnded(Auction auction) {
        if (auction.getCurrentWinner() != null) {
            System.out.println("Notification: Auction ended! Winner: " + auction.getCurrentWinner() + 
                    " with bid $" + auction.getCurrentBid());
        } else {
            System.out.println("Notification: Auction ended with no winner");
        }
    }
}

