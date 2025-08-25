package org.lld.practice.design_vending_machine.improved_solution.states;

import org.lld.practice.design_vending_machine.improved_solution.models.Coin;

public interface VendingMachineState {
    void insertCoin(Coin coin);

    void selectProduct(String productName);

    void dispenseProduct();

    void refund();
}
