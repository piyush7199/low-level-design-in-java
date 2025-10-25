package org.lld.practice.design_library_management_system.improved_solution.services;

import org.lld.practice.design_library_management_system.improved_solution.models.*;
import org.lld.practice.design_library_management_system.improved_solution.repository.BookCatalog;
import org.lld.practice.design_library_management_system.improved_solution.repository.MemberRegistry;
import org.lld.practice.design_library_management_system.improved_solution.strategies.DailyFineCalculator;
import org.lld.practice.design_library_management_system.improved_solution.strategies.FineCalculator;

import java.time.LocalDate;
import java.util.*;

public class LendingService {
    private final BookCatalog bookCatalog;
    private final MemberRegistry memberRegistry;
    private final FineCalculator fineCalculator;
    private final Map<String, LendingRecord> activeLendings; // barcode -> record
    private final Map<String, List<LendingRecord>> memberLendings; // memberId -> records
    private int recordCounter = 1;

    public LendingService(BookCatalog bookCatalog, MemberRegistry memberRegistry) {
        this.bookCatalog = bookCatalog;
        this.memberRegistry = memberRegistry;
        this.fineCalculator = new DailyFineCalculator();
        this.activeLendings = new HashMap<>();
        this.memberLendings = new HashMap<>();
    }

    public synchronized boolean borrowBook(String memberId, String barcode) {
        Member member = memberRegistry.getMember(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return false;
        }

        if (!member.isActive()) {
            System.out.println("Member is not active.");
            return false;
        }

        List<LendingRecord> memberBooks = memberLendings.getOrDefault(memberId, new ArrayList<>());
        if (memberBooks.size() >= member.getMaxBooksAllowed()) {
            System.out.println("Member has reached borrowing limit (" + member.getMaxBooksAllowed() + " books).");
            return false;
        }

        BookItem bookItem = bookCatalog.getBookItem(barcode);
        if (bookItem == null) {
            System.out.println("Book item not found.");
            return false;
        }

        if (!bookItem.isAvailable()) {
            System.out.println("Book item is not available.");
            return false;
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(member.getMaxBorrowDays());
        
        String recordId = "LR" + String.format("%05d", recordCounter++);
        LendingRecord record = new LendingRecord(recordId, member, bookItem, borrowDate, dueDate);

        bookItem.setStatus(BookStatus.BORROWED);
        activeLendings.put(barcode, record);
        memberBooks.add(record);
        memberLendings.put(memberId, memberBooks);

        System.out.println("Book borrowed successfully!");
        System.out.println("Book: " + bookItem.getBook().getTitle());
        System.out.println("Due date: " + dueDate);
        return true;
    }

    public synchronized boolean returnBook(String barcode) {
        LendingRecord record = activeLendings.get(barcode);
        if (record == null) {
            System.out.println("No active lending record found for this book.");
            return false;
        }

        LocalDate returnDate = LocalDate.now();
        record.setReturnDate(returnDate);

        double fine = fineCalculator.calculateFine(record.getDueDate(), returnDate);
        if (fine > 0) {
            record.setFine(fine);
            record.getMember().addFine(fine);
            System.out.println("Book returned late. Fine: $" + fine);
            System.out.println("Total outstanding fines: $" + record.getMember().getTotalFines());
        } else {
            System.out.println("Book returned on time.");
        }

        record.getBookItem().setStatus(BookStatus.AVAILABLE);
        activeLendings.remove(barcode);

        List<LendingRecord> memberBooks = memberLendings.get(record.getMember().getMemberId());
        if (memberBooks != null) {
            memberBooks.remove(record);
        }

        System.out.println("Book returned successfully: " + record.getBookItem().getBook().getTitle());
        return true;
    }

    public List<LendingRecord> getMemberLendings(String memberId) {
        return new ArrayList<>(memberLendings.getOrDefault(memberId, new ArrayList<>()));
    }

    public List<LendingRecord> getOverdueBooks() {
        List<LendingRecord> overdue = new ArrayList<>();
        for (LendingRecord record : activeLendings.values()) {
            if (record.isOverdue()) {
                overdue.add(record);
            }
        }
        return overdue;
    }
}

