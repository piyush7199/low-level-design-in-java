package org.lld.practice.design_movie_ticket_booking_system.naive_solution;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookMyShow bms = new BookMyShow();

        // Create a show
        Show show1 = new Show(1, "Inception", "PVR Koramangala", new Date(), 10);
        bms.addShow(show1);

        // Search for shows
        List<Show> inceptionShows = bms.searchShow("Inception");
        System.out.println("Found " + inceptionShows.size() + " shows");

        // Book seats
        Booking booking = bms.bookSeats("Alice", show1, Arrays.asList(3, 4, 5));
        if (booking != null) {
            bms.makePayment(booking);
        }
    }
}
