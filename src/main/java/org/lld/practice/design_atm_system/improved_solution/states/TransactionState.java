package org.lld.practice.design_atm_system.improved_solution.states;

import org.lld.practice.design_atm_system.improved_solution.ATM;
import org.lld.practice.design_atm_system.improved_solution.transactions.Transaction;

public class TransactionState implements ATMState {
    private final ATM atm;
    private final Transaction transaction;

    public TransactionState(ATM atm, Transaction transaction) {
        this.atm = atm;
        this.transaction = transaction;
        executeTransaction();
    }

    @Override
    public void insertCard(String cardNumber) {
        atm.getScreen().display("Transaction in progress. Please wait.");
    }

    @Override
    public void enterPIN(String pin) {
        atm.getScreen().display("Transaction in progress. Please wait.");
    }

    @Override
    public void selectTransaction(Transaction transaction) {
        atm.getScreen().display("Transaction in progress. Please wait.");
    }

    @Override
    public void executeTransaction() {
        boolean success = transaction.execute(atm);
        if (success) {
            atm.getScreen().display("Transaction completed successfully.");
        } else {
            atm.getScreen().display("Transaction failed.");
        }
        atm.setState(new AuthenticatedState(atm));
    }

    @Override
    public void ejectCard() {
        atm.getScreen().display("Transaction in progress. Please wait.");
    }
}

