package org.lld.practice.design_atm_system.improved_solution.models;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final String cardNumber;
    private final String accountNumber;
    private String pin;
    private double balance;
    private boolean isBlocked;
    private List<String> transactionHistory;

    public Account(String cardNumber, String accountNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.isBlocked = false;
        this.transactionHistory = new ArrayList<>();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void block() {
        this.isBlocked = true;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
        transactionHistory.add("WITHDRAWAL: -$" + amount);
    }

    public void deposit(double amount) {
        this.balance += amount;
        transactionHistory.add("DEPOSIT: +$" + amount);
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
}

