package org.lld.practice.design_social_media_feed_system.improved_solution.services;

import org.lld.practice.design_social_media_feed_system.improved_solution.models.Post;
import org.lld.practice.design_social_media_feed_system.improved_solution.strategies.FeedRankingStrategy;
import org.lld.practice.design_social_media_feed_system.improved_solution.strategies.RecencyRankingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton service managing feed generation.
 */
public class FeedService {
    private static FeedService instance;
    private final PostService postService;
    private final UserService userService;
    private FeedRankingStrategy rankingStrategy;
    private final ConcurrentHashMap<String, List<Post>> feedCache = new ConcurrentHashMap<>();

    private FeedService() {
        this.postService = PostService.getInstance();
        this.userService = UserService.getInstance();
        this.rankingStrategy = new RecencyRankingStrategy(); // Default strategy
    }

    public static synchronized FeedService getInstance() {
        if (instance == null) {
            instance = new FeedService();
        }
        return instance;
    }

    public void setRankingStrategy(FeedRankingStrategy strategy) {
        this.rankingStrategy = strategy;
    }

    public List<Post> getFeed(String userId, int page, int pageSize) {
        // Check cache first
        String cacheKey = userId + "_" + page;
        if (feedCache.containsKey(cacheKey)) {
            return feedCache.get(cacheKey);
        }

        // Get followed users
        List<String> followedUsers = userService.getFollowedUsers(userId);
        followedUsers.add(userId); // Include own posts

        // Collect posts from followed users
        List<Post> allPosts = new ArrayList<>();
        for (String followedUserId : followedUsers) {
            allPosts.addAll(postService.getUserPosts(followedUserId));
        }

        // Rank posts
        List<Post> rankedPosts = rankingStrategy.rank(allPosts);

        // Paginate
        int start = page * pageSize;
        int end = Math.min(start + pageSize, rankedPosts.size());
        List<Post> feed = rankedPosts.subList(start, end);

        // Cache result
        feedCache.put(cacheKey, feed);

        return feed;
    }

    public void invalidateCache(String userId) {
        feedCache.entrySet().removeIf(entry -> entry.getKey().startsWith(userId + "_"));
    }
}

