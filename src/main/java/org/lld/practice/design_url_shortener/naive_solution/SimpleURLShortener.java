package org.lld.practice.design_url_shortener.naive_solution;

import java.util.HashMap;
import java.util.Map;

/**
 * Naive implementation of URL shortener.
 * 
 * This demonstrates common pitfalls:
 * - Simple counter approach (doesn't scale)
 * - No persistence
 * - Predictable URLs
 * - No expiration
 * - No analytics
 */
public class SimpleURLShortener {
    private int counter = 0;
    private final Map<String, String> urlMap = new HashMap<>();
    
    public String shorten(String longUrl) {
        counter++;
        String shortUrl = "http://short.ly/" + counter;
        urlMap.put(shortUrl, longUrl);
        return shortUrl;
    }
    
    public String expand(String shortUrl) {
        return urlMap.get(shortUrl);
    }
}

