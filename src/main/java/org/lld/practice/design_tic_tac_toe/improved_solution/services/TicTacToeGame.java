package org.lld.practice.design_tic_tac_toe.improved_solution.services;

import org.lld.practice.design_tic_tac_toe.improved_solution.models.Board;
import org.lld.practice.design_tic_tac_toe.improved_solution.models.GameStatus;
import org.lld.practice.design_tic_tac_toe.improved_solution.models.Move;
import org.lld.practice.design_tic_tac_toe.improved_solution.models.Player;
import org.lld.practice.design_tic_tac_toe.improved_solution.models.PlayerSymbol;
import org.lld.practice.design_tic_tac_toe.improved_solution.strategies.StandardWinDetector;
import org.lld.practice.design_tic_tac_toe.improved_solution.strategies.WinDetector;

/**
 * Main game controller for Tic-Tac-Toe.
 * Uses Strategy pattern for win detection and State pattern for game status.
 */
public class TicTacToeGame {
    private final Board board;
    private final Player playerX;
    private final Player playerO;
    private final WinDetector winDetector;
    private Player currentPlayer;
    private GameStatus status;
    
    public TicTacToeGame(String playerXId, String playerOId) {
        this(playerXId, playerOId, 3, new StandardWinDetector());
    }
    
    public TicTacToeGame(String playerXId, String playerOId, int boardSize, WinDetector winDetector) {
        this.board = new Board(boardSize);
        this.playerX = new Player(playerXId, PlayerSymbol.X);
        this.playerO = new Player(playerOId, PlayerSymbol.O);
        this.winDetector = winDetector;
        this.currentPlayer = playerX;
        this.status = GameStatus.IN_PROGRESS;
    }
    
    /**
     * Makes a move in the game.
     * 
     * @param row Row index (0-based)
     * @param col Column index (0-based)
     * @return true if move is valid and made, false otherwise
     */
    public boolean makeMove(int row, int col) {
        if (status != GameStatus.IN_PROGRESS) {
            return false;
        }
        
        Move move = new Move(row, col, currentPlayer.getSymbol());
        
        if (!board.makeMove(move)) {
            return false;
        }
        
        // Check for win
        if (winDetector.hasWon(board, currentPlayer.getSymbol())) {
            status = GameStatus.WON;
            return true;
        }
        
        // Check for draw
        if (board.isFull()) {
            status = GameStatus.DRAW;
            return true;
        }
        
        // Switch player
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
        return true;
    }
    
    /**
     * Gets the current game status.
     * 
     * @return The game status
     */
    public GameStatus getStatus() {
        return status;
    }
    
    /**
     * Gets the current player.
     * 
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Gets the winner if game is won.
     * 
     * @return The winning player, or null if game is not won
     */
    public Player getWinner() {
        if (status == GameStatus.WON) {
            // Check which player won
            if (winDetector.hasWon(board, PlayerSymbol.X)) {
                return playerX;
            } else if (winDetector.hasWon(board, PlayerSymbol.O)) {
                return playerO;
            }
        }
        return null;
    }
    
    /**
     * Gets the board.
     * 
     * @return The game board
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Resets the game.
     */
    public void reset() {
        board.reset();
        currentPlayer = playerX;
        status = GameStatus.IN_PROGRESS;
    }
    
    /**
     * Prints the board to console.
     */
    public void printBoard() {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                PlayerSymbol symbol = board.getSymbol(i, j);
                System.out.print(symbol == null ? "-" : symbol);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

