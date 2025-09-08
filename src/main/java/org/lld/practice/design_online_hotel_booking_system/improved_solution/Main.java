package org.lld.practice.design_online_hotel_booking_system.improved_solution;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Hotel;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.RoomType;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.User;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryBookingRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryHotelRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryRoomRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryUserRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.services.BookingService;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.services.HotelService;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.services.PaymentService;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.strategies.StandardPricing;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 1. Initialize Repositories and Services
        InMemoryHotelRepository hotelRepo = new InMemoryHotelRepository();
        InMemoryRoomRepository roomRepo = new InMemoryRoomRepository();
        InMemoryBookingRepository bookingRepo = new InMemoryBookingRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();

        // 2. Setup System Components
        Hotel hotelA = new Hotel("Grand Hyatt", "New York");
        hotelRepo.save(hotelA);

        HotelService hotelService = new HotelService(hotelRepo, roomRepo);
        hotelService.addRoomToHotel(hotelA.getHotelId(), 101, RoomType.DOUBLE, 250.0);
        hotelService.addRoomToHotel(hotelA.getHotelId(), 102, RoomType.DOUBLE, 250.0);

        User user1 = new User("Alice");
        User user2 = new User("Bob");
        userRepo.save(user1);
        userRepo.save(user2);

        // Services with their dependencies
        PaymentService paymentService = new PaymentService(new StandardPricing());
        BookingService bookingService = new BookingService(hotelRepo, roomRepo, bookingRepo, userRepo, paymentService);

        String roomIdToBook = roomRepo.findAvailableRooms(hotelA.getHotelId(), RoomType.DOUBLE).getFirst().getRoomId();

        // 3. Demonstrate System Flow

        System.out.println("--- Test Case 1: Successful Booking ---");
        bookingService.bookRoom(user1.getUserId(), roomIdToBook, LocalDateTime.now(), LocalDateTime.now().plusDays(2));

        System.out.println("\n--- Test Case 2: Concurrency Attempt on the Same Room ---");
        // Create a new room for the concurrency test
        hotelService.addRoomToHotel(hotelA.getHotelId(), 103, RoomType.SINGLE, 150.0);
        String singleRoomId = roomRepo.findAvailableRooms(hotelA.getHotelId(), RoomType.SINGLE).get(0).getRoomId();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // User 1 tries to book the single room
        executor.submit(() -> bookingService.bookRoom(user1.getUserId(), singleRoomId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)));

        // User 2 tries to book the same single room at the same time
        executor.submit(() -> bookingService.bookRoom(user2.getUserId(), singleRoomId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
