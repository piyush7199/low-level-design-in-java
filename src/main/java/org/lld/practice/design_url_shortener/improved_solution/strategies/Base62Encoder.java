package org.lld.practice.design_url_shortener.improved_solution.strategies;

import java.util.Random;

/**
 * Base62 encoding strategy for generating short URLs.
 * Uses characters: 0-9, a-z, A-Z (62 characters total).
 */
public class Base62Encoder implements EncodingStrategy {
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORT_CODE_LENGTH = 7;
    private final Random random = new Random();
    
    @Override
    public String encode(String input) {
        // Simple hash-based encoding
        long hash = input.hashCode();
        if (hash < 0) {
            hash = -hash;
        }
        return encodeNumber(hash);
    }
    
    @Override
    public String generateUniqueCode() {
        // Generate random base62 string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(BASE62_CHARS.charAt(random.nextInt(BASE62_CHARS.length())));
        }
        return sb.toString();
    }
    
    private String encodeNumber(long number) {
        if (number == 0) {
            return String.valueOf(BASE62_CHARS.charAt(0));
        }
        
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            sb.append(BASE62_CHARS.charAt((int) (number % 62)));
            number /= 62;
        }
        return sb.reverse().toString();
    }
}

