package org.lld.practice.design_social_media_feed_system.improved_solution.models;

import java.util.List;

public class Feed {
    private final String userId;
    private final List<Post> posts;
    private final int page;
    private final int totalPages;

    public Feed(String userId, List<Post> posts, int page, int totalPages) {
        this.userId = userId;
        this.posts = posts;
        this.page = page;
        this.totalPages = totalPages;
    }

    public String getUserId() {
        return userId;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return String.format("Feed[userId=%s, posts=%d, page=%d/%d]",
                userId, posts.size(), page, totalPages);
    }
}

