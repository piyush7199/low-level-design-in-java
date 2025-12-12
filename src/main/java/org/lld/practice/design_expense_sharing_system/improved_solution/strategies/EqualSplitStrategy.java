package org.lld.practice.design_expense_sharing_system.improved_solution.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Strategy for equal split - divides amount equally among all participants.
 */
public class EqualSplitStrategy implements SplitStrategy {
    
    @Override
    public Map<String, Double> split(double amount, String paidBy, List<String> participants,
                                   Map<String, Object> splitParams) {
        Map<String, Double> splits = new HashMap<>();
        
        if (participants.isEmpty()) {
            return splits;
        }
        
        double perPerson = amount / participants.size();
        
        for (String participant : participants) {
            splits.put(participant, perPerson);
        }
        
        return splits;
    }
}

