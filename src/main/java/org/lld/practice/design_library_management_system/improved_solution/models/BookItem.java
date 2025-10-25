package org.lld.practice.design_library_management_system.improved_solution.models;

public class BookItem {
    private final String barcode;
    private final Book book;
    private BookStatus status;

    public BookItem(String barcode, Book book) {
        this.barcode = barcode;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }

    public String getBarcode() {
        return barcode;
    }

    public Book getBook() {
        return book;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "BookItem{" +
                "barcode='" + barcode + '\'' +
                ", book=" + book.getTitle() +
                ", status=" + status +
                '}';
    }
}

