package cs3500.pa03.model.types;

/**
 * The ShipType class represents a type of ship in the game.
 */

public class ShipType {
  private final String shipType;

  /**
   * Constructs a new ShipType object with the specified name.
   *
   * @param name the name of the ship type
   */

  public ShipType(String name) {
    this.shipType = name.toLowerCase();
  }

  /**
   * Returns the name of the ship type.
   *
   * @return the ship types name
   */

  public String toString() {
    return this.shipType;
  }

  /**
   * Returns the length according to the ship type.
   *
   * @return the ship types length
   */

  public int getLength() {
    switch (shipType.toLowerCase()) {
      case "carrier" -> {
        return 6;
      }
      case "battleship" -> {
        return 5;
      }
      case "destroyer" -> {
        return 4;
      }
      case "submarine" -> {
        return 3;
      }
      default -> {
        return -1;
      }
    }
  }
}
