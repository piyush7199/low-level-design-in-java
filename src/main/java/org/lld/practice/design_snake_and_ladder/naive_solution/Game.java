package org.lld.practice.design_snake_and_ladder.naive_solution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {
    private final int[] board;
    private final Map<Integer, Integer> snakesAndLadders;
    private final Random random;
    private final List<String> players;
    private final Map<String, Integer> positions;
    private int currentTurn;

    public Game(int size, List<String> playerNames) {
        this.board = new int[size + 1]; // positions 1..size
        this.snakesAndLadders = new HashMap<>();
        this.random = new Random();
        this.players = playerNames;
        this.positions = new HashMap<>();
        for (String p : playerNames) {
            positions.put(p, 1); // all start at position 1
        }
        this.currentTurn = 0;
    }

    public void initializeGame() {
        // Snakes
        snakesAndLadders.put(16, 6);
        snakesAndLadders.put(47, 26);
        snakesAndLadders.put(49, 11);
        snakesAndLadders.put(56, 53);
        snakesAndLadders.put(62, 19);
        snakesAndLadders.put(64, 60);
        snakesAndLadders.put(87, 24);
        snakesAndLadders.put(93, 73);
        snakesAndLadders.put(95, 75);
        snakesAndLadders.put(98, 78);

        // Ladders
        snakesAndLadders.put(1, 38);
        snakesAndLadders.put(4, 14);
        snakesAndLadders.put(9, 31);
        snakesAndLadders.put(21, 42);
        snakesAndLadders.put(28, 84);
        snakesAndLadders.put(36, 44);
        snakesAndLadders.put(51, 67);
        snakesAndLadders.put(71, 91);
        snakesAndLadders.put(80, 100);
    }

    public boolean playTurn() {
        String player = players.get(currentTurn);
        int pos = positions.get(player);

        int dice = random.nextInt(6) + 1;
        System.out.println(player + " rolled: " + dice);

        int nextPosition = pos + dice;
        if (nextPosition > board.length - 1) {
            System.out.println(player + " roll exceeds board size, stays at " + pos);
        } else {
            pos = nextPosition;
            if (snakesAndLadders.containsKey(pos)) {
                int newPos = snakesAndLadders.get(pos);
                if (newPos < pos) {
                    System.out.println("🐍 " + player + " bitten by snake: " + pos + " → " + newPos);
                } else {
                    System.out.println("🪜 " + player + " climbed ladder: " + pos + " → " + newPos);
                }
                pos = newPos;
            }
            positions.put(player, pos);
            System.out.println(player + " is now at " + pos);
        }

        // Win check
        if (pos == board.length - 1) {
            System.out.println("🎉 " + player + " wins the game!");
            return true;
        }

        // Next player's turn
        currentTurn = (currentTurn + 1) % players.size();
        return false;
    }
}
