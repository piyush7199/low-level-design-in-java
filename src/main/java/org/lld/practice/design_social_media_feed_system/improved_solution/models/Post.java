package org.lld.practice.design_social_media_feed_system.improved_solution.models;

import java.time.LocalDateTime;

public class Post {
    private final String postId;
    private final String userId;
    private final String content;
    private final LocalDateTime createdAt;
    private int likes;
    private int comments;
    private int shares;

    public Post(String postId, String userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.likes = 0;
        this.comments = 0;
        this.shares = 0;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public int getComments() {
        return comments;
    }

    public void incrementComments() {
        this.comments++;
    }

    public int getShares() {
        return shares;
    }

    public void incrementShares() {
        this.shares++;
    }

    public double getEngagementScore() {
        return likes * 1.0 + comments * 2.0 + shares * 3.0;
    }

    @Override
    public String toString() {
        return String.format("Post[id=%s, user=%s, content=%s, likes=%d, time=%s]",
                postId, userId, content, likes, createdAt);
    }
}

