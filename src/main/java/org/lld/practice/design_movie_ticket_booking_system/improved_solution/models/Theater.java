package org.lld.practice.design_movie_ticket_booking_system.improved_solution.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Theater {
    private final String theaterId;
    private final String name;
    private final String city;
    private final List<String> showIds;

    public Theater(String name, String city) {
        this.theaterId = UUID.randomUUID().toString();
        this.name = name;
        this.city = city;
        this.showIds = new ArrayList<>();
    }

    public String getTheaterId() {
        return theaterId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public List<String> getShowIds() {
        return new ArrayList<>(showIds);
    }

    public void addShow(String showId) {
        showIds.add(showId);
    }
}
