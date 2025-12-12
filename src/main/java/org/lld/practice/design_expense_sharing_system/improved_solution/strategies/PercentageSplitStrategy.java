package org.lld.practice.design_expense_sharing_system.improved_solution.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Strategy for percentage-based split.
 */
public class PercentageSplitStrategy implements SplitStrategy {
    
    @Override
    public Map<String, Double> split(double amount, String paidBy, List<String> participants,
                                   Map<String, Object> splitParams) {
        Map<String, Double> splits = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        Map<String, Double> percentages = (Map<String, Double>) splitParams.get("percentages");
        
        if (percentages == null || percentages.size() != participants.size()) {
            throw new IllegalArgumentException("Percentages must be provided for all participants");
        }
        
        double totalPercentage = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalPercentage - 100.0) > 0.01) {
            throw new IllegalArgumentException("Percentages must sum to 100");
        }
        
        for (String participant : participants) {
            double percentage = percentages.get(participant);
            splits.put(participant, (amount * percentage) / 100.0);
        }
        
        return splits;
    }
}

