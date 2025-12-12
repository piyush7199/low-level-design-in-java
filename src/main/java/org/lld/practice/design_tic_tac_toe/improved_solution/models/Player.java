package org.lld.practice.design_tic_tac_toe.improved_solution.models;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String playerId;
    private final PlayerSymbol symbol;
    
    public Player(String playerId, PlayerSymbol symbol) {
        this.playerId = playerId;
        this.symbol = symbol;
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public PlayerSymbol getSymbol() {
        return symbol;
    }
}

