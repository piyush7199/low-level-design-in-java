package org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Booking;

import java.util.*;

public class InMemoryBookingRepository implements Repository<Booking, String> {
    private final Map<String, Booking> bookings;

    public InMemoryBookingRepository() {
        this.bookings = new HashMap<>();
    }

    @Override
    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
}
