package org.lld.practice.design_atm_system.improved_solution.states;

import org.lld.practice.design_atm_system.improved_solution.ATM;
import org.lld.practice.design_atm_system.improved_solution.transactions.Transaction;

public class IdleState implements ATMState {
    private final ATM atm;

    public IdleState(ATM atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(String cardNumber) {
        if (atm.getCardReader().readCard(cardNumber)) {
            atm.getScreen().display("Card accepted. Please enter PIN.");
            atm.setState(new CardInsertedState(atm, cardNumber));
        } else {
            atm.getScreen().display("Invalid card. Please try again.");
        }
    }

    @Override
    public void enterPIN(String pin) {
        atm.getScreen().display("Please insert card first.");
    }

    @Override
    public void selectTransaction(Transaction transaction) {
        atm.getScreen().display("Please insert card first.");
    }

    @Override
    public void executeTransaction() {
        atm.getScreen().display("Please insert card first.");
    }

    @Override
    public void ejectCard() {
        atm.getScreen().display("No card inserted.");
    }
}

