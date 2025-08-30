package org.lld.practice.design_movie_ticket_booking_system.improved_solution.models;

import java.util.UUID;

public class Seat {
    private final String seatId;
    private final int row;
    private final int col;
    private SeatStatus seatStatus;

    public Seat(int row, int col) {
        this.seatId = UUID.randomUUID().toString();
        this.row = row;
        this.col = col;
        this.seatStatus = SeatStatus.AVAILABLE;
    }

    public String getSeatId() {
        return seatId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
    }
}
