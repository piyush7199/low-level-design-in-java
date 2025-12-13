package org.lld.practice.design_social_media_feed_system.improved_solution.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private static UserService instance;
    private final ConcurrentHashMap<String, List<String>> followRelations = new ConcurrentHashMap<>();

    private UserService() {
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void follow(String userId, String followUserId) {
        followRelations.computeIfAbsent(userId, k -> new ArrayList<>()).add(followUserId);
        FeedService.getInstance().invalidateCache(userId);
    }

    public void unfollow(String userId, String followUserId) {
        List<String> followed = followRelations.get(userId);
        if (followed != null) {
            followed.remove(followUserId);
        }
        FeedService.getInstance().invalidateCache(userId);
    }

    public List<String> getFollowedUsers(String userId) {
        return new ArrayList<>(followRelations.getOrDefault(userId, new ArrayList<>()));
    }
}

