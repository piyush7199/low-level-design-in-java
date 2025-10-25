package org.lld.practice.design_atm_system.improved_solution.hardware;

public class Screen {
    public void display(String message) {
        System.out.println("[Screen] " + message);
    }

    public void displayMenu() {
        display("=== ATM Menu ===");
        display("1. Check Balance");
        display("2. Withdraw Cash");
        display("3. Deposit Cash");
        display("4. Eject Card");
    }
}

