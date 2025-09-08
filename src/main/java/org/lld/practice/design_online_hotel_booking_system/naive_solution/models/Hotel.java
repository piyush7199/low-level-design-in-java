package org.lld.practice.design_online_hotel_booking_system.naive_solution.models;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private final int id;
    private final String name;
    private final String location;
    private final List<Room> rooms;

    public Hotel(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rooms = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}
