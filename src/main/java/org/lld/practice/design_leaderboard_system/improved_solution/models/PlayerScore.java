package org.lld.practice.design_leaderboard_system.improved_solution.models;

import java.time.LocalDateTime;

/**
 * Represents a player's score with metadata.
 */
public class PlayerScore implements Comparable<PlayerScore> {
    private final String playerId;
    private int score;
    private LocalDateTime lastUpdated;
    
    public PlayerScore(String playerId, int score) {
        this.playerId = playerId;
        this.score = score;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public int getScore() {
        return score;
    }
    
    public void addScore(int points) {
        this.score += points;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void setScore(int score) {
        this.score = score;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    @Override
    public int compareTo(PlayerScore other) {
        // Higher score first, then by playerId for consistency
        int scoreCompare = Integer.compare(other.score, this.score);
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return this.playerId.compareTo(other.playerId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerScore that = (PlayerScore) obj;
        return playerId.equals(that.playerId);
    }
    
    @Override
    public int hashCode() {
        return playerId.hashCode();
    }
}

