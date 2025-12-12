# Design A URL Shortener System (TinyURL)

## 1. Problem Statement and Requirements

Our goal is to design a URL shortening service like TinyURL or bit.ly that converts long URLs into short, shareable links.

### Functional Requirements:

- **URL Shortening**: Convert long URLs to short, unique aliases
- **URL Redirection**: Redirect short URLs to original long URLs
- **Custom Aliases**: Allow users to create custom short URLs (optional)
- **Expiration**: Support optional expiration dates for short URLs
- **Analytics**: Track click counts and access statistics
- **User Management**: Support user accounts with URL history
- **Bulk Operations**: Generate multiple short URLs at once

### Non-Functional Requirements:

- **Performance**: Fast URL redirection (low latency)
- **Scalability**: Handle billions of URLs and millions of requests per second
- **Availability**: High availability for redirection service
- **Uniqueness**: Ensure short URLs are unique
- **Security**: Prevent abuse and malicious URLs

---

## 2. Naive Solution: The "Starting Point"

A beginner might use a simple counter or random string generation.

### The Thought Process:

"I'll use a counter to generate short URLs. Each new URL gets the next number."

```java
class URLShortener {
    private int counter = 0;
    private Map<String, String> urlMap = new HashMap<>();
    
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
```

### Limitations and Design Flaws:

1. **Scalability Issues**: Counter approach doesn't scale across multiple servers
2. **No Persistence**: Data lost on restart
3. **Predictable URLs**: Sequential numbers are easy to guess
4. **No Expiration**: URLs never expire
5. **No Analytics**: No tracking of clicks
6. **Collision Risk**: Random string generation may have collisions

---

## 3. Improved Solution: The "Mentor's Guidance"

Use base62 encoding for short URLs, implement proper hashing, and separate encoding strategy from storage.

### Design Patterns Used:

1. **Strategy Pattern**: For different encoding strategies (Base62, Hash-based, Random)
2. **Factory Pattern**: For creating different types of URL generators
3. **Repository Pattern**: For abstracting data storage
4. **Singleton Pattern**: For URL service instance

### Core Classes and Interactions:

1. **URLShortenerService**: Main service orchestrating URL operations
2. **EncodingStrategy**: Interface for different encoding approaches
3. **Base62Encoder**: Converts numbers to base62 strings
4. **URLRepository**: Abstracts data storage operations
5. **URLAnalytics**: Tracks click counts and statistics

---

## 4. Final Design Overview

The improved solution uses Strategy pattern for encoding, Repository pattern for storage abstraction, and proper hashing to create a scalable, efficient URL shortening system.

