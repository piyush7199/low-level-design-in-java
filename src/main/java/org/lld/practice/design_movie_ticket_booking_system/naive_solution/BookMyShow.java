package org.lld.practice.design_movie_ticket_booking_system.naive_solution;

import java.util.ArrayList;
import java.util.List;

public class BookMyShow {
    private final List<Show> shows;
    private final List<Booking> bookings;
    private int bookingCounter;

    public BookMyShow() {
        this.shows = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.bookingCounter = 1;
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public List<Show> searchShow(String movieName) {
        return shows.stream().filter(show -> show.getMovieName().equalsIgnoreCase(movieName)).toList();
    }

    // Book seats
    public Booking bookSeats(String user, Show show, List<Integer> seatIds) {
        List<Seat> bookedSeats = new ArrayList<>();
        for (int seatId : seatIds) {
            Seat seat = show.getSeats().get(seatId - 1);
            if (seat.isBooked()) {
                System.out.println("Seat " + seatId + " already booked!");
                return null;
            }
            seat.setBooked(true);
            bookedSeats.add(seat);
        }
        Booking booking = new Booking(bookingCounter++, user, show, bookedSeats);
        bookings.add(booking);
        return booking;
    }

    public void makePayment(Booking booking) {
        booking.setPaid(true);
        System.out.println("Payment successful for Booking " + booking.getId());
    }

}
