package org.lld.practice.design_atm_system.improved_solution;

import org.lld.practice.design_atm_system.improved_solution.hardware.CardReader;
import org.lld.practice.design_atm_system.improved_solution.hardware.CashDispenser;
import org.lld.practice.design_atm_system.improved_solution.hardware.Screen;
import org.lld.practice.design_atm_system.improved_solution.models.Account;
import org.lld.practice.design_atm_system.improved_solution.services.AuthenticationService;
import org.lld.practice.design_atm_system.improved_solution.services.BankingService;
import org.lld.practice.design_atm_system.improved_solution.states.*;
import org.lld.practice.design_atm_system.improved_solution.transactions.Transaction;

public class ATM {
    private ATMState currentState;
    private final CardReader cardReader;
    private final CashDispenser cashDispenser;
    private final Screen screen;
    private final AuthenticationService authService;
    private final BankingService bankingService;
    
    private Account currentAccount;

    public ATM(BankingService bankingService) {
        this.bankingService = bankingService;
        this.authService = new AuthenticationService(bankingService);
        this.cardReader = new CardReader();
        this.cashDispenser = new CashDispenser();
        this.screen = new Screen();
        this.currentState = new IdleState(this);
    }

    public void insertCard(String cardNumber) {
        currentState.insertCard(cardNumber);
    }

    public void enterPIN(String pin) {
        currentState.enterPIN(pin);
    }

    public void selectTransaction(Transaction transaction) {
        currentState.selectTransaction(transaction);
    }

    public void executeTransaction() {
        currentState.executeTransaction();
    }

    public void ejectCard() {
        currentState.ejectCard();
    }

    public void setState(ATMState state) {
        this.currentState = state;
    }

    public ATMState getCurrentState() {
        return currentState;
    }

    public CardReader getCardReader() {
        return cardReader;
    }

    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }

    public Screen getScreen() {
        return screen;
    }

    public AuthenticationService getAuthService() {
        return authService;
    }

    public BankingService getBankingService() {
        return bankingService;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }
}

