package org.lld.practice.design_online_hotel_booking_system.naive_solution;

import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.Booking;
import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.Hotel;
import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.Room;
import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();

        // Create hotels and rooms
        Hotel h1 = new Hotel(1, "The Grand Palace", "Bangalore");
        Hotel h2 = new Hotel(2, "Sea Breeze Resort", "Goa");
        system.addHotel(h1);
        system.addHotel(h2);

        system.addRoom(new Room(101, "SINGLE", 1500.0, h1));
        system.addRoom(new Room(102, "DOUBLE", 2500.0, h1));
        system.addRoom(new Room(201, "SINGLE", 2000.0, h2));

        // Search hotels
        List<Hotel> hotelsInGoa = system.searchHotels("Goa");
        System.out.println("Hotels in Goa: " + hotelsInGoa.size());

        // Create a user
        User user = new User(1, "Alice");

        // Book a room
        Date checkIn = new GregorianCalendar(2025, Calendar.SEPTEMBER, 1).getTime();
        Date checkOut = new GregorianCalendar(2025, Calendar.SEPTEMBER, 5).getTime();

        Booking booking = system.bookRoom(user, hotelsInGoa.getFirst().getRooms().getFirst(), checkIn, checkOut);

        if (booking != null) {
            System.out.println("Booking successful for " + user.name() + " at " + booking.getRoom().getHotel().getName());
        }
    }
}
