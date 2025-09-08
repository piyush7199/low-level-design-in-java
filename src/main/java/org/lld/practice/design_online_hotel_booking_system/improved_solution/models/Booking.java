package org.lld.practice.design_online_hotel_booking_system.improved_solution.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Booking {
    private final String bookingId;
    private final String userId;
    private final String roomId;
    private final LocalDateTime checkInDate;
    private final LocalDateTime checkOutDate;
    private final LocalDateTime bookingTime;


    public Booking(String userId, String roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        this.bookingId = UUID.randomUUID().toString();
        this.userId = userId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingTime = LocalDateTime.now();
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
}
