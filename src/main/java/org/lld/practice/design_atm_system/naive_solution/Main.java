package org.lld.practice.design_atm_system.naive_solution;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        
        // Scenario 1: Successful transaction
        System.out.println("=== Scenario 1: Successful Withdrawal ===");
        atm.insertCard("1234");
        atm.enterPIN("5678");
        atm.checkBalance();
        atm.withdrawCash(500);
        atm.ejectCard();
        
        System.out.println("\n=== Scenario 2: Failed PIN Attempts ===");
        atm.insertCard("1234");
        atm.enterPIN("0000");
        atm.enterPIN("1111");
        atm.enterPIN("2222"); // Third failed attempt - account blocked
        
        System.out.println("\n=== Scenario 3: Deposit ===");
        atm.insertCard("5678");
        atm.enterPIN("1234");
        atm.checkBalance();
        atm.depositCash(1000);
        atm.ejectCard();
    }
}

