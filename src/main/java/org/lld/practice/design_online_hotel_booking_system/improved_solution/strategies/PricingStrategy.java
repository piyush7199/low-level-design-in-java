package org.lld.practice.design_online_hotel_booking_system.improved_solution.strategies;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Room;

public interface PricingStrategy {
    double calculatePrice(Room room, int numberOfNights);
}
