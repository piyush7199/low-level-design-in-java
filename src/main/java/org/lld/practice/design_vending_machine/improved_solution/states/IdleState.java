package org.lld.practice.design_vending_machine.improved_solution.states;

import org.lld.practice.design_vending_machine.improved_solution.models.Coin;

public class IdleState implements VendingMachineState {
    private final VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin(Coin coin) {
        machine.getCoinHandler().insertCoin(coin);
        machine.setState(new HasMoneyState(machine));
    }

    @Override
    public void selectProduct(String productName) {
        System.out.println("Please insert money first.");
    }

    @Override
    public void dispenseProduct() {
        System.out.println("Operation not allowed.");
    }

    @Override
    public void refund() {
        System.out.println("No money to refund.");
    }
}
