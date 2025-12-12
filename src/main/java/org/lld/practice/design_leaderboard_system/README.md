# Design A Leaderboard System

## 1. Problem Statement and Requirements

Our goal is to design a leaderboard system for games or competitions that can efficiently rank players, handle score updates, and retrieve top players.

### Functional Requirements:

- **Score Management**: Add, update, and retrieve player scores
- **Ranking**: Calculate and maintain player rankings
- **Top Players**: Get top N players from the leaderboard
- **Player Position**: Get a specific player's rank
- **Time-based Leaderboards**: Support daily, weekly, monthly, and all-time leaderboards
- **Multiple Leaderboards**: Support different game modes or categories
- **Score History**: Track score changes over time

### Non-Functional Requirements:

- **Performance**: Fast ranking updates and queries (O(log n) for updates, O(1) for top N)
- **Scalability**: Handle millions of players
- **Concurrency**: Handle concurrent score updates without race conditions
- **Real-time Updates**: Leaderboard should reflect latest scores quickly

---

## 2. Naive Solution: The "Starting Point"

A beginner might use a simple list sorted by score.

### The Thought Process:

"I'll store all players in a list and sort it by score whenever I need the leaderboard."

```java
class Leaderboard {
    private List<Player> players = new ArrayList<>();
    
    public void addScore(String playerId, int score) {
        players.add(new Player(playerId, score));
        Collections.sort(players, (a, b) -> b.getScore() - a.getScore());
    }
    
    public List<Player> getTopPlayers(int n) {
        return players.subList(0, Math.min(n, players.size()));
    }
}
```

### Limitations and Design Flaws:

1. **Performance Issues**: Sorting entire list on every update is O(n log n)
2. **No Efficient Ranking**: Finding a player's rank requires linear search
3. **No Time-based Support**: Cannot filter by time period
4. **Concurrency Issues**: Not thread-safe
5. **Memory Inefficient**: Stores all players even if only top N needed
6. **No Score History**: Cannot track score changes

---

## 3. Improved Solution: The "Mentor's Guidance"

Use efficient data structures (TreeSet, PriorityQueue) and separate ranking strategies.

### Design Patterns Used:

1. **Strategy Pattern**: For different ranking algorithms (score-based, time-based, weighted)
2. **Factory Pattern**: For creating different types of leaderboards
3. **Observer Pattern**: For notifying about rank changes
4. **Repository Pattern**: For abstracting data storage

### Core Classes and Interactions:

1. **LeaderboardService** (Orchestrator):
   - Main entry point for leaderboard operations
   - Manages score updates and ranking calculations

2. **RankingStrategy** (Strategy Interface):
   - Defines ranking algorithm
   - Implementations: ScoreRankingStrategy, TimeBasedRankingStrategy

3. **Leaderboard** (Model):
   - Represents a leaderboard with metadata
   - Supports different time periods

4. **PlayerScore** (Model):
   - Represents a player's score with metadata
   - Tracks score history

5. **ScoreRepository** (Repository):
   - Abstracts score storage
   - Handles persistence and retrieval

### Key Design Benefits:

- **Performance**: Efficient data structures for O(log n) updates
- **Flexibility**: Strategy pattern allows different ranking algorithms
- **Scalability**: Can handle millions of players
- **Extensibility**: Easy to add new ranking strategies or leaderboard types

---

## 4. Final Design Overview

The improved solution uses Strategy pattern for ranking algorithms and efficient data structures to create a scalable, high-performance leaderboard system.

