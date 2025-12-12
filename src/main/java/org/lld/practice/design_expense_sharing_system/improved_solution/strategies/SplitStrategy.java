package org.lld.practice.design_expense_sharing_system.improved_solution.strategies;

import java.util.List;
import java.util.Map;

/**
 * Strategy interface for splitting expenses.
 * Different implementations handle different split types.
 */
public interface SplitStrategy {
    /**
     * Splits an expense amount among participants.
     * 
     * @param amount Total expense amount
     * @param paidBy User who paid the expense
     * @param participants List of participant user IDs
     * @param splitParams Additional parameters for split (e.g., percentages, exact amounts)
     * @return Map of userId -> amount owed
     */
    Map<String, Double> split(double amount, String paidBy, List<String> participants, 
                              Map<String, Object> splitParams);
}

