package org.lld.practice.design_social_media_feed_system.improved_solution.strategies;

import org.lld.practice.design_social_media_feed_system.improved_solution.models.Post;

import java.util.List;

/**
 * Strategy interface for feed ranking algorithms.
 */
public interface FeedRankingStrategy {
    List<Post> rank(List<Post> posts);
}

