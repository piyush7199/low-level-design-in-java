# Design A Snake & Ladder

## 1. Problem Statement and Requirements

Our goal is to design a software system for a single-player Snake & Ladder game. The system should simulate the game on
a standard board.

### Functional Requirements:

- **Board:** The game must have a board with a predefined number of squares (e.g., 100). The board contains a start and
  an end square.
- **Snakes and Ladders:** The board must contain a predefined set of snakes and ladders, each connecting two squares.
- **Players:** The system should handle a single player. The player's position on the board must be tracked.
- **Dice:** A dice roll (random number from 1 to 6) should determine the number of steps to move.
- **Game Flow:**
    - The player starts at position 0.
    - The player rolls the dice and moves forward the number of squares shown on the dice.
    - If the player lands on the base of a ladder, they move up to the top of the ladder.
    - If the player lands on the head of a snake, they move down to the tail of the snake.
    - The player wins when they reach or exceed the final square (e.g., square 100).

### Non-Functional Requirements:

- **Maintainability:** It should be easy to change the board size or the positions of snakes and ladders.
- **Extensibility:** The design should allow for adding multiple players in the future without a complete redesign.
- **Testability:** The core game logic (dice roll, movement, snake/ladder checks) should be easily testable.

---

## 2. Naive Solution: The "Starting Point"

A simple, initial approach would be to use a single class, say Game, to manage everything.

### The Thought Process:

"The game is the main thing. It needs a board, a player, and a dice. I'll just put all the game logic, state, and rules
in one `Game` class."

```java
// A simple, monolithic approach
class Game {
    private int[] board; // Represents the board, maybe a 1D array
    private int playerPosition;
    private Map<Integer, Integer> snakesAndLadders;

    public void initializeGame() {
        // ... set up board, player position, snakes and ladders ...
    }

    public void rollDice() {
        // ... generate random number, update player position ...
        // ... check for snakes/ladders ...
        // ... check for win condition ...
    }
}
```

### Limitations and Design Flaws:

This monolithic design, while seemingly simple, quickly becomes problematic:

- **Violation of SOLID Principles:**
    - **Single Responsibility Principle (SRP):** The `Game` class is doing too much. It's responsible for the board's
      state, player management, dice mechanics, and all game rules. If we change the dice from 6-sided to 10-sided, we
      have to modify the core `Game` class.
    - **Open/Closed Principle (OCP):** Adding a second player would require opening the `Game` class and modifying its
      state variables (`playerPosition` would become a list of positions) and every method that interacts with the
      player.

- **Tight Coupling:** The `rollDice()` method is tightly coupled to the `Player`, `Board`, and `Snake` and `Ladder`
  logic. You can't test a dice roll in isolation from the game board.
- **Lack of Flexibility:** The design is hard-coded for one player. Making it multi-player would require significant
  refactoring.

This naive solution is like a one-room house—everything is in one place, which works for a very simple case but makes it
impossible to add new rooms (features) without a full rebuild.

---

## 3. Improved Solution: The "Mentor's Guidance"

The key to a good design is to separate concerns. We will break down the game into logical, independent components. This
allows for easier testing, maintenance, and future expansion.

### The "Why": The Strategy and Singleton Patterns

- **Dice Mechanics:** The dice roll is a separate concern. We can use a `Dice` class that encapsulates the logic of
  generating a random number. This makes it easy to swap in a different kind of dice later (e.g., a loaded dice for
  testing).
- **Board as a Singleton:** The game board is a single, central resource that all players interact with. The **Singleton
  pattern** ensures that there is only one instance of the board, preventing inconsistencies. All players will reference
  the same `Board` object.
- **Game Loop and State:** The game flow itself (taking turns, checking win conditions) is a high-level concern. A
  separate `GameEngine` or `Game` class will manage this loop, using composition to reference a list of players and the
  single `Board` instance. This makes the `GameEngine` a thin orchestrator.

### Core Classes and Their Interactions:

We will design a system of collaborating classes, each with a single, clear responsibility.

1. `Board` **(The Singleton):**
    - This class is the central, shared resource.
    - It contains the board's state, including the positions of all snakes and ladders.
    - It has methods like `getSnakeOrLadderEndPosition`(int `startPosition`) to look up a new position.
    - It is a **Singleton** to ensure all players interact with the same board.
2. `Dice` **(The Utility):**
    - This class encapsulates the logic for a single dice roll.
    - It has a method like `roll()` that returns an integer from 1 to 6.
    - This follows SRP and is easy to test in isolation.
3. `Player` **(The Entity)**:
    - A simple class representing a player.
    - It holds the player's name and their current position on the board.
    - It has methods to update its position.
4. `GameEngine` **(The Orchestrator)**:
    - This is the main class that runs the game.
    - It composes a list of `Player` objects, a `Board` object, and a `Dice` object.
    - Its methods, like `playGame()`, manage the game loop:
        - It iterates through the players.
        - It calls `dice.roll()`.
        - It updates the player's position.
        - It checks the `Board` for a snake or ladder at the new position.
        - It checks for the win condition.

---

## 4. Final Design Overview

Our final design is well-structured system with a clear separation of concerns.

- The `Board` class is centralized, single source of truth for the game board's configuration.
- The `Dice` class handles the random number generation, keeping the logic separate and reusable.
- The `Player` class cleanly encapsulate a player's state.
- The `GameEngine` acts as a high-level manager, orchestrating the interaction between all other components without
  getting bogged down in low-level details.

This design is:

- **More Maintainable:** Changing the rule (e.g., adding a bonus move on a specific square) is new method on the `Board`
  or new piece of logic in the `GameEngine`, leaving other components untouched.
- **More Extensible:** Adding multiple players is as simple as making the `GameEngine`'s player field a `List<Player>`.
  The `Dice` and `Board` classes don't need any changes
- **More Testable:** You can easily test `Board.getSnakeOrLadderEndPosition(int)` with a specific input, or test the
  `Dice.roll()` method, without having to set up the entire game state.
