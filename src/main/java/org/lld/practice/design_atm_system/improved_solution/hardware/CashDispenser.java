package org.lld.practice.design_atm_system.improved_solution.hardware;

import java.util.HashMap;
import java.util.Map;

public class CashDispenser {
    private Map<Integer, Integer> cashInventory; // denomination -> count

    public CashDispenser() {
        this.cashInventory = new HashMap<>();
        initializeCash();
    }

    private void initializeCash() {
        cashInventory.put(20, 100);
        cashInventory.put(50, 50);
        cashInventory.put(100, 30);
    }

    public boolean hasSufficientCash(double amount) {
        double availableCash = 0;
        for (Map.Entry<Integer, Integer> entry : cashInventory.entrySet()) {
            availableCash += entry.getKey() * entry.getValue();
        }
        return availableCash >= amount;
    }

    public void dispenseCash(double amount) {
        int remaining = (int) amount;
        Map<Integer, Integer> dispensed = new HashMap<>();
        
        Integer[] denominations = {100, 50, 20};
        for (int denom : denominations) {
            if (remaining >= denom && cashInventory.getOrDefault(denom, 0) > 0) {
                int count = Math.min(remaining / denom, cashInventory.get(denom));
                dispensed.put(denom, count);
                remaining -= count * denom;
                cashInventory.put(denom, cashInventory.get(denom) - count);
            }
        }
        
        System.out.println("[CashDispenser] Dispensing cash: " + dispensed);
        System.out.println("[CashDispenser] Remaining inventory: " + cashInventory);
    }

    public void addCash(int denomination, int count) {
        cashInventory.put(denomination, cashInventory.getOrDefault(denomination, 0) + count);
    }

    public Map<Integer, Integer> getCashInventory() {
        return new HashMap<>(cashInventory);
    }
}

