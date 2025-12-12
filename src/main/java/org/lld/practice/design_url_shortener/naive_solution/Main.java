package org.lld.practice.design_url_shortener.naive_solution;

/**
 * Demo of naive URL shortener implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive URL Shortener Demo ===\n");
        
        SimpleURLShortener shortener = new SimpleURLShortener();
        
        String longUrl1 = "https://www.example.com/very/long/url/path/with/many/segments";
        String shortUrl1 = shortener.shorten(longUrl1);
        System.out.println("Shortened: " + shortUrl1);
        
        String longUrl2 = "https://www.another-example.com/another/very/long/url";
        String shortUrl2 = shortener.shorten(longUrl2);
        System.out.println("Shortened: " + shortUrl2);
        
        System.out.println("\nExpanding:");
        System.out.println(shortUrl1 + " -> " + shortener.expand(shortUrl1));
        System.out.println(shortUrl2 + " -> " + shortener.expand(shortUrl2));
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- Counter doesn't scale across servers");
        System.out.println("- Predictable URLs (easy to guess)");
        System.out.println("- No persistence (data lost on restart)");
        System.out.println("- No expiration support");
        System.out.println("- No analytics");
    }
}

