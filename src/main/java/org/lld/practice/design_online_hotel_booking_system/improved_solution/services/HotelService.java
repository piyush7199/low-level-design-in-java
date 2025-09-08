package org.lld.practice.design_online_hotel_booking_system.improved_solution.services;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.factories.RoomFactory;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Hotel;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Room;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.RoomType;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryHotelRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryRoomRepository;

public class HotelService {
    private final InMemoryHotelRepository hotelRepository;
    private final InMemoryRoomRepository roomRepository;

    public HotelService(InMemoryHotelRepository hotelRepository, InMemoryRoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public void addRoomToHotel(String hotelId, int roomNumber, RoomType type, double price) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found."));
        Room room = RoomFactory.createRoom(hotel.getHotelId(), roomNumber, type, price);
        roomRepository.save(room);
        hotel.addRoom(room.getRoomId());
        hotelRepository.save(hotel);
    }
}
