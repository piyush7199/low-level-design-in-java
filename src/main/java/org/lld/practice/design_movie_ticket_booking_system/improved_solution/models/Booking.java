package org.lld.practice.design_movie_ticket_booking_system.improved_solution.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Booking {
    private final String bookingId;
    private final String userId;
    private final String showId;
    private final List<String> bookedSeatIds;
    private final LocalDateTime bookingTime;

    public Booking(String userId, String showId, List<String> bookedSeatIds) {
        this.bookingId = UUID.randomUUID().toString();
        this.userId = userId;
        this.showId = showId;
        this.bookedSeatIds = bookedSeatIds;
        this.bookingTime = LocalDateTime.now();
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getShowId() {
        return showId;
    }

    public List<String> getBookedSeatIds() {
        return bookedSeatIds;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
}
