package org.lld.practice.design_leaderboard_system.improved_solution.models;

import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Represents a leaderboard with efficient ranking.
 * Uses TreeSet for O(log n) insertions and O(1) top N retrieval.
 */
public class Leaderboard {
    private final String leaderboardId;
    private final LeaderboardType type;
    private final TreeSet<PlayerScore> rankedScores; // Sorted by score
    private final Map<String, PlayerScore> playerScoreMap; // Fast lookup by playerId
    private final LocalDateTime createdAt;
    
    public Leaderboard(String leaderboardId, LeaderboardType type) {
        this.leaderboardId = leaderboardId;
        this.type = type;
        this.rankedScores = new TreeSet<>();
        this.playerScoreMap = new ConcurrentHashMap<>();
        this.createdAt = LocalDateTime.now();
    }
    
    public String getLeaderboardId() {
        return leaderboardId;
    }
    
    public LeaderboardType getType() {
        return type;
    }
    
    /**
     * Adds or updates a player's score.
     * O(log n) operation due to TreeSet.
     * 
     * @param playerId The player ID
     * @param score The score to add
     */
    public synchronized void addScore(String playerId, int score) {
        PlayerScore playerScore = playerScoreMap.get(playerId);
        
        if (playerScore != null) {
            // Remove old entry from TreeSet
            rankedScores.remove(playerScore);
            // Update score
            playerScore.addScore(score);
        } else {
            // Create new player score
            playerScore = new PlayerScore(playerId, score);
            playerScoreMap.put(playerId, playerScore);
        }
        
        // Add updated entry back to TreeSet
        rankedScores.add(playerScore);
    }
    
    /**
     * Gets top N players.
     * O(n) where n is the number of players to return.
     * 
     * @param n Number of top players to return
     * @return List of top N player scores
     */
    public java.util.List<PlayerScore> getTopPlayers(int n) {
        return rankedScores.stream()
                .limit(n)
                .toList();
    }
    
    /**
     * Gets a player's rank.
     * O(n) operation - could be optimized with additional data structure.
     * 
     * @param playerId The player ID
     * @return The player's rank (1-indexed), or -1 if not found
     */
    public int getRank(String playerId) {
        PlayerScore playerScore = playerScoreMap.get(playerId);
        if (playerScore == null) {
            return -1;
        }
        
        int rank = 1;
        for (PlayerScore score : rankedScores) {
            if (score.equals(playerScore)) {
                return rank;
            }
            rank++;
        }
        return -1;
    }
    
    /**
     * Gets a player's score.
     * 
     * @param playerId The player ID
     * @return The player's score, or null if not found
     */
    public PlayerScore getPlayerScore(String playerId) {
        return playerScoreMap.get(playerId);
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

