package org.lld.practice.design_movie_ticket_booking_system.improved_solution.utility;

import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.Seat;
import org.lld.practice.design_movie_ticket_booking_system.improved_solution.models.SeatStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SeatLockProvider {
    private final Map<String, String> lockedSeats;
    private final ScheduledExecutorService scheduler;
    private final int lockTimeoutSeconds;

    public SeatLockProvider(int lockTimeoutSeconds) {
        this.lockedSeats = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.lockTimeoutSeconds = lockTimeoutSeconds;
    }

    public synchronized boolean lockSeats(List<Seat> seatsToLock, String userId) {
        for (Seat seat : seatsToLock) {
            if (lockedSeats.containsKey(seat.getSeatId())) {
                System.out.println("Seat " + seat.getSeatId() + " is already locked by another user.");
                return false;
            }
        }

        for (Seat seat : seatsToLock) {
            lockedSeats.put(seat.getSeatId(), userId);
            seat.setSeatStatus(SeatStatus.LOCKED);
            scheduler.schedule(() -> unlockSeat(seat.getSeatId(), userId), lockTimeoutSeconds, TimeUnit.SECONDS);
        }
        System.out.println("Successfully locked " + seatsToLock.size() + " seats for user " + userId);
        return true;
    }

    public synchronized void unlockSeat(String seatId, String userId) {
        if (userId.equalsIgnoreCase(lockedSeats.get(seatId))) {
            lockedSeats.remove(seatId);
            System.out.println("Seat " + seatId + " unlocked.");
        }
    }

    public synchronized boolean isSeatLocked(String seatId) {
        return lockedSeats.containsKey(seatId);
    }
}
