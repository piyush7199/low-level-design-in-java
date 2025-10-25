package org.lld.practice.design_library_management_system.naive_solution;

import java.time.LocalDate;
import java.util.*;

public class Library {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private Map<String, List<Book>> borrowedBooks = new HashMap<>();
    
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
    }
    
    public void registerMember(Member member) {
        members.add(member);
        System.out.println("Member registered: " + member.getName());
    }
    
    public boolean borrowBook(String memberId, String isbn) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return false;
        }
        
        if (!member.isActive()) {
            System.out.println("Member is not active.");
            return false;
        }
        
        List<Book> memberBooks = borrowedBooks.getOrDefault(memberId, new ArrayList<>());
        if (memberBooks.size() >= 5) {
            System.out.println("Member has reached borrowing limit.");
            return false;
        }
        
        Book book = findAvailableBook(isbn);
        if (book == null) {
            System.out.println("Book not available.");
            return false;
        }
        
        book.setAvailable(false);
        book.setBorrowDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(14));
        memberBooks.add(book);
        borrowedBooks.put(memberId, memberBooks);
        
        System.out.println("Book borrowed successfully: " + book.getTitle());
        System.out.println("Due date: " + book.getDueDate());
        return true;
    }
    
    public boolean returnBook(String memberId, String isbn) {
        List<Book> memberBooks = borrowedBooks.get(memberId);
        if (memberBooks == null || memberBooks.isEmpty()) {
            System.out.println("No borrowed books found for this member.");
            return false;
        }
        
        Book book = null;
        for (Book b : memberBooks) {
            if (b.getIsbn().equals(isbn)) {
                book = b;
                break;
            }
        }
        
        if (book == null) {
            System.out.println("This book was not borrowed by this member.");
            return false;
        }
        
        LocalDate returnDate = LocalDate.now();
        long daysLate = returnDate.toEpochDay() - book.getDueDate().toEpochDay();
        
        if (daysLate > 0) {
            double fine = daysLate * 1.0; // $1 per day
            System.out.println("Book is " + daysLate + " days late. Fine: $" + fine);
        }
        
        book.setAvailable(true);
        book.setBorrowDate(null);
        book.setDueDate(null);
        memberBooks.remove(book);
        
        System.out.println("Book returned successfully: " + book.getTitle());
        return true;
    }
    
    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }
    
    public List<Book> searchByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }
    
    private Member findMember(String memberId) {
        for (Member member : members) {
            if (member.getMemberId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }
    
    private Book findAvailableBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn) && book.isAvailable()) {
                return book;
            }
        }
        return null;
    }
}

