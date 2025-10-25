package org.lld.practice.design_atm_system.improved_solution.hardware;

public class CardReader {
    private String currentCard;

    public boolean readCard(String cardNumber) {
        if (cardNumber != null && !cardNumber.isEmpty()) {
            this.currentCard = cardNumber;
            System.out.println("[CardReader] Card read successfully: " + cardNumber);
            return true;
        }
        System.out.println("[CardReader] Failed to read card.");
        return false;
    }

    public void ejectCard() {
        System.out.println("[CardReader] Card ejected: " + currentCard);
        this.currentCard = null;
    }

    public String getCurrentCard() {
        return currentCard;
    }
}

