package org.lld.practice.design_atm_system.improved_solution.states;

import org.lld.practice.design_atm_system.improved_solution.transactions.Transaction;

public interface ATMState {
    void insertCard(String cardNumber);
    void enterPIN(String pin);
    void selectTransaction(Transaction transaction);
    void executeTransaction();
    void ejectCard();
}

