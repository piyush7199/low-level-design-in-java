package org.lld.practice.design_library_management_system.improved_solution;

import org.lld.practice.design_library_management_system.improved_solution.models.*;
import org.lld.practice.design_library_management_system.improved_solution.strategies.AuthorSearchStrategy;
import org.lld.practice.design_library_management_system.improved_solution.strategies.TitleSearchStrategy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        // Add books to the library
        System.out.println("=== Adding Books to Library ===\n");
        Book book1 = new Book("ISBN001", "Clean Code", "Robert Martin", "Prentice Hall", "Programming");
        Book book2 = new Book("ISBN002", "Design Patterns", "Gang of Four", "Addison-Wesley", "Software Engineering");
        Book book3 = new Book("ISBN003", "Effective Java", "Joshua Bloch", "Addison-Wesley", "Programming");

        library.addBook(book1, 2);
        library.addBook(book2, 1);
        library.addBook(book3, 3);

        // Register members
        System.out.println("\n=== Registering Members ===\n");
        Member student = new Member("M001", "Alice Smith", "alice@university.edu", MemberType.STUDENT);
        Member faculty = new Member("M002", "Dr. Bob Johnson", "bob@university.edu", MemberType.FACULTY);
        Member guest = new Member("M003", "Charlie Brown", "charlie@example.com", MemberType.GUEST);

        library.registerMember(student);
        library.registerMember(faculty);
        library.registerMember(guest);

        // Scenario 1: Borrow books
        System.out.println("\n=== Scenario 1: Borrowing Books ===\n");
        library.borrowBook("M001", "ISBN001-001");
        library.borrowBook("M002", "ISBN002-001");
        library.borrowBook("M003", "ISBN001-002");

        // Scenario 2: Try to borrow unavailable book
        System.out.println("\n=== Scenario 2: Trying to Borrow Unavailable Book ===\n");
        library.borrowBook("M001", "ISBN002-001");

        // Scenario 3: Reserve a book
        System.out.println("\n=== Scenario 3: Reserving a Book ===\n");
        library.reserveBook("M001", "ISBN002");

        // Scenario 4: Return a book
        System.out.println("\n=== Scenario 4: Returning Books ===\n");
        library.returnBook("ISBN002-001");

        // Scenario 5: Search for books
        System.out.println("\n=== Scenario 5: Searching for Books ===\n");
        List<Book> searchByTitle = library.searchBooks(new TitleSearchStrategy(), "Java");
        System.out.println("Search by title 'Java': " + searchByTitle);

        List<Book> searchByAuthor = library.searchBooks(new AuthorSearchStrategy(), "Martin");
        System.out.println("Search by author 'Martin': " + searchByAuthor);

        // Scenario 6: Try to exceed borrowing limit
        System.out.println("\n=== Scenario 6: Testing Borrowing Limits ===\n");
        System.out.println("Guest limit: " + guest.getMaxBooksAllowed() + " books");
        library.borrowBook("M003", "ISBN003-001");
        library.borrowBook("M003", "ISBN003-002"); // Should fail - guest limit is 2

        // Display member borrowing status
        System.out.println("\n=== Current Borrowing Status ===\n");
        System.out.println("Student (Alice) current loans: " + 
            library.getLendingService().getMemberLendings("M001").size());
        System.out.println("Faculty (Bob) current loans: " + 
            library.getLendingService().getMemberLendings("M002").size());
        System.out.println("Guest (Charlie) current loans: " + 
            library.getLendingService().getMemberLendings("M003").size());
    }
}

