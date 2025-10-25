package org.lld.practice.design_atm_system.improved_solution.transactions;

import org.lld.practice.design_atm_system.improved_solution.ATM;

public interface Transaction {
    boolean execute(ATM atm);
    String getType();
}

