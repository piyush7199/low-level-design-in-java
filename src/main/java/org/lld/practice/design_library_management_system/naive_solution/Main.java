package org.lld.practice.design_library_management_system.naive_solution;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        
        // Add books
        library.addBook(new Book("ISBN001", "Clean Code", "Robert Martin"));
        library.addBook(new Book("ISBN002", "Design Patterns", "Gang of Four"));
        library.addBook(new Book("ISBN003", "Effective Java", "Joshua Bloch"));
        
        // Register members
        library.registerMember(new Member("M001", "Alice", "alice@example.com"));
        library.registerMember(new Member("M002", "Bob", "bob@example.com"));
        
        System.out.println("\n=== Scenario 1: Borrow Book ===");
        library.borrowBook("M001", "ISBN001");
        
        System.out.println("\n=== Scenario 2: Try to borrow unavailable book ===");
        library.borrowBook("M002", "ISBN001");
        
        System.out.println("\n=== Scenario 3: Return Book ===");
        library.returnBook("M001", "ISBN001");
        
        System.out.println("\n=== Scenario 4: Search Books ===");
        System.out.println("Search by title 'Java': " + library.searchByTitle("Java"));
        System.out.println("Search by author 'Martin': " + library.searchByAuthor("Martin"));
    }
}

