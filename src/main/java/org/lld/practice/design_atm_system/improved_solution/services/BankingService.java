package org.lld.practice.design_atm_system.improved_solution.services;

import org.lld.practice.design_atm_system.improved_solution.models.Account;

import java.util.HashMap;
import java.util.Map;

public class BankingService {
    private final Map<String, Account> accounts;

    public BankingService() {
        this.accounts = new HashMap<>();
        initializeAccounts();
    }

    private void initializeAccounts() {
        accounts.put("1234", new Account("1234", "ACC001", "5678", 5000));
        accounts.put("5678", new Account("5678", "ACC002", "1234", 10000));
        accounts.put("9012", new Account("9012", "ACC003", "3456", 15000));
    }

    public Account getAccount(String cardNumber) {
        return accounts.get(cardNumber);
    }

    public boolean processWithdrawal(Account account, double amount) {
        if (account.getBalance() >= amount) {
            account.withdraw(amount);
            System.out.println("Withdrawal processed successfully.");
            return true;
        }
        System.out.println("Insufficient funds.");
        return false;
    }

    public boolean processDeposit(Account account, double amount) {
        account.deposit(amount);
        System.out.println("Deposit processed successfully.");
        return true;
    }

    public double checkBalance(Account account) {
        return account.getBalance();
    }
}

