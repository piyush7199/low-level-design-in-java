package org.lld.practice.design_online_hotel_booking_system.naive_solution.models;

import java.util.Date;

public class Booking {
    private final int id;
    private final User user;
    private final Room room;
    private final Date checkIn;
    private final Date checkOut;
    private boolean isConfirmed;

    public Booking(int id, User user, Room room, Date checkIn, Date checkOut) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.isConfirmed = false;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
