package org.lld.practice.design_movie_ticket_booking_system.naive_solution;

public class Seat {
    private final int id;
    private boolean isBooked;

    public Seat(int id) {
        this.id = id;
        this.isBooked = false;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public int getId() {
        return id;
    }
}
