package org.lld.practice.design_online_hotel_booking_system.naive_solution;

import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.Booking;
import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.Hotel;
import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.Room;
import org.lld.practice.design_online_hotel_booking_system.naive_solution.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingSystem {
    private final List<Hotel> allHotels;
    private final List<Room> allRooms;
    private final List<Booking> allBookings;
    private int bookingCounter;

    public BookingSystem() {
        this.allHotels = new ArrayList<>();
        this.allRooms = new ArrayList<>();
        this.allBookings = new ArrayList<>();
        this.bookingCounter = 1;
    }

    public void addHotel(Hotel hotel) {
        allHotels.add(hotel);
    }

    public void addRoom(Room room) {
        allRooms.add(room);
        room.getHotel().addRoom(room);
    }

    public List<Hotel> searchHotels(String location) {
        List<Hotel> result = new ArrayList<>();
        for (Hotel hotel : allHotels) {
            if (hotel.getLocation().equalsIgnoreCase(location)) {
                result.add(hotel);
            }
        }
        return result;
    }

    public Booking bookRoom(User user, Room room, Date checkIn, Date checkout) {
        for (Booking b : allBookings) {
            if (b.getRoom().getId() == room.getId() && isOverlapping(b.getCheckIn(), b.getCheckOut(), checkIn, checkout)) {
                System.out.println("Room is already booked for this period!");
                return null;
            }
        }

        Booking newBooking = new Booking(bookingCounter++, user, room, checkIn, checkout);
        newBooking.setConfirmed(true); // directly confirm in naive approach
        allBookings.add(newBooking);
        System.out.println("Booking confirmed! Booking ID: " + newBooking.getId());
        return newBooking;
    }

    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }
}
