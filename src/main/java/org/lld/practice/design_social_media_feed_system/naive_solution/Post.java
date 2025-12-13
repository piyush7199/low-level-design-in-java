package org.lld.practice.design_social_media_feed_system.naive_solution;

public class Post {
    private String userId;
    private String content;
    private long timestamp;

    public Post(String userId, String content) {
        this.userId = userId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Post[user=%s, content=%s, time=%d]", userId, content, timestamp);
    }
}

