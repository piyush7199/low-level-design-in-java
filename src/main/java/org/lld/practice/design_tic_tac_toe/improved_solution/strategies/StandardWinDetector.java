package org.lld.practice.design_tic_tac_toe.improved_solution.strategies;

import org.lld.practice.design_tic_tac_toe.improved_solution.models.Board;
import org.lld.practice.design_tic_tac_toe.improved_solution.models.PlayerSymbol;

/**
 * Standard win detector for 3x3 Tic-Tac-Toe.
 * Checks rows, columns, and diagonals.
 */
public class StandardWinDetector implements WinDetector {
    
    @Override
    public boolean hasWon(Board board, PlayerSymbol symbol) {
        int size = board.getSize();
        
        // Check rows
        for (int i = 0; i < size; i++) {
            boolean rowWin = true;
            for (int j = 0; j < size; j++) {
                if (board.getSymbol(i, j) != symbol) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
        }
        
        // Check columns
        for (int j = 0; j < size; j++) {
            boolean colWin = true;
            for (int i = 0; i < size; i++) {
                if (board.getSymbol(i, j) != symbol) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                return true;
            }
        }
        
        // Check main diagonal
        boolean diag1Win = true;
        for (int i = 0; i < size; i++) {
            if (board.getSymbol(i, i) != symbol) {
                diag1Win = false;
                break;
            }
        }
        if (diag1Win) {
            return true;
        }
        
        // Check anti-diagonal
        boolean diag2Win = true;
        for (int i = 0; i < size; i++) {
            if (board.getSymbol(i, size - 1 - i) != symbol) {
                diag2Win = false;
                break;
            }
        }
        return diag2Win;
    }
}

