package org.lld.practice.design_atm_system.naive_solution;

import java.util.HashMap;
import java.util.Map;

public class ATM {
    private Map<String, Account> accounts = new HashMap<>();
    private Map<Integer, Integer> cashInventory = new HashMap<>(); // denomination -> count
    private String currentCardNumber;
    private boolean authenticated = false;
    private int failedAttempts = 0;

    public ATM() {
        // Initialize with some cash
        cashInventory.put(20, 100);
        cashInventory.put(50, 50);
        cashInventory.put(100, 30);
        
        // Add sample accounts
        accounts.put("1234", new Account("1234", "5678", 5000));
        accounts.put("5678", new Account("5678", "1234", 10000));
    }

    public boolean insertCard(String cardNumber) {
        if (accounts.containsKey(cardNumber)) {
            currentCardNumber = cardNumber;
            System.out.println("Card inserted successfully.");
            return true;
        }
        System.out.println("Invalid card.");
        return false;
    }

    public boolean enterPIN(String pin) {
        if (currentCardNumber == null) {
            System.out.println("Please insert card first.");
            return false;
        }
        
        Account account = accounts.get(currentCardNumber);
        if (account.getPin().equals(pin)) {
            authenticated = true;
            failedAttempts = 0;
            System.out.println("Authentication successful.");
            return true;
        } else {
            failedAttempts++;
            if (failedAttempts >= 3) {
                System.out.println("Account blocked due to multiple failed attempts.");
                currentCardNumber = null;
            } else {
                System.out.println("Invalid PIN. Attempts remaining: " + (3 - failedAttempts));
            }
            return false;
        }
    }

    public double checkBalance() {
        if (!authenticated) {
            System.out.println("Please authenticate first.");
            return -1;
        }
        double balance = accounts.get(currentCardNumber).getBalance();
        System.out.println("Current balance: $" + balance);
        return balance;
    }

    public boolean withdrawCash(double amount) {
        if (!authenticated) {
            System.out.println("Please authenticate first.");
            return false;
        }
        
        Account account = accounts.get(currentCardNumber);
        if (account.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return false;
        }
        
        if (!hasEnoughCash(amount)) {
            System.out.println("ATM does not have enough cash.");
            return false;
        }
        
        account.withdraw(amount);
        dispenseCash(amount);
        System.out.println("Cash withdrawn successfully. Remaining balance: $" + account.getBalance());
        return true;
    }

    public boolean depositCash(double amount) {
        if (!authenticated) {
            System.out.println("Please authenticate first.");
            return false;
        }
        
        Account account = accounts.get(currentCardNumber);
        account.deposit(amount);
        System.out.println("Cash deposited successfully. New balance: $" + account.getBalance());
        return true;
    }

    public void ejectCard() {
        currentCardNumber = null;
        authenticated = false;
        failedAttempts = 0;
        System.out.println("Card ejected. Thank you!");
    }

    private boolean hasEnoughCash(double amount) {
        double availableCash = 0;
        for (Map.Entry<Integer, Integer> entry : cashInventory.entrySet()) {
            availableCash += entry.getKey() * entry.getValue();
        }
        return availableCash >= amount;
    }

    private void dispenseCash(double amount) {
        // Simple greedy algorithm to dispense cash
        int remaining = (int) amount;
        Map<Integer, Integer> dispensed = new HashMap<>();
        
        Integer[] denominations = {100, 50, 20};
        for (int denom : denominations) {
            if (remaining >= denom && cashInventory.get(denom) > 0) {
                int count = Math.min(remaining / denom, cashInventory.get(denom));
                dispensed.put(denom, count);
                remaining -= count * denom;
                cashInventory.put(denom, cashInventory.get(denom) - count);
            }
        }
        
        System.out.println("Dispensing: " + dispensed);
    }
}

