package org.lld.practice.design_social_media_feed_system.improved_solution.observers;

import org.lld.practice.design_social_media_feed_system.improved_solution.models.Post;

public interface FeedObserver {
    void onNewPost(Post post);
}

