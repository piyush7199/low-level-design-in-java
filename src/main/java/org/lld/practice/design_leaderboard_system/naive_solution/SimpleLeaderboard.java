package org.lld.practice.design_leaderboard_system.naive_solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Naive implementation of a leaderboard system.
 * 
 * This demonstrates common pitfalls:
 * - Sorting entire list on every update (O(n log n))
 * - No efficient ranking lookup
 * - Not thread-safe
 * - No time-based leaderboards
 */
public class SimpleLeaderboard {
    private final List<Player> players = new ArrayList<>();
    
    public void addScore(String playerId, int score) {
        // Check if player exists
        Player existingPlayer = players.stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .findFirst()
                .orElse(null);
        
        if (existingPlayer != null) {
            existingPlayer.setScore(existingPlayer.getScore() + score);
        } else {
            players.add(new Player(playerId, score));
        }
        
        // Sort entire list - expensive operation!
        Collections.sort(players, (a, b) -> Integer.compare(b.getScore(), a.getScore()));
    }
    
    public List<Player> getTopPlayers(int n) {
        return players.subList(0, Math.min(n, players.size()));
    }
    
    public int getRank(String playerId) {
        // Linear search - O(n)
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerId().equals(playerId)) {
                return i + 1; // Rank is 1-indexed
            }
        }
        return -1; // Player not found
    }
    
    static class Player {
        private final String playerId;
        private int score;
        
        public Player(String playerId, int score) {
            this.playerId = playerId;
            this.score = score;
        }
        
        public String getPlayerId() {
            return playerId;
        }
        
        public int getScore() {
            return score;
        }
        
        public void setScore(int score) {
            this.score = score;
        }
    }
}

