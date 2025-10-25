package org.lld.practice.design_library_management_system.improved_solution.strategies;

import java.time.LocalDate;

public interface FineCalculator {
    double calculateFine(LocalDate dueDate, LocalDate returnDate);
}

