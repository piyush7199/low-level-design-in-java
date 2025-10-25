package org.lld.practice.design_library_management_system.improved_solution.models;

import java.time.LocalDate;

public class LendingRecord {
    private final String recordId;
    private final Member member;
    private final BookItem bookItem;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public LendingRecord(String recordId, Member member, BookItem bookItem, LocalDate borrowDate, LocalDate dueDate) {
        this.recordId = recordId;
        this.member = member;
        this.bookItem = bookItem;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.fine = 0.0;
    }

    public String getRecordId() {
        return recordId;
    }

    public Member getMember() {
        return member;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return "LendingRecord{" +
                "recordId='" + recordId + '\'' +
                ", member=" + member.getName() +
                ", book=" + bookItem.getBook().getTitle() +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", fine=" + fine +
                '}';
    }
}

