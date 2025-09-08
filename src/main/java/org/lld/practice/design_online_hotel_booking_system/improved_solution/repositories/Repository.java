package org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    Optional<T> findById(ID id);

    void save(T entity);

    List<T> findAll();
}
