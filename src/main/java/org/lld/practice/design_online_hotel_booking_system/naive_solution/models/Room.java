package org.lld.practice.design_online_hotel_booking_system.naive_solution.models;

public class Room {
    private final int id;
    private final String type; // "SINGLE", "DOUBLE", etc.
    private final double pricePerNight;
    private final Hotel hotel;

    public Room(int id, String type, double pricePerNight, Hotel hotel) {
        this.id = id;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.hotel = hotel;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public Hotel getHotel() {
        return hotel;
    }
}
