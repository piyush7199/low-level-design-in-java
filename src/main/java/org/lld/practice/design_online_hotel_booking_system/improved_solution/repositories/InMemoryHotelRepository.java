package org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Hotel;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryHotelRepository implements Repository<Hotel, String> {
    private final Map<String, Hotel> hotels;

    public InMemoryHotelRepository() {
        this.hotels = new HashMap<>();
    }

    @Override
    public Optional<Hotel> findById(String id) {
        return Optional.ofNullable(hotels.get(id));
    }

    @Override
    public void save(Hotel hotel) {
        hotels.put(hotel.getHotelId(), hotel);
    }

    @Override
    public List<Hotel> findAll() {
        return new ArrayList<>(hotels.values());
    }

    public List<Hotel> findByCity(String city) {
        return hotels.values().stream().filter(hotel -> (hotel.getCity().equalsIgnoreCase(city))).collect(Collectors.toList());
    }
}
