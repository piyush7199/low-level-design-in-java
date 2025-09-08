package org.lld.practice.design_online_hotel_booking_system.improved_solution.services;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Booking;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Room;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.strategies.PricingStrategy;

import java.time.Duration;

public class PaymentService {
    private final PricingStrategy pricingStrategy;

    public PaymentService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;

    }

    public boolean processPayment(Booking booking, Room room) {
        int numberOfNights = (int) Duration.between(booking.getCheckInDate(), booking.getCheckOutDate()).toDays();
        numberOfNights = Math.max(numberOfNights, 1); // Minimum 1 night
        double amount = pricingStrategy.calculatePrice(room, numberOfNights);
        System.out.printf("Processing payment of $%.2f for booking %s%n", amount, booking.getBookingId());
        // In a real system, this would interact with a payment gateway
        return true;
    }
}
