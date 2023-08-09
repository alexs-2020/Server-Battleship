package cs3500.pa03.model.types;


import cs3500.pa03.model.player.Player;
import java.util.List;

/**
 * The Ship class represents a ship in the game.
 * Holds information about the ship's type and all its coordinates on the board/grid.
 */

public class Ship {
  private final ShipType shipType;
  private List<Coord> coordinates;
  private final String isVertical;

  /**
   * Constructs a new Ship object with the specified ship type and
   * list of coordinates representing where the ship has beeen placed.
   *
   * @param ship       the ship type of the ship
   * @param shipCoords the list of coordinates representing the ship's position
   */

  public Ship(ShipType ship, List<Coord> shipCoords, boolean direction) {
    this.shipType = ship;
    this.coordinates = shipCoords;
    if (direction) {
      this.isVertical = "VERTICAL";
    } else {
      this.isVertical = "HORIZONTAL";
    }

  }

  /**
   * Returns the list of coordinates representing the ship's position.
   *
   * @return the list of ship coordinates
   */

  public List<Coord> getCoords() {
    return this.coordinates;
  }

  /**
   * Removes a coordinate from the ship's list of coordinates.
   *
   * @param coordToRemove the coordinate to be removed
   */

  public void deleteCoord(Coord coordToRemove) {
    coordinates.remove(coordToRemove);
  }

  /**
   * Returns the ShipType/name of the ship.
   *
   * @return the ships name
   */

  public String name() {
    return this.shipType.toString();
  }

  public int getLength() {
    return this.shipType.getLength();
  }

  public String layout() {
    return this.isVertical;
  }
}
