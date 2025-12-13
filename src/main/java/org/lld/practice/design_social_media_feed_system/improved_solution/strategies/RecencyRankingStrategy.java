package org.lld.practice.design_social_media_feed_system.improved_solution.strategies;

import org.lld.practice.design_social_media_feed_system.improved_solution.models.Post;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ranks posts by recency (newest first).
 */
public class RecencyRankingStrategy implements FeedRankingStrategy {
    @Override
    public List<Post> rank(List<Post> posts) {
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}

