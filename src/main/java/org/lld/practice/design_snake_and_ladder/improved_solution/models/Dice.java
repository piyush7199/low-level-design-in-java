package org.lld.practice.design_snake_and_ladder.improved_solution.models;

import java.util.Random;

public class Dice {
    private static final int SIDES = 6;
    private final Random random;

    public Dice() {
        this.random = new Random();
    }

    public int roll() {
        // Roll a dice with 6 sides (1 to 6)
        return random.nextInt(SIDES) + 1;
    }
}
