package org.lld.practice.design_snake_and_ladder.improved_solution;

import org.lld.practice.design_snake_and_ladder.improved_solution.models.Player;

public class Main {
    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine(100);
        gameEngine.addPlayer(new Player("Player 1"));
        gameEngine.addPlayer(new Player("Player 2"));

        gameEngine.playGame();
    }
}
