package org.lld.practice.design_online_auction_system.naive_solution;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Online Auction System ===\n");

        SimpleAuctionSystem system = new SimpleAuctionSystem();

        system.createAuction("A1", "Vintage Watch", 100.0);
        system.placeBid("A1", "user1", 150.0);
        system.placeBid("A1", "user2", 200.0);
        system.placeBid("A1", "user1", 180.0); // Too low

        System.out.println("\nAuction Status:");
        System.out.println(system.getAuction("A1"));

        System.out.println("\nLimitations:");
        System.out.println("- Race conditions (not thread-safe)");
        System.out.println("- No auction states");
        System.out.println("- No bid history");
        System.out.println("- No time management");
    }
}

