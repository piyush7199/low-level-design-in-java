package org.lld.practice.design_atm_system.improved_solution.transactions;

import org.lld.practice.design_atm_system.improved_solution.ATM;
import org.lld.practice.design_atm_system.improved_solution.models.Account;

public class DepositTransaction implements Transaction {
    private final double amount;

    public DepositTransaction(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute(ATM atm) {
        Account account = atm.getCurrentAccount();
        
        if (atm.getBankingService().processDeposit(account, amount)) {
            atm.getScreen().display("Deposit successful. Amount: $" + amount);
            atm.getScreen().display("New balance: $" + account.getBalance());
            return true;
        }
        
        return false;
    }

    @Override
    public String getType() {
        return "DEPOSIT";
    }
}

