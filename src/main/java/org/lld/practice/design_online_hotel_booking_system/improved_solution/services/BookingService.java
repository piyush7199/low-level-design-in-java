package org.lld.practice.design_online_hotel_booking_system.improved_solution.services;

import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Booking;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.Room;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.RoomStatus;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.models.User;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryBookingRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryHotelRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryRoomRepository;
import org.lld.practice.design_online_hotel_booking_system.improved_solution.repositories.InMemoryUserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class BookingService {
    private final InMemoryHotelRepository hotelRepository;
    private final InMemoryRoomRepository roomRepository;
    private final InMemoryBookingRepository bookingRepository;
    private final InMemoryUserRepository userRepository;
    private final PaymentService paymentService;

    public BookingService(InMemoryHotelRepository hotelRepository, InMemoryRoomRepository roomRepository, InMemoryBookingRepository bookingRepository, InMemoryUserRepository userRepository, PaymentService paymentService) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
    }

    public Optional<Booking> bookRoom(String userId, String roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        System.out.printf("%nBooking request for User: %s, Room: %s%n", userId, roomId);

        Optional<Room> roomOpt = roomRepository.findById(roomId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (roomOpt.isEmpty() || userOpt.isEmpty()) {
            System.out.println("❌ Room or User not found.");
            return Optional.empty();
        }

        Room room = roomOpt.get();

        synchronized (room) {
            if (room.getRoomStatus() != RoomStatus.AVAILABLE) {
                System.out.println("❌ Room is not available for booking.");
                return Optional.empty();
            }

            room.setRoomStatus(RoomStatus.PENDING);
            roomRepository.save(room);
        }

        Booking booking = new Booking(userId, roomId, checkInDate, checkOutDate);

        if (!paymentService.processPayment(booking, room)) {
            System.out.println("❌ Payment failed. Releasing room.");
            room.setRoomStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
            return Optional.empty();
        }

        bookingRepository.save(booking);
        room.setRoomStatus(RoomStatus.BOOKED);
        roomRepository.save(room);
        System.out.println("✅ Booking successful! Booking ID: " + booking.getBookingId());
        return Optional.of(booking);
    }
}
