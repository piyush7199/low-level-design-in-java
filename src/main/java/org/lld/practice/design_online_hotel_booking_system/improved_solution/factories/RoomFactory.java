package org.lld.practice.design_online_hotel_booking_system.improved_solution.factories;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Room;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.RoomType;

public class RoomFactory {
    public static Room createRoom(String hotelId, int roomNo, RoomType roomType, double pricePerNight) {
        return new Room(hotelId, roomNo, roomType, pricePerNight);
    }
}
