package org.lld.practice.design_url_shortener.improved_solution.strategies;

/**
 * Strategy interface for encoding URLs.
 * Different implementations can use base62, hash-based, or random approaches.
 */
public interface EncodingStrategy {
    /**
     * Encodes a number or string into a short code.
     * 
     * @param input The input to encode
     * @return The encoded short code
     */
    String encode(String input);
    
    /**
     * Generates a unique short code.
     * 
     * @return A unique short code
     */
    String generateUniqueCode();
}

