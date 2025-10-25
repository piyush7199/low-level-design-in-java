package org.lld.practice.design_library_management_system.improved_solution.repository;

import org.lld.practice.design_library_management_system.improved_solution.models.Book;
import org.lld.practice.design_library_management_system.improved_solution.models.BookItem;
import org.lld.practice.design_library_management_system.improved_solution.strategies.SearchStrategy;

import java.util.*;

public class BookCatalog {
    private final Map<String, Book> booksByIsbn;
    private final Map<String, BookItem> bookItemsByBarcode;
    private final Map<String, List<BookItem>> itemsByIsbn;

    public BookCatalog() {
        this.booksByIsbn = new HashMap<>();
        this.bookItemsByBarcode = new HashMap<>();
        this.itemsByIsbn = new HashMap<>();
    }

    public void addBook(Book book, int copies) {
        booksByIsbn.put(book.getIsbn(), book);
        
        List<BookItem> items = itemsByIsbn.getOrDefault(book.getIsbn(), new ArrayList<>());
        for (int i = 0; i < copies; i++) {
            String barcode = generateBarcode(book.getIsbn(), items.size() + i + 1);
            BookItem item = new BookItem(barcode, book);
            items.add(item);
            bookItemsByBarcode.put(barcode, item);
        }
        itemsByIsbn.put(book.getIsbn(), items);
        
        System.out.println("Added " + copies + " copies of: " + book.getTitle());
    }

    public BookItem getBookItem(String barcode) {
        return bookItemsByBarcode.get(barcode);
    }

    public BookItem findAvailableBookItem(String isbn) {
        List<BookItem> items = itemsByIsbn.get(isbn);
        if (items == null) return null;
        
        return items.stream()
                .filter(BookItem::isAvailable)
                .findFirst()
                .orElse(null);
    }

    public List<Book> search(SearchStrategy strategy, String query) {
        return strategy.search(new ArrayList<>(booksByIsbn.values()), query);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(booksByIsbn.values());
    }

    private String generateBarcode(String isbn, int copyNumber) {
        return isbn + "-" + String.format("%03d", copyNumber);
    }

    public int getAvailableCopies(String isbn) {
        List<BookItem> items = itemsByIsbn.get(isbn);
        if (items == null) return 0;
        return (int) items.stream().filter(BookItem::isAvailable).count();
    }
}

