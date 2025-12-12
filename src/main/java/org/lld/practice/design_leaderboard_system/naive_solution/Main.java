package org.lld.practice.design_leaderboard_system.naive_solution;

/**
 * Demo of naive leaderboard implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Leaderboard System Demo ===\n");
        
        SimpleLeaderboard leaderboard = new SimpleLeaderboard();
        
        leaderboard.addScore("player1", 100);
        leaderboard.addScore("player2", 200);
        leaderboard.addScore("player3", 150);
        leaderboard.addScore("player1", 50); // Update existing player
        
        System.out.println("Top 3 players:");
        leaderboard.getTopPlayers(3).forEach(p -> 
            System.out.println(p.getPlayerId() + ": " + p.getScore())
        );
        
        System.out.println("\nRank of player1: " + leaderboard.getRank("player1"));
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- Sorting entire list on every update (O(n log n))");
        System.out.println("- Linear search for ranking (O(n))");
        System.out.println("- Not thread-safe");
        System.out.println("- No time-based leaderboards");
        System.out.println("- No score history");
    }
}

