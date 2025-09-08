package org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Room;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.RoomStatus;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.RoomType;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRoomRepository implements Repository<Room, String> {
    private final Map<String, Room> rooms;

    public InMemoryRoomRepository() {
        this.rooms = new HashMap<>();
    }

    @Override
    public Optional<Room> findById(String id) {
        return Optional.ofNullable(rooms.get(id));
    }

    @Override
    public void save(Room room) {
        rooms.put(room.getHotelId(), room);
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> findAvailableRooms(String hotelId, RoomType type) {
        return rooms.values().stream()
                .filter(room -> Objects.equals(room.getHotelId(), hotelId) && room.getRoomStatus() == RoomStatus.AVAILABLE && room.getRoomType() == type)
                .collect(Collectors.toList());
    }
}
