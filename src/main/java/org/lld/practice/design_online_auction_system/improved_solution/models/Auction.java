package org.lld.practice.design_online_auction_system.improved_solution.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Auction {
    private final String auctionId;
    private final String item;
    private final String sellerId;
    private final double startingPrice;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private AuctionStatus status;
    private double currentBid;
    private String currentWinner;
    private final List<Bid> bidHistory;

    public Auction(String auctionId, String item, String sellerId, double startingPrice,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this.auctionId = auctionId;
        this.item = item;
        this.sellerId = sellerId;
        this.startingPrice = startingPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = AuctionStatus.SCHEDULED;
        this.currentBid = startingPrice;
        this.bidHistory = new ArrayList<>();
    }

    public String getAuctionId() {
        return auctionId;
    }

    public String getItem() {
        return item;
    }

    public String getSellerId() {
        return sellerId;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public String getCurrentWinner() {
        return currentWinner;
    }

    public void setCurrentWinner(String currentWinner) {
        this.currentWinner = currentWinner;
    }

    public void addBid(Bid bid) {
        bidHistory.add(bid);
        if (bid.getAmount() > currentBid) {
            currentBid = bid.getAmount();
            currentWinner = bid.getUserId();
        }
    }

    public List<Bid> getBidHistory() {
        return new ArrayList<>(bidHistory);
    }

    public boolean isActive() {
        return status == AuctionStatus.ACTIVE &&
               LocalDateTime.now().isAfter(startTime) &&
               LocalDateTime.now().isBefore(endTime);
    }

    @Override
    public String toString() {
        return String.format("Auction[id=%s, item=%s, status=%s, currentBid=$%.2f, winner=%s]",
                auctionId, item, status, currentBid, currentWinner);
    }
}

