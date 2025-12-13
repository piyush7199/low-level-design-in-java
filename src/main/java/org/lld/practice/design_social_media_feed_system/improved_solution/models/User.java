package org.lld.practice.design_social_media_feed_system.improved_solution.models;

public class User {
    private final String userId;
    private final String username;
    private final String email;

    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, username=%s]", userId, username);
    }
}

