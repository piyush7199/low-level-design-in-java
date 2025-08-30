package org.lld.practice.design_movie_ticket_booking_system.improved_solution.repositories;

public interface Repository<T, ID> {
    void save(T entity);

    T findById(ID id);
}
