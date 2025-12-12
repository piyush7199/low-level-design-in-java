# Design A Tic-Tac-Toe Game

## 1. Problem Statement and Requirements

Our goal is to design a Tic-Tac-Toe game that supports two players, validates moves, detects wins/draws, and can be extended for multiplayer or AI opponents.

### Functional Requirements:

- **Game Board**: 3x3 grid for placing X and O
- **Player Management**: Support two players (X and O)
- **Move Validation**: Validate moves (check if cell is empty, valid position)
- **Win Detection**: Detect when a player wins (row, column, or diagonal)
- **Draw Detection**: Detect when the game ends in a draw
- **Game State**: Track current player, game status (IN_PROGRESS, WON, DRAW)
- **Move History**: Track all moves made
- **Reset Game**: Ability to start a new game

### Non-Functional Requirements:

- **Performance**: Fast move validation and win detection
- **Extensibility**: Easy to extend for larger boards or AI players
- **Maintainability**: Clean code structure for easy modifications

---

## 2. Naive Solution: The "Starting Point"

A beginner might use a simple 2D array and hardcode win conditions.

### The Thought Process:

"I'll use a 2D array to represent the board and check all win conditions with if statements."

```java
class TicTacToe {
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    
    public boolean makeMove(int row, int col) {
        if (board[row][col] != '\0') {
            return false; // Cell already occupied
        }
        board[row][col] = currentPlayer;
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        return true;
    }
    
    public boolean checkWin() {
        // Hardcoded win conditions...
        if (board[0][0] == board[0][1] && board[0][1] == board[0][2]) {
            return true;
        }
        // ... many more if statements
    }
}
```

### Limitations and Design Flaws:

1. **Hardcoded Logic**: Win conditions hardcoded, not extensible
2. **No Strategy Pattern**: Cannot easily add different game rules
3. **No State Management**: Game state not properly encapsulated
4. **Tight Coupling**: Board, players, and win detection all mixed together
5. **No Extensibility**: Hard to extend for larger boards or AI players

---

## 3. Improved Solution: The "Mentor's Guidance"

Separate board, players, win detection, and game state into independent components.

### Design Patterns Used:

1. **Strategy Pattern**: For different win detection strategies or game rules
2. **State Pattern**: For game states (IN_PROGRESS, WON, DRAW)
3. **Factory Pattern**: For creating different types of players (Human, AI)
4. **Observer Pattern**: For notifying about game events (move made, game won)

### Core Classes and Interactions:

1. **Game** (Orchestrator):
   - Main game controller
   - Manages game flow and state

2. **Board** (Model):
   - Represents the game board
   - Handles cell placement and validation

3. **Player** (Model/Interface):
   - Represents a player
   - Implementations: HumanPlayer, AIPlayer

4. **WinDetector** (Strategy Interface):
   - Defines win detection algorithm
   - Can support different board sizes or rules

5. **GameState** (State):
   - Tracks game status
   - States: IN_PROGRESS, WON, DRAW

### Key Design Benefits:

- **Extensibility**: Easy to add AI players or larger boards
- **Flexibility**: Strategy pattern allows different win detection rules
- **Maintainability**: Clear separation of concerns
- **Testability**: Each component can be tested independently

---

## 4. Final Design Overview

The improved solution uses Strategy, State, and Factory patterns to create an extensible, maintainable Tic-Tac-Toe game that can easily support AI players and different game variations.

