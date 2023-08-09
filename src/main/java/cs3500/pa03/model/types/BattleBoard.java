package cs3500.pa03.model.types;

/**
 * The `BattleBoard` enum represents the possible states of a cell on a battle board.
 * Each enum constant represents a specific state of the cell.
 * O - Represents an empty cell.
 * S - Represents a cell occupied by a submarine.
 * D - Represents a cell occupied by a destroyer.
 * C - Represents a cell occupied by a cruiser.
 * B - Represents a cell occupied by a battleship.
 * X - Represents a cell that has been hit.
 */

public enum BattleBoard {
  O, S, D, C, B, X
}
