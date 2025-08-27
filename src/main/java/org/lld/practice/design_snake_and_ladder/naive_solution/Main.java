package org.lld.practice.design_snake_and_ladder.naive_solution;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> players = Arrays.asList("Alice", "Bob", "Charlie");
        Game game = new Game(100, players);
        game.initializeGame();

        while (true) {
            boolean finished = game.playTurn();
            if (finished) break;
            try { Thread.sleep(700); } catch (Exception ignored) {}
        }
    }
}
