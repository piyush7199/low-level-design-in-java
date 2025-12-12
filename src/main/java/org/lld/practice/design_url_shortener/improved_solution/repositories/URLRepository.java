package org.lld.practice.design_url_shortener.improved_solution.repositories;

import org.lld.practice.design_url_shortener.improved_solution.models.ShortURL;

import java.util.Optional;

/**
 * Repository interface for URL storage operations.
 * Abstracts data access layer.
 */
public interface URLRepository {
    /**
     * Saves a short URL.
     * 
     * @param shortURL The short URL to save
     */
    void save(ShortURL shortURL);
    
    /**
     * Finds a short URL by its short code.
     * 
     * @param shortCode The short code
     * @return Optional containing the ShortURL if found
     */
    Optional<ShortURL> findByShortCode(String shortCode);
    
    /**
     * Checks if a short code already exists.
     * 
     * @param shortCode The short code to check
     * @return true if exists, false otherwise
     */
    boolean exists(String shortCode);
}

