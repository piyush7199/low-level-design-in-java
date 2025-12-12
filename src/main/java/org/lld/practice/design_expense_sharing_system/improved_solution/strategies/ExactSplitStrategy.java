package org.lld.practice.design_expense_sharing_system.improved_solution.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Strategy for exact amount split - each participant pays a specific amount.
 */
public class ExactSplitStrategy implements SplitStrategy {
    
    @Override
    public Map<String, Double> split(double amount, String paidBy, List<String> participants,
                                   Map<String, Object> splitParams) {
        Map<String, Double> splits = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        Map<String, Double> exactAmounts = (Map<String, Double>) splitParams.get("amounts");
        
        if (exactAmounts == null || exactAmounts.size() != participants.size()) {
            throw new IllegalArgumentException("Exact amounts must be provided for all participants");
        }
        
        double total = exactAmounts.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(total - amount) > 0.01) {
            throw new IllegalArgumentException("Exact amounts must sum to total expense amount");
        }
        
        for (String participant : participants) {
            splits.put(participant, exactAmounts.get(participant));
        }
        
        return splits;
    }
}

