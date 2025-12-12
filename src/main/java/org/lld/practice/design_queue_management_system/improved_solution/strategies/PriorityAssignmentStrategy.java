package org.lld.practice.design_queue_management_system.improved_solution.strategies;

import org.lld.practice.design_queue_management_system.improved_solution.models.Token;
import org.lld.practice.design_queue_management_system.improved_solution.models.TokenType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Priority-based token assignment strategy.
 * 
 * Selects tokens based on their priority level first, then by creation time.
 * Higher priority tokens (VIP > Priority > Senior > Regular) are served first.
 * 
 * To prevent starvation, an aging mechanism can be added where
 * wait time increases effective priority.
 */
public class PriorityAssignmentStrategy implements TokenAssignmentStrategy {
    
    private static final long AGING_THRESHOLD_SECONDS = 300; // 5 minutes
    private static final int AGING_PRIORITY_BOOST = 1;

    @Override
    public Optional<Token> getNextToken(List<Token> waitingTokens, List<TokenType> supportedTypes) {
        if (waitingTokens == null || waitingTokens.isEmpty()) {
            return Optional.empty();
        }
        
        return waitingTokens.stream()
                .filter(token -> supportedTypes.contains(token.getType()))
                .max(Comparator.comparingInt(this::getEffectivePriority)
                        .thenComparing(Token::getCreatedAt, Comparator.reverseOrder()));
    }
    
    /**
     * Calculate effective priority considering wait time (aging).
     * Long-waiting tokens get priority boost to prevent starvation.
     */
    private int getEffectivePriority(Token token) {
        int basePriority = token.getType().getPriorityLevel();
        long waitTimeSeconds = token.getWaitTimeSeconds();
        
        // Boost priority for every AGING_THRESHOLD_SECONDS of waiting
        int agingBoost = (int) (waitTimeSeconds / AGING_THRESHOLD_SECONDS) * AGING_PRIORITY_BOOST;
        
        return basePriority + agingBoost;
    }
}

