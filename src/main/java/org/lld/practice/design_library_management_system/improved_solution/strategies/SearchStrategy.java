package org.lld.practice.design_library_management_system.improved_solution.strategies;

import org.lld.practice.design_library_management_system.improved_solution.models.Book;

import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}

