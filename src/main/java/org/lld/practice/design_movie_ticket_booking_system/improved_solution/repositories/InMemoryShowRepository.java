package org.lld.practice.design_movie_ticket_booking_system.improved_solution.repositories;

import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Show;

import java.util.HashMap;
import java.util.Map;

public class InMemoryShowRepository implements Repository<Show, String> {
    private final Map<String, Show> shows = new HashMap<>();

    @Override
    public void save(Show show) {
        shows.put(show.getShowId(), show);
    }

    @Override
    public Show findById(String id) {
        return shows.get(id);
    }
}
