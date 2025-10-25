package org.lld.practice.design_atm_system.improved_solution;

import org.lld.practice.design_atm_system.improved_solution.services.BankingService;
import org.lld.practice.design_atm_system.improved_solution.transactions.BalanceInquiryTransaction;
import org.lld.practice.design_atm_system.improved_solution.transactions.DepositTransaction;
import org.lld.practice.design_atm_system.improved_solution.transactions.WithdrawalTransaction;

public class Main {
    public static void main(String[] args) {
        BankingService bankingService = new BankingService();
        ATM atm = new ATM(bankingService);
        
        System.out.println("=== Scenario 1: Successful Withdrawal ===\n");
        atm.insertCard("1234");
        atm.enterPIN("5678");
        atm.selectTransaction(new BalanceInquiryTransaction());
        atm.selectTransaction(new WithdrawalTransaction(500));
        atm.ejectCard();
        
        System.out.println("\n=== Scenario 2: Failed PIN Attempts ===\n");
        atm.insertCard("1234");
        atm.enterPIN("0000");
        
        atm.insertCard("1234");
        atm.enterPIN("1111");
        
        atm.insertCard("1234");
        atm.enterPIN("2222"); // Third failed attempt - account should be blocked
        
        System.out.println("\n=== Scenario 3: Successful Deposit ===\n");
        atm.insertCard("5678");
        atm.enterPIN("1234");
        atm.selectTransaction(new BalanceInquiryTransaction());
        atm.selectTransaction(new DepositTransaction(1000));
        atm.selectTransaction(new BalanceInquiryTransaction());
        atm.ejectCard();
        
        System.out.println("\n=== Scenario 4: Insufficient Balance ===\n");
        atm.insertCard("9012");
        atm.enterPIN("3456");
        atm.selectTransaction(new WithdrawalTransaction(20000)); // Should fail
        atm.ejectCard();
    }
}

