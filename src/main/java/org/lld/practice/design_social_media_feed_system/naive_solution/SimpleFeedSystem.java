package org.lld.practice.design_social_media_feed_system.naive_solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Naive implementation showing common pitfalls:
 * - O(n*m) complexity
 * - No ranking algorithm
 * - No caching
 * - No pagination
 */
public class SimpleFeedSystem {
    private final Map<String, List<String>> followers = new HashMap<>(); // userId -> list of followed users
    private final Map<String, List<Post>> userPosts = new HashMap<>();   // userId -> list of posts

    public void follow(String userId, String followUserId) {
        followers.computeIfAbsent(userId, k -> new ArrayList<>()).add(followUserId);
    }

    public void createPost(String userId, String content) {
        Post post = new Post(userId, content);
        userPosts.computeIfAbsent(userId, k -> new ArrayList<>()).add(post);
    }

    public List<Post> getFeed(String userId) {
        List<Post> feed = new ArrayList<>();
        List<String> followedUsers = followers.getOrDefault(userId, new ArrayList<>());

        // O(n*m) complexity - very inefficient!
        for (String followedUserId : followedUsers) {
            feed.addAll(userPosts.getOrDefault(followedUserId, new ArrayList<>()));
        }

        // Simple sort by timestamp (no ranking algorithm)
        feed.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        return feed;
    }
}

