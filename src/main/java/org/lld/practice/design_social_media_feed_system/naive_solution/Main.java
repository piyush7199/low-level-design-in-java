package org.lld.practice.design_social_media_feed_system.naive_solution;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Social Media Feed System ===\n");

        SimpleFeedSystem system = new SimpleFeedSystem();

        system.createPost("user1", "Hello world!");
        system.createPost("user2", "Great day today!");
        system.follow("user3", "user1");
        system.follow("user3", "user2");

        System.out.println("Feed for user3:");
        system.getFeed("user3").forEach(System.out::println);

        System.out.println("\nLimitations:");
        System.out.println("- O(n*m) complexity");
        System.out.println("- No ranking algorithm");
        System.out.println("- No caching");
        System.out.println("- No pagination");
    }
}

