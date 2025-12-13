package org.lld.practice.design_online_auction_system.improved_solution;

import org.lld.practice.design_online_auction_system.improved_solution.models.Auction;
import org.lld.practice.design_online_auction_system.improved_solution.models.AuctionStatus;
import org.lld.practice.design_online_auction_system.improved_solution.models.Bid;
import org.lld.practice.design_online_auction_system.improved_solution.observers.BidNotificationObserver;
import org.lld.practice.design_online_auction_system.improved_solution.services.AuctionService;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Online Auction System Demo ===\n");

        AuctionService auctionService = AuctionService.getInstance();
        auctionService.addObserver(new BidNotificationObserver());

        // Create auction
        System.out.println("1. Creating auction:");
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        Auction auction = auctionService.createAuction("A1", "Vintage Watch", "seller1",
                100.0, startTime, endTime);
        auction.setStatus(AuctionStatus.ACTIVE);
        System.out.println(auction);
        System.out.println();

        // Place bids
        System.out.println("2. Placing bids:");
        Bid bid1 = auctionService.placeBid("A1", "user1", 150.0);
        Bid bid2 = auctionService.placeBid("A1", "user2", 200.0);
        Bid bid3 = auctionService.placeBid("A1", "user1", 250.0);
        System.out.println();

        // Try invalid bid
        System.out.println("3. Trying invalid bid (too low):");
        try {
            auctionService.placeBid("A1", "user3", 180.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();

        // Show bid history
        System.out.println("4. Bid History:");
        auction.getBidHistory().forEach(System.out::println);
        System.out.println();

        // End auction
        System.out.println("5. Ending auction:");
        auctionService.endAuction("A1");
        System.out.println(auction);
    }
}

