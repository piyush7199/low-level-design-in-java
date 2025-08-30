package org.lld.practice.design_movie_ticket_booking_system.improved_solution.repositories;

import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Booking;

import java.util.HashMap;
import java.util.Map;

public class InMemoryBookingRepository implements Repository<Booking, String> {
    private final Map<String, Booking> bookings = new HashMap<>();

    @Override
    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
    }

    @Override
    public Booking findById(String id) {
        return bookings.get(id);
    }
}
