package org.lld.practice.design_tic_tac_toe.improved_solution.models;

/**
 * Represents a move in the game.
 */
public class Move {
    private final int row;
    private final int col;
    private final PlayerSymbol symbol;
    
    public Move(int row, int col, PlayerSymbol symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public PlayerSymbol getSymbol() {
        return symbol;
    }
}

