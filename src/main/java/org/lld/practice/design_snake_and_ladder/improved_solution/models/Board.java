package org.lld.practice.design_snake_and_ladder.improved_solution.models;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private static volatile Board instance;
    private final int boardSize;
    private final Map<Integer, Integer> snakesAndLadders;

    private Board(int boardSize) {
        this.boardSize = boardSize;
        this.snakesAndLadders = new HashMap<>();
    }

    public static Board getInstance(int boardSize) {
        if (instance == null) {
            synchronized (Board.class) {
                if (instance == null) {
                    instance = new Board(boardSize);
                }

            }
        }
        return instance;
    }

    private void setupSnakesAndLadders() {
        // Snakes: head -> tail
        snakesAndLadders.put(17, 7);
        snakesAndLadders.put(54, 34);
        snakesAndLadders.put(62, 19);
        snakesAndLadders.put(64, 60);
        snakesAndLadders.put(87, 24);
        snakesAndLadders.put(93, 73);
        snakesAndLadders.put(95, 75);
        snakesAndLadders.put(98, 79);

        // Ladders: start -> end
        snakesAndLadders.put(4, 14);
        snakesAndLadders.put(9, 31);
        snakesAndLadders.put(20, 38);
        snakesAndLadders.put(28, 84);
        snakesAndLadders.put(40, 59);
        snakesAndLadders.put(51, 67);
        snakesAndLadders.put(71, 91);
        snakesAndLadders.put(80, 100);
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getSnakeOrLadderEndPosition(int startPosition) {
        return snakesAndLadders.getOrDefault(startPosition, startPosition);
    }
}
