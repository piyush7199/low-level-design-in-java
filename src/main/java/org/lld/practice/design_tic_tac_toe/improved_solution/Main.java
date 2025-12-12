package org.lld.practice.design_tic_tac_toe.improved_solution;

import org.lld.practice.design_tic_tac_toe.improved_solution.models.GameStatus;
import org.lld.practice.design_tic_tac_toe.improved_solution.models.Player;
import org.lld.practice.design_tic_tac_toe.improved_solution.services.TicTacToeGame;

/**
 * Demo of improved Tic-Tac-Toe game with proper design patterns.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Improved Tic-Tac-Toe Game Demo ===\n");
        
        TicTacToeGame game = new TicTacToeGame("alice", "bob");
        
        System.out.println("1. Initial board:");
        game.printBoard();
        System.out.println("Current player: " + game.getCurrentPlayer().getSymbol());
        
        System.out.println("\n2. Making moves:");
        // X's turn
        game.makeMove(0, 0);
        System.out.println("X plays at (0, 0)");
        game.printBoard();
        
        // O's turn
        game.makeMove(1, 1);
        System.out.println("\nO plays at (1, 1)");
        game.printBoard();
        
        // X's turn
        game.makeMove(0, 1);
        System.out.println("\nX plays at (0, 1)");
        game.printBoard();
        
        // O's turn
        game.makeMove(1, 0);
        System.out.println("\nO plays at (1, 0)");
        game.printBoard();
        
        // X's turn - wins!
        game.makeMove(0, 2);
        System.out.println("\nX plays at (0, 2)");
        game.printBoard();
        
        System.out.println("\n3. Game result:");
        System.out.println("Status: " + game.getStatus());
        if (game.getStatus() == GameStatus.WON) {
            Player winner = game.getWinner();
            System.out.println("Winner: " + winner.getPlayerId() + " (" + winner.getSymbol() + ")");
        }
        
        System.out.println("\n4. Move history:");
        game.getBoard().getMoveHistory().forEach(move -> 
            System.out.println("  " + move.getSymbol() + " at (" + move.getRow() + ", " + move.getCol() + ")")
        );
        
        System.out.println("\n5. Resetting game:");
        game.reset();
        System.out.println("Game reset. Status: " + game.getStatus());
        game.printBoard();
        
        System.out.println("\n=== Design Benefits ===");
        System.out.println("✓ Strategy pattern for win detection (extensible)");
        System.out.println("✓ State pattern for game status");
        System.out.println("✓ Separation of concerns (Board, Player, Game)");
        System.out.println("✓ Easy to extend for AI players");
        System.out.println("✓ Support for different board sizes");
        System.out.println("✓ Move history tracking");
    }
}

