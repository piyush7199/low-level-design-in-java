package org.lld.practice.design_leaderboard_system.improved_solution.services;

import org.lld.practice.design_leaderboard_system.improved_solution.models.Leaderboard;
import org.lld.practice.design_leaderboard_system.improved_solution.models.LeaderboardType;
import org.lld.practice.design_leaderboard_system.improved_solution.models.PlayerScore;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing leaderboards.
 * Factory pattern: Creates and manages different types of leaderboards.
 */
public class LeaderboardService {
    private final Map<String, Leaderboard> leaderboards;
    
    public LeaderboardService() {
        this.leaderboards = new ConcurrentHashMap<>();
    }
    
    /**
     * Creates a new leaderboard.
     * 
     * @param type The leaderboard type
     * @return The created leaderboard
     */
    public Leaderboard createLeaderboard(LeaderboardType type) {
        String leaderboardId = UUID.randomUUID().toString();
        Leaderboard leaderboard = new Leaderboard(leaderboardId, type);
        leaderboards.put(leaderboardId, leaderboard);
        return leaderboard;
    }
    
    /**
     * Gets a leaderboard by ID.
     * 
     * @param leaderboardId The leaderboard ID
     * @return The leaderboard, or null if not found
     */
    public Leaderboard getLeaderboard(String leaderboardId) {
        return leaderboards.get(leaderboardId);
    }
    
    /**
     * Adds score to a leaderboard.
     * 
     * @param leaderboardId The leaderboard ID
     * @param playerId The player ID
     * @param score The score to add
     */
    public void addScore(String leaderboardId, String playerId, int score) {
        Leaderboard leaderboard = leaderboards.get(leaderboardId);
        if (leaderboard == null) {
            throw new IllegalArgumentException("Leaderboard not found: " + leaderboardId);
        }
        leaderboard.addScore(playerId, score);
    }
    
    /**
     * Gets top N players from a leaderboard.
     * 
     * @param leaderboardId The leaderboard ID
     * @param n Number of top players
     * @return List of top N player scores
     */
    public List<PlayerScore> getTopPlayers(String leaderboardId, int n) {
        Leaderboard leaderboard = leaderboards.get(leaderboardId);
        if (leaderboard == null) {
            throw new IllegalArgumentException("Leaderboard not found: " + leaderboardId);
        }
        return leaderboard.getTopPlayers(n);
    }
    
    /**
     * Gets a player's rank in a leaderboard.
     * 
     * @param leaderboardId The leaderboard ID
     * @param playerId The player ID
     * @return The player's rank (1-indexed), or -1 if not found
     */
    public int getPlayerRank(String leaderboardId, String playerId) {
        Leaderboard leaderboard = leaderboards.get(leaderboardId);
        if (leaderboard == null) {
            throw new IllegalArgumentException("Leaderboard not found: " + leaderboardId);
        }
        return leaderboard.getRank(playerId);
    }
    
    /**
     * Gets players around a specific rank (for pagination).
     * 
     * @param leaderboardId The leaderboard ID
     * @param rank The rank to get players around
     * @param range Number of players before and after
     * @return List of player scores around the rank
     */
    public List<PlayerScore> getPlayersAroundRank(String leaderboardId, int rank, int range) {
        Leaderboard leaderboard = leaderboards.get(leaderboardId);
        if (leaderboard == null) {
            throw new IllegalArgumentException("Leaderboard not found: " + leaderboardId);
        }
        
        List<PlayerScore> topPlayers = leaderboard.getTopPlayers(rank + range);
        int startIndex = Math.max(0, rank - range - 1);
        int endIndex = Math.min(topPlayers.size(), rank + range);
        
        return topPlayers.subList(startIndex, endIndex);
    }
}

