package org.lld.practice.design_atm_system.improved_solution.transactions;

import org.lld.practice.design_atm_system.improved_solution.ATM;
import org.lld.practice.design_atm_system.improved_solution.models.Account;

public class BalanceInquiryTransaction implements Transaction {

    @Override
    public boolean execute(ATM atm) {
        Account account = atm.getCurrentAccount();
        atm.getScreen().display("Your current balance is: $" + account.getBalance());
        return true;
    }

    @Override
    public String getType() {
        return "BALANCE_INQUIRY";
    }
}

