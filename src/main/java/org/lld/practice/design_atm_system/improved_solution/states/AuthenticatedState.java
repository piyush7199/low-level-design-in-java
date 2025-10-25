package org.lld.practice.design_atm_system.improved_solution.states;

import org.lld.practice.design_atm_system.improved_solution.ATM;
import org.lld.practice.design_atm_system.improved_solution.transactions.Transaction;

public class AuthenticatedState implements ATMState {
    private final ATM atm;
    private Transaction currentTransaction;

    public AuthenticatedState(ATM atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(String cardNumber) {
        atm.getScreen().display("Card already inserted and authenticated.");
    }

    @Override
    public void enterPIN(String pin) {
        atm.getScreen().display("Already authenticated.");
    }

    @Override
    public void selectTransaction(Transaction transaction) {
        this.currentTransaction = transaction;
        atm.getScreen().display("Transaction selected. Executing...");
        atm.setState(new TransactionState(atm, transaction));
    }

    @Override
    public void executeTransaction() {
        atm.getScreen().display("Please select a transaction first.");
    }

    @Override
    public void ejectCard() {
        atm.getCardReader().ejectCard();
        atm.setCurrentAccount(null);
        atm.getScreen().display("Card ejected. Thank you!");
        atm.setState(new IdleState(atm));
    }
}

