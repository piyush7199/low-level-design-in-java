package org.lld.practice.design_tic_tac_toe.naive_solution;

/**
 * Demo of naive Tic-Tac-Toe implementation.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Naive Tic-Tac-Toe Game Demo ===\n");
        
        SimpleTicTacToe game = new SimpleTicTacToe();
        
        System.out.println("Initial board:");
        game.printBoard();
        
        System.out.println("\nMaking moves:");
        game.makeMove(0, 0); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 2); // X wins
        
        System.out.println("\nFinal board:");
        game.printBoard();
        
        System.out.println("\n=== Limitations ===");
        System.out.println("- Hardcoded win conditions");
        System.out.println("- No separation of concerns");
        System.out.println("- Not extensible for larger boards");
        System.out.println("- Cannot add AI players");
        System.out.println("- Mixed responsibilities");
    }
}

