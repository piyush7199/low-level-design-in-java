package org.lld.practice.design_library_management_system.improved_solution.services;

import org.lld.practice.design_library_management_system.improved_solution.repository.BookCatalog;

import java.util.*;

public class ReservationService {
    private final BookCatalog bookCatalog;
    private final Map<String, Queue<String>> reservationQueues; // isbn -> queue of memberIds

    public ReservationService(BookCatalog bookCatalog) {
        this.bookCatalog = bookCatalog;
        this.reservationQueues = new HashMap<>();
    }

    public boolean reserveBook(String memberId, String isbn) {
        if (bookCatalog.getAvailableCopies(isbn) > 0) {
            System.out.println("Book is available. Please borrow it directly.");
            return false;
        }

        Queue<String> queue = reservationQueues.getOrDefault(isbn, new LinkedList<>());
        if (queue.contains(memberId)) {
            System.out.println("You have already reserved this book.");
            return false;
        }

        queue.offer(memberId);
        reservationQueues.put(isbn, queue);
        System.out.println("Book reserved successfully. Position in queue: " + queue.size());
        return true;
    }

    public void notifyNextInQueue(String barcode) {
        // Extract ISBN from barcode (format: ISBN-XXX)
        String isbn = barcode.substring(0, barcode.lastIndexOf("-"));
        
        Queue<String> queue = reservationQueues.get(isbn);
        if (queue != null && !queue.isEmpty()) {
            String nextMemberId = queue.poll();
            System.out.println("[Notification] Member " + nextMemberId + ": Your reserved book is now available!");
        }
    }
}

