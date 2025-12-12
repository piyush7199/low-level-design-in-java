package org.lld.practice.design_leaderboard_system.improved_solution;

import org.lld.practice.design_leaderboard_system.improved_solution.models.Leaderboard;
import org.lld.practice.design_leaderboard_system.improved_solution.models.LeaderboardType;
import org.lld.practice.design_leaderboard_system.improved_solution.models.PlayerScore;
import org.lld.practice.design_leaderboard_system.improved_solution.services.LeaderboardService;

import java.util.List;

/**
 * Demo of improved leaderboard system with efficient ranking.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Improved Leaderboard System Demo ===\n");
        
        LeaderboardService service = new LeaderboardService();
        
        System.out.println("1. Creating leaderboards:");
        Leaderboard dailyLeaderboard = service.createLeaderboard(LeaderboardType.DAILY);
        Leaderboard allTimeLeaderboard = service.createLeaderboard(LeaderboardType.ALL_TIME);
        System.out.println("Daily leaderboard created: " + dailyLeaderboard.getLeaderboardId());
        System.out.println("All-time leaderboard created: " + allTimeLeaderboard.getLeaderboardId());
        
        System.out.println("\n2. Adding scores to daily leaderboard:");
        service.addScore(dailyLeaderboard.getLeaderboardId(), "player1", 100);
        service.addScore(dailyLeaderboard.getLeaderboardId(), "player2", 200);
        service.addScore(dailyLeaderboard.getLeaderboardId(), "player3", 150);
        service.addScore(dailyLeaderboard.getLeaderboardId(), "player1", 50); // Update existing
        
        System.out.println("\n3. Getting top 3 players:");
        List<PlayerScore> topPlayers = service.getTopPlayers(dailyLeaderboard.getLeaderboardId(), 3);
        for (int i = 0; i < topPlayers.size(); i++) {
            PlayerScore player = topPlayers.get(i);
            System.out.println("Rank " + (i + 1) + ": " + player.getPlayerId() + " - " + player.getScore());
        }
        
        System.out.println("\n4. Getting player rank:");
        int rank = service.getPlayerRank(dailyLeaderboard.getLeaderboardId(), "player1");
        System.out.println("Player1 rank: " + rank);
        
        System.out.println("\n5. Getting players around a rank:");
        List<PlayerScore> aroundRank = service.getPlayersAroundRank(dailyLeaderboard.getLeaderboardId(), 2, 1);
        System.out.println("Players around rank 2:");
        aroundRank.forEach(p -> System.out.println("  " + p.getPlayerId() + ": " + p.getScore()));
        
        System.out.println("\n6. Adding more players:");
        service.addScore(dailyLeaderboard.getLeaderboardId(), "player4", 300);
        service.addScore(dailyLeaderboard.getLeaderboardId(), "player5", 180);
        
        System.out.println("\nUpdated top 3:");
        service.getTopPlayers(dailyLeaderboard.getLeaderboardId(), 3)
                .forEach(p -> System.out.println("  " + p.getPlayerId() + ": " + p.getScore()));
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Efficient O(log n) score updates using TreeSet");
        System.out.println("✓ Fast O(1) top N retrieval");
        System.out.println("✓ Thread-safe operations");
        System.out.println("✓ Support for multiple leaderboard types");
        System.out.println("✓ Factory pattern for creating leaderboards");
        System.out.println("✓ Easy to extend with new features");
    }
}

