package org.lld.practice.design_url_shortener.improved_solution.models;

import java.time.LocalDateTime;

/**
 * Represents a short URL with metadata.
 */
public class ShortURL {
    private final String shortCode;
    private final String longUrl;
    private final LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private long clickCount;
    private final String userId;
    
    public ShortURL(String shortCode, String longUrl, String userId) {
        this(shortCode, longUrl, userId, null);
    }
    
    public ShortURL(String shortCode, String longUrl, String userId, LocalDateTime expiresAt) {
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
        this.clickCount = 0;
    }
    
    public String getShortCode() {
        return shortCode;
    }
    
    public String getLongUrl() {
        return longUrl;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    public long getClickCount() {
        return clickCount;
    }
    
    public void incrementClickCount() {
        this.clickCount++;
    }
    
    public String getUserId() {
        return userId;
    }
}

