package org.lld.practice.design_atm_system.improved_solution.services;

import org.lld.practice.design_atm_system.improved_solution.models.Account;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private final BankingService bankingService;
    private final Map<String, Integer> failedAttempts;
    private static final int MAX_FAILED_ATTEMPTS = 3;

    public AuthenticationService(BankingService bankingService) {
        this.bankingService = bankingService;
        this.failedAttempts = new HashMap<>();
    }

    public Account authenticate(String cardNumber, String pin) {
        if (failedAttempts.getOrDefault(cardNumber, 0) >= MAX_FAILED_ATTEMPTS) {
            System.out.println("Account blocked due to multiple failed attempts.");
            return null;
        }

        Account account = bankingService.getAccount(cardNumber);
        if (account == null) {
            System.out.println("Invalid card number.");
            return null;
        }

        if (account.isBlocked()) {
            System.out.println("Account is blocked.");
            return null;
        }

        if (account.getPin().equals(pin)) {
            failedAttempts.remove(cardNumber);
            System.out.println("Authentication successful.");
            return account;
        } else {
            int attempts = failedAttempts.getOrDefault(cardNumber, 0) + 1;
            failedAttempts.put(cardNumber, attempts);
            
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                account.block();
                System.out.println("Account blocked due to " + MAX_FAILED_ATTEMPTS + " failed attempts.");
            } else {
                System.out.println("Invalid PIN. Attempts remaining: " + (MAX_FAILED_ATTEMPTS - attempts));
            }
            return null;
        }
    }

    public void resetFailedAttempts(String cardNumber) {
        failedAttempts.remove(cardNumber);
    }
}

