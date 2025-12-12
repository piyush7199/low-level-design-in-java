package org.lld.practice.design_cache_management_system.improved_solution.cache;

import org.lld.practice.design_cache_management_system.improved_solution.models.CacheStats;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe cache wrapper using ReadWriteLock.
 * 
 * Decorates any Cache implementation to make it thread-safe.
 * Uses ReadWriteLock for better read concurrency:
 * - Multiple readers can access simultaneously
 * - Writers have exclusive access
 *
 * @param <K> Type of cache keys
 * @param <V> Type of cache values
 */
public class ThreadSafeCache<K, V> implements Cache<K, V> {
    
    private final Cache<K, V> delegate;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public ThreadSafeCache(Cache<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<V> get(K key) {
        lock.readLock().lock();
        try {
            return delegate.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            delegate.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<V> remove(K key) {
        lock.writeLock().lock();
        try {
            return delegate.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return delegate.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return delegate.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int capacity() {
        return delegate.capacity();
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return delegate.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            delegate.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public CacheStats getStats() {
        return delegate.getStats();
    }
}

