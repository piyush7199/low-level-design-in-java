# Design Social Media Feed System

## 1. Problem Statement and Requirements

Design a Social Media Feed System (like Twitter, Facebook, Instagram) that generates personalized news feeds for users based on the people they follow.

### Functional Requirements:

- **User Management**: Register users, follow/unfollow other users
- **Post Creation**: Users can create posts (text, images, videos)
- **Feed Generation**: Generate personalized feed for each user
- **Feed Ranking**: Rank posts in feed based on relevance, recency, engagement
- **Real-time Updates**: Update feed when followed users post new content
- **Feed Types**: Support different feed types (Home feed, User profile feed)
- **Engagement**: Support likes, comments, shares on posts
- **Pagination**: Support pagination for large feeds

### Non-Functional Requirements:

- **Low Latency**: Generate feed within milliseconds
- **Scalability**: Handle millions of users and posts
- **Real-time**: Update feeds in near real-time
- **Personalization**: Customize feed based on user preferences
- **Consistency**: Ensure feed consistency across requests

---

## 2. Naive Solution: The "Starting Point"

### The Thought Process:

A beginner might create a simple system that fetches all posts from followed users:

```java
class SimpleFeedSystem {
    private Map<String, List<String>> followers; // userId -> list of followed users
    private Map<String, List<Post>> userPosts;   // userId -> list of posts
    
    public List<Post> getFeed(String userId) {
        List<Post> feed = new ArrayList<>();
        List<String> followedUsers = followers.get(userId);
        
        for (String followedUserId : followedUsers) {
            feed.addAll(userPosts.get(followedUserId));
        }
        
        // Simple sort by timestamp
        feed.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return feed;
    }
}
```

### Limitations and Design Flaws:

1. **O(n*m) Complexity**:
   - Iterates through all followed users and their posts
   - Doesn't scale with large number of follows or posts
   - No caching or optimization

2. **No Ranking Algorithm**:
   - Only sorts by timestamp
   - Ignores engagement (likes, comments)
   - No personalization

3. **No Real-time Updates**:
   - Feed generated on-demand only
   - No push notifications for new posts
   - No pre-computed feeds

4. **No Pagination**:
   - Returns all posts at once
   - Memory intensive for users with many follows
   - Poor performance

5. **No Feed Types**:
   - Only supports home feed
   - No user profile feed
   - No trending/hashtag feeds

6. **No Caching**:
   - Recomputes feed on every request
   - No feed pre-computation
   - Wastes computational resources

7. **Violation of SOLID**:
   - Single class doing everything
   - No separation of concerns
   - Hard to extend with new features

---

## 3. Improved Solution: The "Mentor's Guidance"

### Design Patterns Used:

| Pattern | Usage | Why |
|---------|-------|-----|
| **Strategy** | Feed Ranking | Different ranking algorithms (Recency, Engagement, Hybrid) |
| **Observer** | Feed Updates | Notify users when followed users post |
| **Factory** | Feed Generation | Create different feed types (Home, Profile, Trending) |
| **Singleton** | Feed Service | Single instance managing all feeds |
| **Repository** | Data Access | Abstract post and user storage |

### Core Classes and Their Interactions:

#### 1. Models Layer (`models/`)
- `User` - User entity with profile information
- `Post` - Post entity with content, author, timestamp, engagement metrics
- `Feed` - Feed entity containing list of posts
- `FeedType` - Enum: HOME, PROFILE, TRENDING
- `Engagement` - Likes, comments, shares count

#### 2. Strategy Pattern (`strategies/`)
- `FeedRankingStrategy` - Interface for ranking algorithms
- `RecencyRankingStrategy` - Rank by timestamp (newest first)
- `EngagementRankingStrategy` - Rank by likes, comments, shares
- `HybridRankingStrategy` - Combine recency and engagement

#### 3. Services (`services/`)
- `FeedService` - Singleton managing feed generation
- `PostService` - Handles post creation, retrieval
- `UserService` - Manages users, follow/unfollow
- `FeedCacheService` - Caches pre-computed feeds

#### 4. Observers (`observers/`)
- `FeedObserver` - Interface for feed updates
- `UserFeedObserver` - Updates user feeds when new posts arrive
- `NotificationObserver` - Sends notifications about new posts

#### 5. Repositories (`repositories/`)
- `PostRepository` - Data access for posts
- `UserRepository` - Data access for users
- `FollowRepository` - Data access for follow relationships

### Key Design Benefits:

- **Efficient Ranking**: Strategy pattern allows different ranking algorithms
- **Real-time Updates**: Observer pattern notifies users of new posts
- **Caching**: Pre-computed feeds reduce latency
- **Extensibility**: Easy to add new feed types or ranking strategies
- **Separation of Concerns**: Each component has single responsibility

---

## 4. Final Design Overview

The improved solution uses Strategy pattern for flexible feed ranking, Observer pattern for real-time updates, and proper caching to create a scalable, responsive social media feed system.

### Architecture Highlights:

- **Feed Generation**: Pull model (on-demand) with caching, or push model (pre-computed)
- **Ranking**: Multiple ranking strategies for personalization
- **Caching**: Cache feeds to reduce computation
- **Pagination**: Efficient pagination for large feeds
- **Real-time**: Observer pattern for instant feed updates

---

## 5. Interview Discussion Points

1. **Feed Generation**: Pull vs Push model - trade-offs?
2. **Ranking Algorithm**: How to rank posts by relevance?
3. **Caching Strategy**: What to cache and for how long?
4. **Scalability**: How to handle millions of users?
5. **Real-time Updates**: How to push updates to users?
6. **Pagination**: How to efficiently paginate large feeds?
7. **Personalization**: How to customize feeds per user?

---

## 6. Key Learnings

- **Ranking Algorithms**: Critical for user engagement
- **Caching**: Essential for performance at scale
- **Real-time Updates**: Observer pattern enables instant notifications
- **Feed Models**: Pull vs Push - each has trade-offs
- **Pagination**: Must handle large result sets efficiently
- **Personalization**: Different users need different feeds

