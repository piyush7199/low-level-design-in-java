package org.lld.practice.design_social_media_feed_system.improved_solution.services;

import org.lld.practice.design_social_media_feed_system.improved_solution.models.Post;
import org.lld.practice.design_social_media_feed_system.improved_solution.observers.FeedObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PostService {
    private static PostService instance;
    private final ConcurrentHashMap<String, List<Post>> userPosts = new ConcurrentHashMap<>();
    private final List<FeedObserver> observers = new ArrayList<>();

    private PostService() {
    }

    public static synchronized PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }
        return instance;
    }

    public Post createPost(String userId, String content) {
        String postId = "P" + UUID.randomUUID().toString().substring(0, 8);
        Post post = new Post(postId, userId, content);
        userPosts.computeIfAbsent(userId, k -> new ArrayList<>()).add(post);

        // Notify observers
        notifyObservers(post);

        return post;
    }

    public List<Post> getUserPosts(String userId) {
        return new ArrayList<>(userPosts.getOrDefault(userId, new ArrayList<>()));
    }

    public void addObserver(FeedObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(Post post) {
        for (FeedObserver observer : observers) {
            observer.onNewPost(post);
        }
    }
}

