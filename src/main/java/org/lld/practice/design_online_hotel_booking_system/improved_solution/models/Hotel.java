package org.lld.practice.design_online_hotel_booking_system.improved_solution.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hotel {
    private final String hotelId;
    private final String name;
    private final String city;
    private final List<String> roomIds;

    public Hotel(String name, String city) {
        this.hotelId = UUID.randomUUID().toString();
        this.name = name;
        this.city = city;
        this.roomIds = new ArrayList<>();
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public List<String> getRoomIds() {
        return new ArrayList<>(roomIds);
    }

    public void addRoom(String roomId) {
        roomIds.add(roomId);
    }
}
