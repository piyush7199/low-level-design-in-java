package org.lld.practice.design_url_shortener.improved_solution.repositories;

import org.lld.practice.design_url_shortener.improved_solution.models.ShortURL;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of URL repository.
 * In production, this would be replaced with a database implementation.
 */
public class InMemoryURLRepository implements URLRepository {
    private final Map<String, ShortURL> urlStore = new ConcurrentHashMap<>();
    
    @Override
    public void save(ShortURL shortURL) {
        urlStore.put(shortURL.getShortCode(), shortURL);
    }
    
    @Override
    public Optional<ShortURL> findByShortCode(String shortCode) {
        return Optional.ofNullable(urlStore.get(shortCode));
    }
    
    @Override
    public boolean exists(String shortCode) {
        return urlStore.containsKey(shortCode);
    }
}

