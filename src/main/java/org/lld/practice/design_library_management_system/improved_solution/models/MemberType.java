package org.lld.practice.design_library_management_system.improved_solution.models;

public enum MemberType {
    STUDENT(5, 14),
    FACULTY(10, 30),
    GUEST(2, 7);

    private final int maxBooks;
    private final int maxDays;

    MemberType(int maxBooks, int maxDays) {
        this.maxBooks = maxBooks;
        this.maxDays = maxDays;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public int getMaxDays() {
        return maxDays;
    }
}

