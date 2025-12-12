package org.lld.practice.design_url_shortener.improved_solution;

import org.lld.practice.design_url_shortener.improved_solution.repositories.InMemoryURLRepository;
import org.lld.practice.design_url_shortener.improved_solution.repositories.URLRepository;
import org.lld.practice.design_url_shortener.improved_solution.services.URLShortenerService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Demo of improved URL shortener system.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Improved URL Shortener System Demo ===\n");
        
        URLRepository repository = new InMemoryURLRepository();
        URLShortenerService service = new URLShortenerService(repository);
        
        System.out.println("1. Shortening URLs:");
        String longUrl1 = "https://www.example.com/very/long/url/path/with/many/segments";
        String shortUrl1 = service.shorten(longUrl1, "user123");
        System.out.println("Long URL: " + longUrl1);
        System.out.println("Short URL: " + shortUrl1);
        
        String longUrl2 = "https://www.another-example.com/another/very/long/url";
        String shortUrl2 = service.shorten(longUrl2, "user456");
        System.out.println("\nLong URL: " + longUrl2);
        System.out.println("Short URL: " + shortUrl2);
        
        System.out.println("\n2. Expanding short URLs:");
        Optional<String> expanded1 = service.expand(shortUrl1);
        expanded1.ifPresent(url -> System.out.println(shortUrl1 + " -> " + url));
        
        Optional<String> expanded2 = service.expand(shortUrl2);
        expanded2.ifPresent(url -> System.out.println(shortUrl2 + " -> " + url));
        
        System.out.println("\n3. Analytics:");
        Optional<Long> clicks1 = service.getClickCount(shortUrl1);
        clicks1.ifPresent(count -> System.out.println(shortUrl1 + " has been clicked " + count + " time(s)"));
        
        System.out.println("\n4. Shortening with expiration:");
        String shortUrl3 = service.shorten("https://www.temp.com", "user789", 
                                           LocalDateTime.now().plusDays(7));
        System.out.println("Short URL with 7-day expiration: " + shortUrl3);
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Base62 encoding for short, unique URLs");
        System.out.println("✓ Strategy pattern for different encoding approaches");
        System.out.println("✓ Repository pattern for storage abstraction");
        System.out.println("✓ Click analytics tracking");
        System.out.println("✓ Expiration support");
        System.out.println("✓ Thread-safe operations");
    }
}

