package org.lld.practice.design_movie_ticket_booking_system.naive_solution;

import java.util.List;

public class Booking {
    private final int id;
    private final String user;
    private final Show show;
    private final List<Seat> bookedSeats;
    private boolean isPaid;

    Booking(int id, String user, Show show, List<Seat> bookedSeats) {
        this.id = id;
        this.user = user;
        this.show = show;
        this.bookedSeats = bookedSeats;
        this.isPaid = false;
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getBookedSeats() {
        return bookedSeats;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}