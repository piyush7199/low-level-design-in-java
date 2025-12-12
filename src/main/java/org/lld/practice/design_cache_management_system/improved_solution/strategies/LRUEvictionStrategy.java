package org.lld.practice.design_cache_management_system.improved_solution.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Least Recently Used (LRU) eviction strategy.
 * 
 * Uses a HashMap + Doubly Linked List for O(1) operations:
 * - HashMap: O(1) lookup of nodes by key
 * - Doubly Linked List: O(1) removal and insertion
 * 
 * Most recently accessed items are at the head.
 * Least recently accessed items are at the tail (eviction candidates).
 *
 * @param <K> Type of cache keys
 */
public class LRUEvictionStrategy<K> implements EvictionStrategy<K> {
    
    private final Map<K, Node<K>> nodeMap = new HashMap<>();
    private final Node<K> head;  // Dummy head (most recent side)
    private final Node<K> tail;  // Dummy tail (least recent side)

    public LRUEvictionStrategy() {
        // Initialize with dummy head and tail nodes
        head = new Node<>(null);
        tail = new Node<>(null);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void recordAccess(K key) {
        Node<K> node = nodeMap.get(key);
        if (node != null) {
            // Move to head (most recently used)
            removeNode(node);
            addToHead(node);
        }
    }

    @Override
    public void recordInsertion(K key) {
        if (nodeMap.containsKey(key)) {
            // Key already exists, just update access
            recordAccess(key);
            return;
        }
        
        Node<K> node = new Node<>(key);
        nodeMap.put(key, node);
        addToHead(node);
    }

    @Override
    public Optional<K> getEvictionCandidate() {
        if (nodeMap.isEmpty()) {
            return Optional.empty();
        }
        // Return the key at tail (least recently used)
        return Optional.ofNullable(tail.prev.key);
    }

    @Override
    public void remove(K key) {
        Node<K> node = nodeMap.remove(key);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public void clear() {
        nodeMap.clear();
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public int size() {
        return nodeMap.size();
    }

    /**
     * Add node right after head (most recent position).
     */
    private void addToHead(Node<K> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    /**
     * Remove node from its current position.
     */
    private void removeNode(Node<K> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * Doubly linked list node.
     */
    private static class Node<K> {
        K key;
        Node<K> prev;
        Node<K> next;

        Node(K key) {
            this.key = key;
        }
    }
}

