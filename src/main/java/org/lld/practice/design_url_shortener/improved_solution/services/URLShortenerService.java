package org.lld.practice.design_url_shortener.improved_solution.services;

import org.lld.practice.design_url_shortener.improved_solution.models.ShortURL;
import org.lld.practice.design_url_shortener.improved_solution.repositories.URLRepository;
import org.lld.practice.design_url_shortener.improved_solution.strategies.Base62Encoder;
import org.lld.practice.design_url_shortener.improved_solution.strategies.EncodingStrategy;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for URL shortening operations.
 */
public class URLShortenerService {
    private final URLRepository repository;
    private final EncodingStrategy encoder;
    private static final String BASE_URL = "http://short.ly/";
    
    public URLShortenerService(URLRepository repository) {
        this.repository = repository;
        this.encoder = new Base62Encoder();
    }
    
    public URLShortenerService(URLRepository repository, EncodingStrategy encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }
    
    /**
     * Shortens a long URL.
     * 
     * @param longUrl The long URL to shorten
     * @param userId The user ID (optional)
     * @return The short URL
     */
    public String shorten(String longUrl, String userId) {
        return shorten(longUrl, userId, null);
    }
    
    /**
     * Shortens a long URL with optional expiration.
     * 
     * @param longUrl The long URL to shorten
     * @param userId The user ID
     * @param expiresAt Optional expiration date
     * @return The short URL
     */
    public String shorten(String longUrl, String userId, LocalDateTime expiresAt) {
        // Generate unique short code
        String shortCode = generateUniqueShortCode();
        
        ShortURL shortURL = new ShortURL(shortCode, longUrl, userId, expiresAt);
        repository.save(shortURL);
        
        return BASE_URL + shortCode;
    }
    
    /**
     * Expands a short URL to the original long URL.
     * 
     * @param shortUrl The short URL
     * @return Optional containing the long URL if found and not expired
     */
    public Optional<String> expand(String shortUrl) {
        String shortCode = extractShortCode(shortUrl);
        
        Optional<ShortURL> shortURL = repository.findByShortCode(shortCode);
        
        if (shortURL.isEmpty()) {
            return Optional.empty();
        }
        
        ShortURL url = shortURL.get();
        
        if (url.isExpired()) {
            return Optional.empty();
        }
        
        // Increment click count
        url.incrementClickCount();
        repository.save(url);
        
        return Optional.of(url.getLongUrl());
    }
    
    /**
     * Gets analytics for a short URL.
     * 
     * @param shortUrl The short URL
     * @return Optional containing click count if found
     */
    public Optional<Long> getClickCount(String shortUrl) {
        String shortCode = extractShortCode(shortUrl);
        Optional<ShortURL> shortURL = repository.findByShortCode(shortCode);
        return shortURL.map(ShortURL::getClickCount);
    }
    
    private String generateUniqueShortCode() {
        String shortCode;
        int attempts = 0;
        do {
            shortCode = encoder.generateUniqueCode();
            attempts++;
            if (attempts > 10) {
                // Fallback to hash-based encoding
                shortCode = encoder.encode(String.valueOf(System.currentTimeMillis()));
            }
        } while (repository.exists(shortCode) && attempts < 20);
        
        return shortCode;
    }
    
    private String extractShortCode(String shortUrl) {
        if (shortUrl.startsWith(BASE_URL)) {
            return shortUrl.substring(BASE_URL.length());
        }
        return shortUrl;
    }
}

