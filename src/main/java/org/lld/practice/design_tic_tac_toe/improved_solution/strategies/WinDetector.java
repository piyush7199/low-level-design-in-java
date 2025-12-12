package org.lld.practice.design_tic_tac_toe.improved_solution.strategies;

import org.lld.practice.design_tic_tac_toe.improved_solution.models.Board;
import org.lld.practice.design_tic_tac_toe.improved_solution.models.PlayerSymbol;

/**
 * Strategy interface for win detection.
 * Different implementations can support different board sizes or win conditions.
 */
public interface WinDetector {
    /**
     * Checks if a player has won.
     * 
     * @param board The game board
     * @param symbol The player symbol to check
     * @return true if the player has won, false otherwise
     */
    boolean hasWon(Board board, PlayerSymbol symbol);
}

