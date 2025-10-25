package org.lld.practice.design_library_management_system.improved_solution.strategies;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DailyFineCalculator implements FineCalculator {
    private static final double FINE_PER_DAY = 1.0;

    @Override
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isBefore(dueDate) || returnDate.isEqual(dueDate)) {
            return 0.0;
        }
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        return daysLate * FINE_PER_DAY;
    }
}

