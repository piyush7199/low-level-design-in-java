package org.lld.practice.design_online_auction_system.naive_solution;

public class Auction {
    private String auctionId;
    private String item;
    private double startingPrice;
    private double currentBid;
    private String winner;

    public Auction(String auctionId, String item, double startingPrice) {
        this.auctionId = auctionId;
        this.item = item;
        this.startingPrice = startingPrice;
        this.currentBid = startingPrice;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public String getItem() {
        return item;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return String.format("Auction[id=%s, item=%s, currentBid=$%.2f, winner=%s]",
                auctionId, item, currentBid, winner);
    }
}

