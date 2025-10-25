package org.lld.practice.design_library_management_system.improved_solution;

import org.lld.practice.design_library_management_system.improved_solution.models.Book;
import org.lld.practice.design_library_management_system.improved_solution.models.BookItem;
import org.lld.practice.design_library_management_system.improved_solution.models.Member;
import org.lld.practice.design_library_management_system.improved_solution.repository.BookCatalog;
import org.lld.practice.design_library_management_system.improved_solution.repository.MemberRegistry;
import org.lld.practice.design_library_management_system.improved_solution.services.LendingService;
import org.lld.practice.design_library_management_system.improved_solution.services.ReservationService;
import org.lld.practice.design_library_management_system.improved_solution.strategies.SearchStrategy;

import java.util.List;

/**
 * Facade class that provides a simplified interface to the library management system
 */
public class Library {
    private final BookCatalog bookCatalog;
    private final MemberRegistry memberRegistry;
    private final LendingService lendingService;
    private final ReservationService reservationService;

    public Library() {
        this.bookCatalog = new BookCatalog();
        this.memberRegistry = new MemberRegistry();
        this.lendingService = new LendingService(bookCatalog, memberRegistry);
        this.reservationService = new ReservationService(bookCatalog);
    }

    public void addBook(Book book, int copies) {
        bookCatalog.addBook(book, copies);
    }

    public void registerMember(Member member) {
        memberRegistry.addMember(member);
    }

    public boolean borrowBook(String memberId, String barcode) {
        return lendingService.borrowBook(memberId, barcode);
    }

    public boolean returnBook(String barcode) {
        boolean returned = lendingService.returnBook(barcode);
        if (returned) {
            reservationService.notifyNextInQueue(barcode);
        }
        return returned;
    }

    public List<Book> searchBooks(SearchStrategy strategy, String query) {
        return bookCatalog.search(strategy, query);
    }

    public boolean reserveBook(String memberId, String isbn) {
        return reservationService.reserveBook(memberId, isbn);
    }

    public BookCatalog getBookCatalog() {
        return bookCatalog;
    }

    public MemberRegistry getMemberRegistry() {
        return memberRegistry;
    }

    public LendingService getLendingService() {
        return lendingService;
    }
}

