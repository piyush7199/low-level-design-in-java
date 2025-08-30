package org.lld.practice.design_movie_ticket_booking_system.improved_solution;

import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Booking;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Seat;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Show;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.repositories.InMemoryBookingRepository;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.repositories.InMemoryShowRepository;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.repositories.Repository;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.services.BookingService;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.services.PaymentService;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.strategies.impl.CreditCardPayment;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.utility.SeatLockProvider;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Repository<Show, String> showRepo = new InMemoryShowRepository();
        Repository<Booking, String> bookingRepo = new InMemoryBookingRepository(); //

        // Setup Services
        SeatLockProvider lockProvider = new SeatLockProvider(10); // 10-second lock timeout
        PaymentService paymentService = new PaymentService();
        BookingService bookingService = new BookingService(showRepo, bookingRepo, lockProvider, paymentService);

        Show avengersShow = new Show("movie-1", "theater-1", LocalDateTime.now(), 5, 5); // 25 seats
        showRepo.save(avengersShow);
        List<Seat> allSeats = avengersShow.getSeats();
        String showId = avengersShow.getShowId();
        System.out.println("Total seats available: " + allSeats.size());

        // --- Test Case 1: Two users try to book the same seats concurrently ---
        System.out.println("\n--- Concurrent Booking Test ---");

        // User A tries to book the first two seats
        List<String> userA_seats = Arrays.asList(allSeats.get(0).getSeatId(), allSeats.get(1).getSeatId());

        // User B tries to book the same seats at the same time
        List<String> userB_seats = Arrays.asList(allSeats.get(0).getSeatId(), allSeats.get(1).getSeatId());

        // Simulate concurrent requests using threads
        Thread userA_thread = new Thread(() -> {
            System.out.println("User A is attempting to book.");
            bookingService.bookTickets("user-A", showId, userA_seats, new CreditCardPayment());
        });

        Thread userB_thread = new Thread(() -> {
            System.out.println("User B is attempting to book.");
            bookingService.bookTickets("user-B", showId, userB_seats, new CreditCardPayment());
        });

        userA_thread.start();
        userB_thread.start();

        // Wait for threads to finish
        try {
            userA_thread.join();
            userB_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n--- Check seat status after concurrent booking ---");
        System.out.println("Seat " + allSeats.get(0).getSeatId() + " status: " + allSeats.get(0).getSeatStatus());

        // Test Case 2: A different user books different seats
        System.out.println("\n--- Non-Conflicting Booking Test ---");
        List<String> userC_seats = Arrays.asList(allSeats.get(2).getSeatId(), allSeats.get(3).getSeatId());
        bookingService.bookTickets("user-C", showId, userC_seats, new CreditCardPayment());

    }
}
