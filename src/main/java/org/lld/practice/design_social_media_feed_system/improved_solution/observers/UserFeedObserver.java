package org.lld.practice.design_social_media_feed_system.improved_solution.observers;

import org.lld.practice.design_social_media_feed_system.improved_solution.models.Post;
import org.lld.practice.design_social_media_feed_system.improved_solution.services.FeedService;

/**
 * Updates user feeds when new posts arrive from followed users.
 */
public class UserFeedObserver implements FeedObserver {
    @Override
    public void onNewPost(Post post) {
        // Invalidate cache for all users following this post's author
        // In production, would maintain reverse index: userId -> list of followers
        FeedService.getInstance().invalidateCache("*"); // Simplified: invalidate all
        System.out.println("Feed cache invalidated due to new post from " + post.getUserId());
    }
}

