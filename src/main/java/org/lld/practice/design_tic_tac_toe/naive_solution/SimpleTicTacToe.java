package org.lld.practice.design_tic_tac_toe.naive_solution;

/**
 * Naive implementation of Tic-Tac-Toe game.
 * 
 * This demonstrates common pitfalls:
 * - Hardcoded win conditions
 * - No separation of concerns
 * - Not extensible
 * - Mixed responsibilities
 */
public class SimpleTicTacToe {
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    
    public boolean makeMove(int row, int col) {
        if (gameOver || board[row][col] != '\0') {
            return false;
        }
        
        board[row][col] = currentPlayer;
        
        if (checkWin()) {
            gameOver = true;
            System.out.println("Player " + currentPlayer + " wins!");
        } else if (isBoardFull()) {
            gameOver = true;
            System.out.println("Game is a draw!");
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
        
        return true;
    }
    
    private boolean checkWin() {
        // Hardcoded win conditions - not extensible!
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }
        
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }
        }
        
        // Check diagonals
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }
        
        return false;
    }
    
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] == '\0' ? "-" : board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

