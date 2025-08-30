package org.lld.practice.design_movie_ticket_booking_system.improved_solution.models;

import java.util.UUID;

public class Movie {
    private final String movieId;
    private final String title;
    private final int durationMinutes;
    private final String language;

    public Movie(String title, int durationMinutes, String language) {
        this.movieId = UUID.randomUUID().toString();
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.language = language;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getLanguage() {
        return language;
    }
}
