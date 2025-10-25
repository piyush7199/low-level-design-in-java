package org.lld.practice.design_atm_system.improved_solution.transactions;

import org.lld.practice.design_atm_system.improved_solution.ATM;
import org.lld.practice.design_atm_system.improved_solution.models.Account;

public class WithdrawalTransaction implements Transaction {
    private final double amount;

    public WithdrawalTransaction(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute(ATM atm) {
        Account account = atm.getCurrentAccount();
        
        if (account.getBalance() < amount) {
            atm.getScreen().display("Insufficient balance. Current balance: $" + account.getBalance());
            return false;
        }
        
        if (!atm.getCashDispenser().hasSufficientCash(amount)) {
            atm.getScreen().display("ATM does not have sufficient cash.");
            return false;
        }
        
        if (atm.getBankingService().processWithdrawal(account, amount)) {
            atm.getCashDispenser().dispenseCash(amount);
            atm.getScreen().display("Please collect your cash. Amount: $" + amount);
            atm.getScreen().display("Remaining balance: $" + account.getBalance());
            return true;
        }
        
        return false;
    }

    @Override
    public String getType() {
        return "WITHDRAWAL";
    }
}

