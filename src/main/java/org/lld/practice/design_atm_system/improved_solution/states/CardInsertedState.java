package org.lld.practice.design_atm_system.improved_solution.states;

import org.lld.practice.design_atm_system.improved_solution.ATM;
import org.lld.practice.design_atm_system.improved_solution.models.Account;
import org.lld.practice.design_atm_system.improved_solution.transactions.Transaction;

public class CardInsertedState implements ATMState {
    private final ATM atm;
    private final String cardNumber;

    public CardInsertedState(ATM atm, String cardNumber) {
        this.atm = atm;
        this.cardNumber = cardNumber;
    }

    @Override
    public void insertCard(String cardNumber) {
        atm.getScreen().display("Card already inserted.");
    }

    @Override
    public void enterPIN(String pin) {
        Account account = atm.getAuthService().authenticate(cardNumber, pin);
        if (account != null) {
            atm.setCurrentAccount(account);
            atm.getScreen().display("Authentication successful. Please select a transaction.");
            atm.setState(new AuthenticatedState(atm));
        } else {
            atm.getScreen().display("Authentication failed. Card ejected.");
            atm.getCardReader().ejectCard();
            atm.setState(new IdleState(atm));
        }
    }

    @Override
    public void selectTransaction(Transaction transaction) {
        atm.getScreen().display("Please enter PIN first.");
    }

    @Override
    public void executeTransaction() {
        atm.getScreen().display("Please enter PIN first.");
    }

    @Override
    public void ejectCard() {
        atm.getCardReader().ejectCard();
        atm.getScreen().display("Card ejected.");
        atm.setState(new IdleState(atm));
    }
}

