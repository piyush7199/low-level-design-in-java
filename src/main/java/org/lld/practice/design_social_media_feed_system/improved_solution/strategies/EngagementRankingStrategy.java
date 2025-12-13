package org.lld.practice.design_social_media_feed_system.improved_solution.strategies;

import org.lld.practice.design_social_media_feed_system.improved_solution.models.Post;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ranks posts by engagement score (likes, comments, shares).
 */
public class EngagementRankingStrategy implements FeedRankingStrategy {
    @Override
    public List<Post> rank(List<Post> posts) {
        return posts.stream()
                .sorted(Comparator.comparing(Post::getEngagementScore).reversed())
                .collect(Collectors.toList());
    }
}

