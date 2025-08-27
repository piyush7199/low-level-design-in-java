package org.lld.practice.design_movie_ticket_booking_system.naive_solution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Show {
    private final int id;
    private String movieName;
    private String theaterName;
    private Date startTime;
    private final List<Seat> seats;

    Show(int id, String movieName, String theaterName, Date startTime, int seatCount) {
        this.id = id;
        this.movieName = movieName;
        this.theaterName = theaterName;
        this.startTime = startTime;
        this.seats = new ArrayList<>();
        for (int i = 1; i <= seatCount; i++) {
            seats.add(new Seat(i));
        }
    }

    public int getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public List<Seat> getSeats() {
        return new ArrayList<>(seats);
    }
}
