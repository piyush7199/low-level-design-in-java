package org.lld.practice.design_online_hotel_booking_system.improved_solution.models;

import java.util.UUID;

public class Room {
    private final String roomId;
    private final String hotelId;
    private final int roomNumber;
    private final RoomType roomType;
    private RoomStatus roomStatus;
    private final double pricePerNight;

    public Room(String hotelId, int roomNumber, RoomType roomType, double pricePerNight) {
        this.roomId = UUID.randomUUID().toString();
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = RoomStatus.AVAILABLE;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}
