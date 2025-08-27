package org.lld.practice.design_snake_and_ladder.improved_solution;

import org.lld.practice.design_snake_and_ladder.improved_solution.models.Board;
import org.lld.practice.design_snake_and_ladder.improved_solution.models.Dice;
import org.lld.practice.design_snake_and_ladder.improved_solution.models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private final List<Player> players;
    private final Dice dice;
    private final Board board;
    private static final int WINNING_POSITION = 100;

    public GameEngine(int boardSize) {
        // The game engine composes the other components
        this.players = new ArrayList<>();
        this.dice = new Dice();
        // The board is a singleton, so we get the single instance
        this.board = Board.getInstance(boardSize);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void playGame() {
        if (players.isEmpty()) {
            System.out.println("No players added. Cannot start game.");
            return;
        }

        boolean gameIsRunning = true;

        while (gameIsRunning) {
            for (Player player : players) {
                int diceRoll = dice.roll();
                System.out.printf("%s rolled a %d.%n", player.getName(), diceRoll);

                int newPosition = player.getPosition() + diceRoll;

                // Check for win condition
                if (newPosition >= WINNING_POSITION) {
                    player.setPosition(WINNING_POSITION);
                    System.out.printf("%s moves to square %d and wins the game!%n", player.getName(), player.getPosition());
                    gameIsRunning = false;
                    break;
                }

                // Check for snake or ladder
                int finalPosition = board.getSnakeOrLadderEndPosition(newPosition);
                if (finalPosition > newPosition) {
                    System.out.printf("Wow! %s found a ladder and climbs from %d to %d.%n", player.getName(), newPosition, finalPosition);
                    player.setPosition(finalPosition);
                } else if (finalPosition < newPosition) {
                    System.out.printf("Oh no! %s was bitten by a snake and slides from %d to %d.%n", player.getName(), newPosition, finalPosition);
                    player.setPosition(finalPosition);
                } else {
                    player.setPosition(newPosition);
                    System.out.printf("%s moves to square %d.%n", player.getName(), player.getPosition());
                }
            }
        }
    }
}
