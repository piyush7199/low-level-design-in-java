package org.lld.practice.design_social_media_feed_system.improved_solution;

import org.lld.practice.design_social_media_feed_system.improved_solution.observers.UserFeedObserver;
import org.lld.practice.design_social_media_feed_system.improved_solution.services.FeedService;
import org.lld.practice.design_social_media_feed_system.improved_solution.services.PostService;
import org.lld.practice.design_social_media_feed_system.improved_solution.services.UserService;
import org.lld.practice.design_social_media_feed_system.improved_solution.strategies.EngagementRankingStrategy;
import org.lld.practice.design_social_media_feed_system.improved_solution.strategies.RecencyRankingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Social Media Feed System Demo ===\n");

        FeedService feedService = FeedService.getInstance();
        PostService postService = PostService.getInstance();
        UserService userService = UserService.getInstance();

        // Add observer
        postService.addObserver(new UserFeedObserver());

        // Create posts
        System.out.println("1. Creating posts:");
        postService.createPost("user1", "Hello world!");
        postService.createPost("user2", "Great day today!");
        postService.createPost("user1", "Another post from user1");

        // Set up follows
        System.out.println("\n2. Setting up follows:");
        userService.follow("user3", "user1");
        userService.follow("user3", "user2");

        // Get feed with recency ranking
        System.out.println("\n3. Feed for user3 (Recency Ranking):");
        feedService.setRankingStrategy(new RecencyRankingStrategy());
        feedService.getFeed("user3", 0, 10).forEach(System.out::println);

        // Get feed with engagement ranking
        System.out.println("\n4. Feed for user3 (Engagement Ranking):");
        feedService.setRankingStrategy(new EngagementRankingStrategy());
        feedService.getFeed("user3", 0, 10).forEach(System.out::println);

        System.out.println("\n5. Adding engagement to posts:");
        postService.getUserPosts("user1").get(0).incrementLikes();
        postService.getUserPosts("user1").get(0).incrementComments();

        System.out.println("\n6. Feed after engagement (Engagement Ranking):");
        feedService.invalidateCache("user3");
        feedService.getFeed("user3", 0, 10).forEach(System.out::println);
    }
}

