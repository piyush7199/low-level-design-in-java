package org.lld.practice.design_movie_ticket_booking_system.improved_solution.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Show {
    private final String showId;
    private final String movieId;
    private final String theaterId;
    private final LocalDateTime showTime;
    private final List<Seat> seats;

    public Show(String movieId, String theaterId, LocalDateTime showTime, int numRows, int numCols) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.showTime = showTime;
        this.showId = UUID.randomUUID().toString();
        this.seats = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                seats.add(new Seat(i + 1, j + 1));
            }
        }
    }

    public String getShowId() {
        return showId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public List<Seat> getSeats() {
        return new ArrayList<>(seats);
    }
}
