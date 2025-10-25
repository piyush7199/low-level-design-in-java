package org.lld.practice.design_library_management_system.improved_solution.strategies;

import org.lld.practice.design_library_management_system.improved_solution.models.Book;

import java.util.ArrayList;
import java.util.List;

public class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        }
        return results;
    }
}

