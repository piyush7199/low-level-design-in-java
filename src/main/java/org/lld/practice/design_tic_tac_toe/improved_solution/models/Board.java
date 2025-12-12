package org.lld.practice.design_tic_tac_toe.improved_solution.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board.
 */
public class Board {
    private final int size;
    private final PlayerSymbol[][] grid;
    private final List<Move> moveHistory;
    
    public Board(int size) {
        this.size = size;
        this.grid = new PlayerSymbol[size][size];
        this.moveHistory = new ArrayList<>();
    }
    
    public int getSize() {
        return size;
    }
    
    /**
     * Makes a move on the board.
     * 
     * @param move The move to make
     * @return true if move is valid and made, false otherwise
     */
    public boolean makeMove(Move move) {
        if (!isValidMove(move)) {
            return false;
        }
        
        grid[move.getRow()][move.getCol()] = move.getSymbol();
        moveHistory.add(move);
        return true;
    }
    
    /**
     * Checks if a move is valid.
     * 
     * @param move The move to check
     * @return true if valid, false otherwise
     */
    public boolean isValidMove(Move move) {
        if (move.getRow() < 0 || move.getRow() >= size || 
            move.getCol() < 0 || move.getCol() >= size) {
            return false;
        }
        return grid[move.getRow()][move.getCol()] == null;
    }
    
    /**
     * Gets the symbol at a position.
     * 
     * @param row Row index
     * @param col Column index
     * @return The symbol at the position, or null if empty
     */
    public PlayerSymbol getSymbol(int row, int col) {
        return grid[row][col];
    }
    
    /**
     * Checks if the board is full.
     * 
     * @return true if board is full, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Gets the move history.
     * 
     * @return List of moves
     */
    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
    
    /**
     * Resets the board.
     */
    public void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = null;
            }
        }
        moveHistory.clear();
    }
}

