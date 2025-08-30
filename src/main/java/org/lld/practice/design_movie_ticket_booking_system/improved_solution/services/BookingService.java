package org.lld.practice.design_movie_ticket_booking_system.improved_solution.services;

import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Booking;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Seat;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.SeatStatus;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Show;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.repositories.Repository;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.strategies.PaymentStrategy;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.utility.SeatLockProvider;

import java.util.List;
import java.util.Optional;

public class BookingService {
    private final Repository<Show, String> showRepository;
    private final Repository<Booking, String> bookingRepository;
    private final SeatLockProvider seatLockProvider;
    private final PaymentService paymentService;

    public BookingService(Repository<Show, String> showRepository, Repository<Booking, String> bookingRepository, SeatLockProvider seatLockProvider, PaymentService paymentService) {
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.seatLockProvider = seatLockProvider;
        this.paymentService = paymentService;
    }

    public Optional<Booking> bookTickets(String userId, String showId, List<String> selectedSeatIds, PaymentStrategy paymentStrategy) {
        Show show = showRepository.findById(showId);
        if (show == null) {
            System.out.println("Invalid show ID.");
            return Optional.empty();
        }

        List<Seat> selectedSeats = show.getSeats().stream()
                .filter(s -> selectedSeatIds.contains(s.getSeatId()))
                .toList();
        if (!seatLockProvider.lockSeats(selectedSeats, userId)) {
            System.out.println("❌ Failed to lock seats. Some seats might be in use.");
            return Optional.empty();
        }

        // Process Payment
        double totalAmount = selectedSeats.size() * 10.0;
        if (!paymentService.processPayment(totalAmount, paymentStrategy)) {
            System.out.println("Payment failed. Unlocking seats.");
            selectedSeats.forEach(s -> seatLockProvider.unlockSeat(s.getSeatId(), userId));
            return Optional.empty();
        }

        // Step 3: Finalize booking and update seat status
        selectedSeats.forEach(s -> s.setSeatStatus(SeatStatus.BOOKED));
        Booking booking = new Booking(userId, showId, selectedSeatIds);
        bookingRepository.save(booking);

        System.out.println("Booking successful for user " + userId + ". Booking ID: " + booking.getBookingId());
        return Optional.of(booking);
    }
}
